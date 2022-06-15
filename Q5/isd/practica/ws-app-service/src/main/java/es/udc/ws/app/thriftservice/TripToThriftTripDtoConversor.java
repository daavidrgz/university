package es.udc.ws.app.thriftservice;

import es.udc.ws.app.model.trip.Trip;
import es.udc.ws.app.thrift.ThriftTripDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TripToThriftTripDtoConversor {
    public static Trip toTrip(ThriftTripDto trip){
        Trip modelTrip = new Trip(trip.getCity(), trip.getDescription(),LocalDateTime.parse(trip.getStartDate()),trip.getPrice(),
                trip.getMaxSlots());

        //Esto es por el caso de uso de update, para que el id esté setted en el modelo
        if (trip.isSetId()){
            modelTrip.setId(trip.getId());
        }
        if (trip.isSetRemainingSlots()){
            modelTrip.setRemainingSlots(trip.getRemainingSlots());
        }
        return modelTrip;
    }
    public static List<ThriftTripDto> toThriftTripDtos(List<Trip> trips){
        List<ThriftTripDto> thriftTripDtos = new ArrayList<>();
        for(Trip trip : trips){
            thriftTripDtos.add(toThriftTripDto(trip));
        }
        return thriftTripDtos;
    }
    public static ThriftTripDto toThriftTripDto(Trip trip){
        ThriftTripDto thriftTripDto = new ThriftTripDto(trip.getCity(),trip.getDescription(),trip.getStartDate().toString(),
                trip.getPrice(),trip.getMaxSlots());
        //Esto lo hacemos así porque estos campos están como optional en el Dto, para no tener que mandar valores
        //inválidos, como por ejemplo, -1 en el id/remainingSlots
        if(trip.getId() != null){
            thriftTripDto.setId(trip.getId());
        }
        if(trip.getRemainingSlots() != null){
            thriftTripDto.setRemainingSlots(trip.getRemainingSlots());
        }
        return thriftTripDto;
    }
}
