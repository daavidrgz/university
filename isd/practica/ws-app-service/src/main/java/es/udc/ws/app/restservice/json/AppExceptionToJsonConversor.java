package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.udc.ws.app.model.tripservice.exceptions.*;

public class AppExceptionToJsonConversor {
    public static ObjectNode toTripBookingTooLateException(TripBookingTooLateException ex) {
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "TripBookingTooLate");
        exceptionObject.put("message", ex.getMessage());

        return exceptionObject;
    }
    public static ObjectNode toTripModifyTimeExpiredException(TripModifyTimeExpiredException ex) {
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "TripModifyTimeExpired");
        exceptionObject.put("message", ex.getMessage());

        return exceptionObject;
    }
    public static ObjectNode toTripNoRemainingSlotsException(TripNoRemainingSlotsException ex) {
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "TripNoRemainingSlots");
        exceptionObject.put("message", ex.getMessage());

        return exceptionObject;
    }
    public static ObjectNode toTripStartDateEarlierException(TripStartDateEarlierException ex) {
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "TripStartDateEarlier");
        exceptionObject.put("message", ex.getMessage());

        return exceptionObject;
    }
    public static ObjectNode toTripSlotsReducedException(TripSlotsReducedException ex) {
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "TripSlotsReduced");
        exceptionObject.put("message", ex.getMessage());

        return exceptionObject;
    }

    public static ObjectNode toBookingCancellationTooLateException(BookingCancellationTooLateException ex) {
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "BookingCancellationTooLate");
        exceptionObject.put("message", ex.getMessage());

        return exceptionObject;
    }
    public static ObjectNode toBookingCancellationNotAllowedException(BookingCancellationNotAllowedException ex) {
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "BookingCancellationNotAllowed");
        exceptionObject.put("message", ex.getMessage());

        return exceptionObject;
    }
    public static ObjectNode toBookingAlreadyCancelledException(BookingAlreadyCancelledException ex) {
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "BookingAlreadyCancelled");
        exceptionObject.put("message", ex.getMessage());

        return exceptionObject;
    }
}
