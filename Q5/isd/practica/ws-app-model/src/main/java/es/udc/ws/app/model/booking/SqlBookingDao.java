package es.udc.ws.app.model.booking;

import java.sql.Connection;
import java.util.List;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

public interface SqlBookingDao {

    Booking create(Connection connection, Booking booking);

    Booking find(Connection connection, Long bookingId) throws InstanceNotFoundException;

    List<Booking> findByEmail(Connection connection, String email);

    void update(Connection connection, Booking booking) throws InstanceNotFoundException;

    //Para test
    void remove(Connection connection, Long bookingId) throws  InstanceNotFoundException;
}
