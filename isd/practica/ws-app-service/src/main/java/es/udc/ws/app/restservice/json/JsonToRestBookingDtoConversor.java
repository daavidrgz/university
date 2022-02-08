package es.udc.ws.app.restservice.json;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.restservice.dto.RestBookingDto;

import java.time.LocalDateTime;
import java.util.List;

public class JsonToRestBookingDtoConversor {

    public static ObjectNode toObjectNode(RestBookingDto booking) {
        ObjectNode bookingObject = JsonNodeFactory.instance.objectNode();
        if (booking.getId() != null) {
            bookingObject.put("id", booking.getId());
        }
        String cDate = booking.getCancellationDate()!=null?booking.getCancellationDate().toString():null;
        bookingObject.put("tripId", booking.getTripId()).
                put("email", booking.getEmail()).
                put("lastCCDigits", booking.getLastCCDigits()).
                put("size", booking.getSize()).
                put("bookingPrice", booking.getBookingPrice()).
                put("bookingDate", booking.getBookingDate().toString());

        if (cDate != null){
            //Mandar fecha de cancelación sólo si no es nula
            bookingObject.put("cancellationDate",cDate);
        }
        return bookingObject;
    }

    public static ArrayNode toArrayNode(List<RestBookingDto> bookings){
        ArrayNode bookingsNode = JsonNodeFactory.instance.arrayNode();
        for (RestBookingDto booking : bookings){
            ObjectNode bookObject = toObjectNode(booking);
            bookingsNode.add(bookObject);
        }
        return bookingsNode;
    }

}
