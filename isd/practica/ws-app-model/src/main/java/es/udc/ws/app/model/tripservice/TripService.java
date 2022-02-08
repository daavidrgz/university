package es.udc.ws.app.model.tripservice;

import es.udc.ws.app.model.booking.Booking;
import es.udc.ws.app.model.trip.Trip;
import es.udc.ws.app.model.tripservice.exceptions.*;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.exceptions.InputValidationException;


import java.time.LocalDateTime;
import java.util.List;

public interface TripService {
    Trip addTrip(Trip trip) throws InputValidationException;

    void updateTrip(Trip trip) throws InputValidationException,
            InstanceNotFoundException, TripModifyTimeExpiredException,
            TripStartDateEarlierException, TripSlotsReducedException;

    List<Trip> findTripByCity(String city, LocalDateTime startDate,
                              LocalDateTime endDate);

    Long bookTrip(Long tripId,String email, int size,String creditCardNumber)
            throws InstanceNotFoundException,InputValidationException,
            TripBookingTooLateException, TripNoRemainingSlotsException;

    void cancelBooking(Long bookingID, String email) throws
            InputValidationException,InstanceNotFoundException,
            BookingCancellationTooLateException, BookingAlreadyCancelledException,
            BookingCancellationNotAllowedException;

    List<Booking> getBookings(String email) throws InputValidationException;

}
