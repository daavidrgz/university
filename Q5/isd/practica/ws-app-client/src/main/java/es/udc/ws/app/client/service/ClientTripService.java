package es.udc.ws.app.client.service;

import es.udc.ws.app.client.service.dto.ClientBookingDto;
import es.udc.ws.app.client.service.dto.ClientTripDto;
import es.udc.ws.app.client.service.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.time.LocalDate;
import java.util.List;

public interface ClientTripService {
    ClientTripDto addTrip(ClientTripDto trip) throws InputValidationException;

    void updateTrip(ClientTripDto trip) throws InputValidationException,
            InstanceNotFoundException, ClientTripModifyTimeExpiredException,
            ClientTripStartDateEarlierException, ClientTripSlotsReducedException;

    //Utilizar LocalDate en lugar de LocalDateTime, es más cómodo espeficiar
    //sólo la fecha. El service ya se ocupa de transformarlo al LocalDateTime
    //que usa el modelo
    List<ClientTripDto> findTripByCity(String city, LocalDate startDate,
                              LocalDate endDate);

    Long bookTrip(Long tripId,String email, int size,String creditCardNumber)
            throws InstanceNotFoundException,InputValidationException,
            ClientTripBookingTooLateException, ClientTripNoRemainingSlotsException;

    void cancelBooking(Long bookingId, String email) throws
            InputValidationException,InstanceNotFoundException,
            ClientBookingCancellationTooLateException, ClientBookingAlreadyCancelledException,
            ClientBookingCancellationNotAllowedException;

    List<ClientBookingDto> getBookings(String email) throws InputValidationException;

}
