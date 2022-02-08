package es.udc.ws.app.client.service.thrift;

import es.udc.ws.app.client.service.dto.ClientTripDto;
import es.udc.ws.app.thrift.ThriftTripDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ClientTripDtoToThriftTripDtoConversor {

    public static ThriftTripDto toThriftTripDto(ClientTripDto clientTripDto) {
        ThriftTripDto thriftTripDto = new ThriftTripDto(
                clientTripDto.getCity(),
                clientTripDto.getDescription(),
                clientTripDto.getStartDate().toString(),
                clientTripDto.getPrice(),
                clientTripDto.getMaxSlots());

        if ( clientTripDto.getId() != null )
            thriftTripDto.setId(clientTripDto.getId());

        // El modelo ignorará este atributo, pero se añadió por consistencia
        if ( clientTripDto.getBookedSlots() != null )
            thriftTripDto.setRemainingSlots(clientTripDto.getMaxSlots() - clientTripDto.getBookedSlots());

        return thriftTripDto;
    }

    public static ClientTripDto toClientTripDto(ThriftTripDto trip) {
        Long tripId = trip.isSetId() ? trip.getId() : null;
        Integer bookedSlots = trip.isSetRemainingSlots() ? trip.getMaxSlots() - trip.getRemainingSlots() : null;

        return new ClientTripDto(
                tripId,
                trip.getCity(),
                trip.getDescription(),
                LocalDateTime.parse(trip.getStartDate()),
                trip.getPrice(),
                trip.getMaxSlots(),
                bookedSlots);
    }

    public static List<ClientTripDto> toClientTripDtos(List<ThriftTripDto> trips) {
        List<ClientTripDto> clientTripDtos = new ArrayList<>();
        for ( ThriftTripDto thriftTrip : trips ) {
            clientTripDtos.add(toClientTripDto(thriftTrip));
        }
        return clientTripDtos;
    }
}
