package es.udc.ws.app.thriftservice;

import es.udc.ws.app.model.booking.Booking;
import es.udc.ws.app.model.trip.Trip;
import es.udc.ws.app.model.tripservice.TripServiceFactory;
import es.udc.ws.app.model.tripservice.exceptions.*;
import es.udc.ws.app.restservice.dto.RestTripDto;
import es.udc.ws.app.thrift.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

public class ThriftTripServiceImpl implements ThriftTripService.Iface {

    @Override
    public ThriftTripDto addTrip(ThriftTripDto trip) throws ThriftInputValidationException{

        try{
            Trip newTrip = TripToThriftTripDtoConversor.toTrip(trip);
            newTrip = TripServiceFactory.getService().addTrip(newTrip);
            ThriftTripDto thriftTripDto =  TripToThriftTripDtoConversor.toThriftTripDto(newTrip);

            return thriftTripDto;

        }catch(InputValidationException e){
            throw new ThriftInputValidationException(e.getMessage());
        }

    }

    @Override
    public void updateTrip(ThriftTripDto trip) throws ThriftInputValidationException,ThriftInstanceNotFoundException,
            ThriftTripModifyTimeExpiredException,ThriftTripStartDateEarlierException,ThriftTripSlotsReducedException{
        try{
            Trip convertedTrip = TripToThriftTripDtoConversor.toTrip(trip);
            TripServiceFactory.getService().updateTrip(convertedTrip);
        }catch (InputValidationException e){
            throw new ThriftInputValidationException(e.getMessage());
        }catch (InstanceNotFoundException e){
            throw new ThriftInstanceNotFoundException(e.getInstanceId().toString(),
                e.getInstanceType().substring(e.getInstanceType().lastIndexOf('.') + 1));
        }catch (TripModifyTimeExpiredException e){
            throw new ThriftTripModifyTimeExpiredException(e.getMessage());
        }catch (TripStartDateEarlierException e){
            throw new ThriftTripStartDateEarlierException(e.getMessage());
        }catch (TripSlotsReducedException e){
            throw new ThriftTripSlotsReducedException(e.getMessage());
        }
    }

    @Override
    public List<ThriftTripDto> findTripByCity(String city, String startDateStr, String endDateStr) {
        LocalDateTime startDate = LocalDate.parse(startDateStr).atStartOfDay();
        LocalDateTime endDate = LocalDate.parse(endDateStr).atTime(23,59,59);

        List<Trip> trips = TripServiceFactory.getService().findTripByCity(city, startDate, endDate);

        return TripToThriftTripDtoConversor.toThriftTripDtos(trips);
    }

    @Override
    public long bookTrip(long tripId, String email, int size, String creditCardNumber) throws ThriftInputValidationException, ThriftInstanceNotFoundException, ThriftTripBookingTooLateException, ThriftTripNoRemainingSlotsException {
        try {
            return TripServiceFactory.getService().bookTrip(tripId, email, size, creditCardNumber);
        } catch (InputValidationException e) {
            throw new ThriftInputValidationException(e.getMessage());
        } catch (InstanceNotFoundException e) {
            throw new ThriftInstanceNotFoundException(e.getInstanceId().toString(),
                    e.getInstanceType().substring(e.getInstanceType().lastIndexOf('.') + 1));
        } catch (TripBookingTooLateException e) {
            throw new ThriftTripBookingTooLateException(e.getMessage());
        } catch (TripNoRemainingSlotsException e) {
            throw new ThriftTripNoRemainingSlotsException(e.getMessage());
        }
    }

    @Override
    public void cancelBooking(long bookingId, String email) throws ThriftInputValidationException, ThriftInstanceNotFoundException, ThriftBookingCancellationTooLateException, ThriftBookingAlreadyCancelledException, ThriftBookingCancellationNotAllowedException {
        try{
            TripServiceFactory.getService().cancelBooking(bookingId,email);
        }catch(InputValidationException e){
            throw new ThriftInputValidationException(e.getMessage());
        } catch (BookingCancellationTooLateException e) {
            throw new ThriftBookingCancellationTooLateException(e.getMessage());
        } catch (BookingCancellationNotAllowedException e) {
            throw new ThriftBookingCancellationNotAllowedException(e.getMessage());
        } catch (InstanceNotFoundException e) {
            throw new ThriftInstanceNotFoundException(e.getInstanceId().toString(),
                    e.getInstanceType().substring(e.getInstanceType().lastIndexOf('.') + 1));
        } catch (BookingAlreadyCancelledException e) {
            throw new ThriftBookingAlreadyCancelledException(e.getMessage());
        }
    }

    @Override
    public List<ThriftBookingDto> getBookings(String email) throws ThriftInputValidationException {
        try {
            List<Booking> modelBookings = TripServiceFactory.getService().getBookings(email);
            return BookingToThriftBookingDtoConversor.toThriftBookingDtos(modelBookings);
        } catch (InputValidationException e) {
            throw new ThriftInputValidationException(e.getMessage());
        }
    }
}
