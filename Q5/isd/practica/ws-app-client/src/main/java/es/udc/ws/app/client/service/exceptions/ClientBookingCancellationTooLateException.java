package es.udc.ws.app.client.service.exceptions;

public class ClientBookingCancellationTooLateException extends Exception{
    public ClientBookingCancellationTooLateException(String desc) {
        super(desc);
    }
}
