package es.udc.ws.app.client.service.exceptions;

public class ClientTripNoRemainingSlotsException extends Exception {
    public ClientTripNoRemainingSlotsException(String desc) {
        super(desc);
    }
}