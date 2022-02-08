package es.udc.ws.app.model.tripservice.exceptions;

public class BookingCancellationNotAllowedException extends Exception{
    public BookingCancellationNotAllowedException() {
        super("Cannot cancel booking, provided email does not match the booking's email");
    }
}
