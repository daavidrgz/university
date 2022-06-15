package es.udc.ws.app.client.service.exceptions;

public class ClientBookingCancellationNotAllowedException extends Exception{
    public ClientBookingCancellationNotAllowedException(String desc) {
        super(desc);
    }
}
