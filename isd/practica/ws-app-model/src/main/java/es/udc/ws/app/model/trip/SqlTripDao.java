package es.udc.ws.app.model.trip;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

public interface SqlTripDao {

    Trip create(Connection connection, Trip trip);

    Trip find(Connection connection, Long tripId)
            throws InstanceNotFoundException;

    List<Trip> findByCity(Connection connection, String city,
                          LocalDateTime startDate, LocalDateTime endDate);

    void update(Connection connection, Trip trip)
            throws InstanceNotFoundException;

    //Para test
    void remove(Connection connection, Long tripId)
            throws InstanceNotFoundException;
}
