package es.udc.ws.app.model.tripservice.exceptions;

public class TripSlotsReducedException extends Exception {
    public TripSlotsReducedException() {
        super("Cannot update trip, cannot lower max slots below booked slots");
    }
}
