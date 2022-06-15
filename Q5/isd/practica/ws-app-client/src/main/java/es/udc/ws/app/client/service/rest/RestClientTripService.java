package es.udc.ws.app.client.service.rest;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.client.service.ClientTripService;
import es.udc.ws.app.client.service.dto.ClientBookingDto;
import es.udc.ws.app.client.service.dto.ClientTripDto;
import es.udc.ws.app.client.service.exceptions.*;
import es.udc.ws.app.client.service.rest.json.JsonToClientBookingDtoConversor;
import es.udc.ws.app.client.service.rest.json.JsonToClientExceptionConversor;
import es.udc.ws.app.client.service.rest.json.JsonToClientTripDtoConversor;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

public class RestClientTripService implements ClientTripService {
    private final static String ENDPOINT_ADDRESS_PARAMETER = "RestClientTripService.endpointAddress";
    private String endpointAddress;

    @Override
    public ClientTripDto addTrip(ClientTripDto trip) throws InputValidationException{
        try{
            HttpResponse response = Request.Post(getEndpointAddress()+"trips")
                    .bodyStream(toInputStream(trip), ContentType.create("application/json"))
                    .execute().returnResponse();
            validateStatusCode(HttpStatus.SC_CREATED,response);
            return JsonToClientTripDtoConversor.toClientTripDto(response.getEntity().getContent());
        }catch(InputValidationException e){
            throw e;
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateTrip(ClientTripDto trip) throws InputValidationException, InstanceNotFoundException, ClientTripModifyTimeExpiredException, ClientTripStartDateEarlierException, ClientTripSlotsReducedException {
        try {
            HttpResponse response = Request.Put(getEndpointAddress() + "trips/" + trip.getId())
                    .bodyStream(toInputStream(trip), ContentType.create("application/json"))
                    .execute().returnResponse();
            validateStatusCode(HttpStatus.SC_NO_CONTENT, response);

        } catch (InputValidationException | InstanceNotFoundException |
                ClientTripModifyTimeExpiredException | ClientTripStartDateEarlierException |
                ClientTripSlotsReducedException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ClientTripDto> findTripByCity(String city, LocalDate startDate, LocalDate endDate) {
        try{
            HttpResponse response = Request.Get(getEndpointAddress()+"trips?city="+ URLEncoder.encode(city, StandardCharsets.UTF_8)+"&startDate="
                    + startDate.toString()+"&endDate="+endDate.toString()).execute().returnResponse();
            validateStatusCode(HttpStatus.SC_OK,response);
            return JsonToClientTripDtoConversor.toClientTripDtos(response.getEntity().getContent());
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Long bookTrip(Long tripId, String email, int size, String creditCardNumber) throws InstanceNotFoundException, InputValidationException, ClientTripBookingTooLateException, ClientTripNoRemainingSlotsException {
        try {
            HttpResponse response = Request.Post(getEndpointAddress() + "bookings")
                    .bodyForm(Form.form()
                            .add("tripId", Long.toString(tripId))
                            .add("email", email)
                            .add("size", Integer.toString(size))
                            .add("creditCardNumber", creditCardNumber)
                            .build())
                    .execute().returnResponse();
            validateStatusCode(HttpStatus.SC_CREATED, response);

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            //Si casteo a LongNode, da una excepción, ya que el readTree puede interpretar
            //que es un IntNode si el id es un número pequeño
            NumericNode idNode = (NumericNode) objectMapper.readTree(response.getEntity().getContent());
            return idNode.longValue();
        } catch (InstanceNotFoundException | InputValidationException |
                ClientTripBookingTooLateException | ClientTripNoRemainingSlotsException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void cancelBooking(Long bookingId, String email) throws InputValidationException, InstanceNotFoundException, ClientBookingCancellationTooLateException, ClientBookingAlreadyCancelledException, ClientBookingCancellationNotAllowedException {
        try{
            HttpResponse response = Request.Post(getEndpointAddress()+"bookings/"+bookingId+"/cancel").
                    bodyForm(
                            Form.form().
                                    add("email",email).build()).
                    execute().returnResponse();
            validateStatusCode(HttpStatus.SC_NO_CONTENT,response);
        }catch (InputValidationException | InstanceNotFoundException |
                ClientBookingCancellationTooLateException | ClientBookingAlreadyCancelledException |
                ClientBookingCancellationNotAllowedException e){
            throw e;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ClientBookingDto> getBookings(String email) throws InputValidationException {
        try {
            HttpResponse response = Request.Get(getEndpointAddress() + "bookings?email=" + URLEncoder.encode(email, StandardCharsets.UTF_8))
                    .execute().returnResponse();
            validateStatusCode(HttpStatus.SC_OK, response);
            return JsonToClientBookingDtoConversor.toClientBookingDtos(response.getEntity().getContent());
        }catch(InputValidationException e){
            throw e;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private synchronized String getEndpointAddress(){
        if(endpointAddress ==null){
            endpointAddress= ConfigurationParametersManager.getParameter(ENDPOINT_ADDRESS_PARAMETER);
        }
        return endpointAddress;
    }
    private InputStream toInputStream(ClientTripDto trip){
        try{
            ByteArrayOutputStream outputStream= new ByteArrayOutputStream();
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            objectMapper.writer(new DefaultPrettyPrinter()).writeValue(outputStream,
                    JsonToClientTripDtoConversor.toObjectNode(trip));
            return new ByteArrayInputStream(outputStream.toByteArray());
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
    private void validateStatusCode(int successCode,HttpResponse response) throws Exception{
        try {
            int statusCode = response.getStatusLine().getStatusCode();
            if ( statusCode == successCode ) return;

            switch ( statusCode ) {
                case HttpStatus.SC_BAD_REQUEST:
                    throw JsonToClientExceptionConversor.fromBadRequestErrorCode(
                            response.getEntity().getContent());
                case HttpStatus.SC_FORBIDDEN:
                    throw JsonToClientExceptionConversor.fromForbiddenErrorCode(
                            response.getEntity().getContent());
                case HttpStatus.SC_GONE:
                    throw JsonToClientExceptionConversor.fromGoneErrorCode(
                            response.getEntity().getContent());
                case HttpStatus.SC_NOT_FOUND:
                    throw JsonToClientExceptionConversor.fromNotFoundErrorCode(
                            response.getEntity().getContent());
                default:
                    throw new RuntimeException("HTTP error; status code = " + statusCode);
            }
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
