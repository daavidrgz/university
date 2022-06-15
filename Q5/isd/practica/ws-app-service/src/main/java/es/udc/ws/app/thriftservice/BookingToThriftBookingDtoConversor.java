package es.udc.ws.app.thriftservice;

import es.udc.ws.app.model.booking.Booking;
import es.udc.ws.app.thrift.ThriftBookingDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingToThriftBookingDtoConversor {


    public static List<ThriftBookingDto> toThriftBookingDtos(List<Booking> bookings){

        List<ThriftBookingDto> thriftBookingDtos = new ArrayList<>();

        for(Booking book : bookings){
            thriftBookingDtos.add(toThriftBookingDto(book));
        }

        return thriftBookingDtos;

    }

    public static ThriftBookingDto toThriftBookingDto(Booking booking){

        String ccNumber =booking.getCreditCardNumber();
        String lastCCdigits = ccNumber.substring(ccNumber.length()-4);
        String cancellationDate = booking.getCancellationDate() !=null?
                booking.getCancellationDate().toString():null;

        ThriftBookingDto thriftBookingDto = new ThriftBookingDto(booking.getId(),booking.getTripId(),
                booking.getBookingDate().toString(),booking.getEmail(),lastCCdigits,
                booking.getSize(),booking.getBookingPrice()
                ,cancellationDate);

        return thriftBookingDto;
    }
}
