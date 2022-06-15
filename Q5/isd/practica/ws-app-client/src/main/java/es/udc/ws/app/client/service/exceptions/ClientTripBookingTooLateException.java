package es.udc.ws.app.client.service.exceptions;


public class ClientTripBookingTooLateException extends Exception{
    public ClientTripBookingTooLateException(String desc) {
        super(desc);
    }
}
