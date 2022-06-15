package es.udc.ws.app.restservice.servlets;

import es.udc.ws.app.model.trip.Trip;
import es.udc.ws.app.model.tripservice.TripServiceFactory;
import es.udc.ws.app.model.tripservice.exceptions.TripModifyTimeExpiredException;
import es.udc.ws.app.model.tripservice.exceptions.TripSlotsReducedException;
import es.udc.ws.app.model.tripservice.exceptions.TripStartDateEarlierException;
import es.udc.ws.app.restservice.dto.RestTripDto;
import es.udc.ws.app.restservice.dto.TripToRestTripDtoConversor;
import es.udc.ws.app.restservice.json.AppExceptionToJsonConversor;
import es.udc.ws.app.restservice.json.JsonToRestTripDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ExceptionToJsonConversor;
import es.udc.ws.util.servlet.RestHttpServletTemplate;
import es.udc.ws.util.servlet.ServletUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class TripsServlet extends RestHttpServletTemplate {

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse res) throws
            IOException, InputValidationException{
        ServletUtils.checkEmptyPath(req);

        RestTripDto tripDto = JsonToRestTripDtoConversor.toRestTripDto(req.getInputStream());
        Trip trip = TripToRestTripDtoConversor.toTrip(tripDto);

        trip = TripServiceFactory.getService().addTrip(trip);

        tripDto = TripToRestTripDtoConversor.toRestTripDto(trip);

        String tripURL = ServletUtils.normalizePath(req.getRequestURL().toString()+
                "/"+tripDto.getId());

        Map<String,String> headers = new HashMap<>(1);
        headers.put("Location",tripURL);
        ServletUtils.writeServiceResponse(res,HttpServletResponse.SC_CREATED,
                JsonToRestTripDtoConversor.toObjectNode(tripDto),headers);
    }
    @Override
    protected void processPut(HttpServletRequest req, HttpServletResponse res) throws
            IOException, InputValidationException,InstanceNotFoundException{
        Long tripId = ServletUtils.getIdFromPath(req,"trip");
        RestTripDto tripDto = JsonToRestTripDtoConversor.toRestTripDto(req.getInputStream());

        if (!tripId.equals(tripDto.getId())) {
            throw new InputValidationException("Invalid Request: invalid tripId");
        }
        
        Trip trip = TripToRestTripDtoConversor.toTrip(tripDto);
        try {
            TripServiceFactory.getService().updateTrip(trip);

            ServletUtils.writeServiceResponse(res, HttpServletResponse.SC_NO_CONTENT,null,null);

        }catch (TripModifyTimeExpiredException e){
            ServletUtils.writeServiceResponse(res, HttpServletResponse.SC_GONE, AppExceptionToJsonConversor.toTripModifyTimeExpiredException(e) , null);
        }catch (TripStartDateEarlierException e){
            ServletUtils.writeServiceResponse(res, HttpServletResponse.SC_BAD_REQUEST, AppExceptionToJsonConversor.toTripStartDateEarlierException(e), null);
        }catch (TripSlotsReducedException e){
            ServletUtils.writeServiceResponse(res, HttpServletResponse.SC_BAD_REQUEST, AppExceptionToJsonConversor.toTripSlotsReducedException(e), null);
        }

    }
    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse res) throws
            IOException, InputValidationException{
        ServletUtils.checkEmptyPath(req);
        String city = ServletUtils.getMandatoryParameter(req,"city");
        String startDateStr = ServletUtils.getMandatoryParameter(req,"startDate");
        String endDateStr = ServletUtils.getMandatoryParameter(req,"endDate");
        LocalDateTime startDate = LocalDate.parse(startDateStr).atStartOfDay();
        LocalDateTime endDate = LocalDate.parse(endDateStr).atTime(23,59,59);

        List<Trip> trips = TripServiceFactory.getService().findTripByCity(city,startDate,endDate);

        List<RestTripDto> tripDtos = TripToRestTripDtoConversor.toRestTripDtos(trips);

        ServletUtils.writeServiceResponse(res,HttpServletResponse.SC_OK,
                JsonToRestTripDtoConversor.toArrayNode(tripDtos),null);

    }
}
