package es.udc.ws.app.client.service.thrift;

import es.udc.ws.app.client.service.dto.ClientBookingDto;
import es.udc.ws.app.thrift.ThriftBookingDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import java.util.List;


public class ClientBookingDtoToThriftBookingDtoConversor {

    public static ClientBookingDto toClientBookingDto(ThriftBookingDto booking){

        LocalDateTime cDate= (booking.getCancellationDate() != null) ? LocalDateTime.parse(booking.getCancellationDate()):null;


        return new ClientBookingDto(
                booking.getId(),
                booking.getTripId(),
                booking.getEmail(),
                booking.getLastCCDigits(),
                booking.getSize(),
                booking.getBookingPrice(),
                LocalDateTime.parse(booking.getBookingDate()),
                cDate
        );
    }

    public static List<ClientBookingDto> toClientBookingDtos(List<ThriftBookingDto> bookings){
            List<ClientBookingDto> clientBookingDtos = new ArrayList<>();

            for(ThriftBookingDto booking : bookings){
                clientBookingDtos.add(toClientBookingDto(booking));
            }

            return clientBookingDtos;
    }

}
