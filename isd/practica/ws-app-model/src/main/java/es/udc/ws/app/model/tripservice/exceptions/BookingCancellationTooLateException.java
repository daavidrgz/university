package es.udc.ws.app.model.tripservice.exceptions;

import static es.udc.ws.app.model.util.ModelConstants.CANCELLATION_TIME_LIMIT;

public class BookingCancellationTooLateException extends Exception{
    public BookingCancellationTooLateException() {
        super("Cannot cancel booking, remaining time until the trip starts is less than " + CANCELLATION_TIME_LIMIT + "h");
    }
}
