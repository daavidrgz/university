package es.udc.ws.app.model.booking;

import es.udc.ws.app.model.trip.Trip;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractSqlBookingDao  implements SqlBookingDao{

    @Override
    public Booking find(Connection connection, Long bookingId)
            throws InstanceNotFoundException{

        String queryString = "SELECT tripId, bookingDate, email, creditCardNumber, size, " +
                "bookingPrice, cancellationDate FROM Booking WHERE bookingId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            int i = 1;
            preparedStatement.setLong(i, bookingId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if ( !resultSet.next() )
                throw new InstanceNotFoundException(bookingId, Booking.class.getName());

            Long tripId = resultSet.getLong(i++);
            LocalDateTime bookingDate = resultSet.getTimestamp(i++).toLocalDateTime();
            String email = resultSet.getString(i++);
            String creditCardNumber = resultSet.getString(i++);
            int size = resultSet.getInt(i++);
            double bookingPrice = resultSet.getDouble(i++);
            Timestamp timestamp = resultSet.getTimestamp(i);
            //Si en la base de datos está guardado como null
            LocalDateTime cancellationDate = timestamp==null?null:timestamp.toLocalDateTime();

            return new Booking(bookingId, tripId, email, creditCardNumber, size, bookingPrice, bookingDate,
                    cancellationDate);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Booking> findByEmail(Connection connection, String email){

        String queryString = " SELECT tripId, bookingDate, bookingId ,creditCardNumber, size, " +
                "bookingPrice, cancellationDate FROM Booking WHERE email = ? ORDER BY bookingDate DESC";

        try(PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            int i = 1;

            preparedStatement.setString(i++,email);

            /*Ejectutar query*/
            ResultSet resultSet = preparedStatement.executeQuery();

            /*Lista de books*/
            List<Booking> bookings = new ArrayList<Booking>();

            while(resultSet.next()){
                i = 1;

                Long tripId = resultSet.getLong(i++);
                LocalDateTime bookingDate = resultSet.getTimestamp(i++).toLocalDateTime();
                Long bookingId = resultSet.getLong(i++);
                String creditCardNumber = resultSet.getString(i++);
                int size = resultSet.getInt(i++);
                double bookingPrice = resultSet.getDouble(i++);
                Timestamp cDate = resultSet.getTimestamp(i++);

                LocalDateTime cancellationDate = cDate == null?null: cDate.toLocalDateTime();
                bookings.add(new Booking(bookingId,tripId,email,creditCardNumber,size,bookingPrice,bookingDate,cancellationDate));

            }

            return bookings;

        }catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    public void update(Connection connection, Booking booking) throws InstanceNotFoundException {

        String queryString = "UPDATE Booking" +
                " SET tripId = ?, bookingDate = ?,  BookingId= ?, email = ?," +
                " creditCardNumber = ?, size = ?, bookingPrice = ?, cancellationDate = ?" +
                " WHERE bookingId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)){

            int i = 1;

            preparedStatement.setLong(i++, booking.getTripId());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(booking.getBookingDate()));
            preparedStatement.setLong(i++,booking.getId());
            preparedStatement.setString(i++, booking.getEmail());
            preparedStatement.setString(i++, booking.getCreditCardNumber());
            preparedStatement.setInt(i++, booking.getSize());
            preparedStatement.setDouble(i++, booking.getBookingPrice());
            //Para insertar date null

            LocalDateTime cDate = booking.getCancellationDate();
            preparedStatement.setTimestamp(i++, cDate == null?null : Timestamp.valueOf(cDate));
            preparedStatement.setLong(i, booking.getId());
            int updatedRows = preparedStatement.executeUpdate();

            if ( updatedRows == 0 )
                throw new InstanceNotFoundException(booking.getId(),Booking.class.getName());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void remove(Connection connection, Long bookingId) throws  InstanceNotFoundException {
        String queryString = "DELETE FROM Booking WHERE bookingId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)){
            int i = 1;
            preparedStatement.setLong(i, bookingId);

            int removedRows = preparedStatement.executeUpdate();

            if ( removedRows == 0 )
                throw new InstanceNotFoundException(bookingId, Booking.class.getName());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
