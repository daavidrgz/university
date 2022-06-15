package es.udc.ws.app.client.service.exceptions;

public class ClientTripStartDateEarlierException extends Exception{
    public ClientTripStartDateEarlierException(String desc) {
        super(desc);
    }
}
