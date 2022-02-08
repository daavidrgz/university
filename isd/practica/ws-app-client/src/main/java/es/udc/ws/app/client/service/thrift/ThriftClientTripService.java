package es.udc.ws.app.client.service.thrift;

import es.udc.ws.app.client.service.ClientTripService;
import es.udc.ws.app.client.service.dto.ClientBookingDto;
import es.udc.ws.app.client.service.dto.ClientTripDto;
import es.udc.ws.app.client.service.exceptions.*;
import es.udc.ws.app.thrift.*;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.time.LocalDate;
import java.util.List;

public class ThriftClientTripService implements ClientTripService {

    private final static String ENDPOINT_ADDRESS_PARAMETER =
            "ThriftClientTripService.endpointAddress";

    private final static String endpointAddress =
            ConfigurationParametersManager.getParameter(ENDPOINT_ADDRESS_PARAMETER);

    @Override
    public ClientTripDto addTrip(ClientTripDto trip) throws InputValidationException {
        ThriftTripService.Client client = getClient();

        try (TTransport transport = client.getInputProtocol().getTransport()) {
            transport.open();
            ThriftTripDto thriftTrip = ClientTripDtoToThriftTripDtoConversor.toThriftTripDto(trip);
            return ClientTripDtoToThriftTripDtoConversor.toClientTripDto(client.addTrip(thriftTrip));

        }catch (ThriftInputValidationException e){
            throw new InputValidationException(e.getMessage());
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateTrip(ClientTripDto trip) throws InputValidationException, InstanceNotFoundException,
            ClientTripModifyTimeExpiredException, ClientTripStartDateEarlierException,
            ClientTripSlotsReducedException {
        ThriftTripService.Client client = getClient();

        try (TTransport transport = client.getInputProtocol().getTransport()){
            transport.open();
            ThriftTripDto thriftTrip = ClientTripDtoToThriftTripDtoConversor.toThriftTripDto(trip);
            client.updateTrip(thriftTrip);
        } catch (ThriftInputValidationException e){
            throw new InputValidationException(e.getMessage());
        } catch (ThriftInstanceNotFoundException e){
            throw new InstanceNotFoundException(e.getInstanceId(), e.getInstanceType());
        } catch (ThriftTripModifyTimeExpiredException e){
            throw new ClientTripModifyTimeExpiredException(e.getMessage());
        } catch (ThriftTripStartDateEarlierException e){
            throw new ClientTripStartDateEarlierException(e.getMessage());
        } catch (ThriftTripSlotsReducedException e){
            throw new ClientTripStartDateEarlierException(e.getMessage());
        } catch (Exception e){
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<ClientTripDto> findTripByCity(String city, LocalDate startDate, LocalDate endDate) {
        ThriftTripService.Client client = getClient();

        try (TTransport transport = client.getInputProtocol().getTransport()) {
            transport.open();

            return ClientTripDtoToThriftTripDtoConversor.toClientTripDtos(client.findTripByCity(
                    city, startDate.toString(), endDate.toString()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Long bookTrip(Long tripId, String email, int size, String creditCardNumber) throws InstanceNotFoundException, InputValidationException, ClientTripBookingTooLateException, ClientTripNoRemainingSlotsException {
        ThriftTripService.Client client = getClient();

        try (TTransport transport = client.getInputProtocol().getTransport()) {
            transport.open();

            return client.bookTrip(tripId, email, size, creditCardNumber);

        } catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch (ThriftInstanceNotFoundException e) {
            throw new InstanceNotFoundException(e.getInstanceId(), e.getInstanceType());
        } catch (ThriftTripBookingTooLateException e) {
            throw new ClientTripBookingTooLateException(e.getMessage());
        } catch (ThriftTripNoRemainingSlotsException e) {
            throw new ClientTripNoRemainingSlotsException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void cancelBooking(Long bookingId, String email) throws InputValidationException, InstanceNotFoundException, ClientBookingCancellationTooLateException, ClientBookingAlreadyCancelledException, ClientBookingCancellationNotAllowedException {
        ThriftTripService.Client client = getClient();
        try(TTransport transport = client.getInputProtocol().getTransport()){
            transport.open();

            client.cancelBooking(bookingId,email);
        } catch (ThriftBookingAlreadyCancelledException e) {
            throw new ClientBookingAlreadyCancelledException(e.getMessage());
        } catch (ThriftBookingCancellationNotAllowedException e) {
            throw new ClientBookingCancellationNotAllowedException(e.getMessage());
        } catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch (ThriftBookingCancellationTooLateException e) {
            throw new ClientBookingCancellationTooLateException(e.getMessage());
        } catch (ThriftInstanceNotFoundException e) {
            throw new InstanceNotFoundException(e.getInstanceId(),e.getInstanceType());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ClientBookingDto> getBookings(String email) throws InputValidationException {
        ThriftTripService.Client client = getClient();
        try(TTransport transport = client.getInputProtocol().getTransport()){
            transport.open();

            List<ThriftBookingDto> thriftBookings = client.getBookings(email);
            return ClientBookingDtoToThriftBookingDtoConversor.toClientBookingDtos(thriftBookings);

        } catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    private ThriftTripService.Client getClient() {
        try {
            TTransport transport = new THttpClient(endpointAddress);
            TProtocol protocol = new TBinaryProtocol(transport);

            return new ThriftTripService.Client(protocol);
        } catch (TTransportException e) {
            throw new RuntimeException(e);
        }
    }
}
