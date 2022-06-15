package es.udc.ws.app.model.tripservice.exceptions;

import static es.udc.ws.app.model.util.ModelConstants.BOOKING_TIME_LIMIT;

public class TripBookingTooLateException extends Exception{
    public TripBookingTooLateException() {
        super("Cannot book a slot, remaining time to start the trip is less than " + BOOKING_TIME_LIMIT + "h");
    }
}
