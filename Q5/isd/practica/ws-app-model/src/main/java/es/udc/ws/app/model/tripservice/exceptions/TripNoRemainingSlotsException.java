package es.udc.ws.app.model.tripservice.exceptions;

public class TripNoRemainingSlotsException extends Exception {
    public TripNoRemainingSlotsException() {
        super("Cannot book trip, there are no enough slots available");
    }
}