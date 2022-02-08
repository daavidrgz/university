package es.udc.ws.app.model.tripservice.exceptions;

public class TripStartDateEarlierException extends Exception{
    public TripStartDateEarlierException() {
        super("Cannot update trip, start date must not be earlier");
    }
}
