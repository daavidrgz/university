package es.udc.ws.app.restservice.dto;

import es.udc.ws.app.model.booking.Booking;

import java.util.ArrayList;
import java.util.List;

public class BookingToRestBookingDtoConversor {
    public static List<RestBookingDto> toRestBookingDtos(List<Booking> bookings) {
        List<RestBookingDto> bookingDtos = new ArrayList<>(bookings.size());

        for (Booking booking : bookings){
            bookingDtos.add(toRestBookingDto(booking));
        }
        return bookingDtos;
    }

    public static RestBookingDto toRestBookingDto(Booking booking) {
        String ccNumber =booking.getCreditCardNumber();
        String lastCCdigits = ccNumber.substring(ccNumber.length()-4);
        return new RestBookingDto(booking.getId(), booking.getTripId(), booking.getEmail(), lastCCdigits,
                booking.getSize(), booking.getBookingPrice(), booking.getBookingDate(),booking.getCancellationDate());
    }
}
