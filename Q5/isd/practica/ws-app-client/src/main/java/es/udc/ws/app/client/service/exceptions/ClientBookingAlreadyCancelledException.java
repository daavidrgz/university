package es.udc.ws.app.client.service.exceptions;

public class ClientBookingAlreadyCancelledException extends Exception{
    public ClientBookingAlreadyCancelledException(String desc) {
        super(desc);
    }
}
