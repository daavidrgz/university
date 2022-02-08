package es.udc.ws.app.model.tripservice.exceptions;

public class BookingAlreadyCancelledException extends Exception{
    public BookingAlreadyCancelledException() {
        super("Cannot cancel booking, it is already cancelled");
    }
}
