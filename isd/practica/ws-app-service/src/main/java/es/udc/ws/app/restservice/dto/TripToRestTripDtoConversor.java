package es.udc.ws.app.restservice.dto;

import es.udc.ws.app.model.trip.Trip;

import java.util.ArrayList;
import java.util.List;

public class TripToRestTripDtoConversor {
    public static List<RestTripDto> toRestTripDtos(List<Trip> trips) {
        List<RestTripDto> tripDtos = new ArrayList<>(trips.size());
        for(Trip trip : trips){
            tripDtos.add(toRestTripDto(trip));
        }
        return tripDtos;
    }

    public static RestTripDto toRestTripDto(Trip trip) {
        return new RestTripDto(trip.getId(),trip.getCity(),trip.getDescription(),trip.getStartDate(), trip.getPrice(),
                trip.getMaxSlots(), trip.getRemainingSlots());
    }

    public static Trip toTrip(RestTripDto trip) {
        return new Trip(trip.getId(), trip.getCity(),trip.getDescription(),trip.getStartDate(), trip.getPrice(),
                trip.getMaxSlots(),trip.getRemainingSlots(),null);
    }
}
