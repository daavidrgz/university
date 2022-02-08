package es.udc.ws.app.model.booking;

import java.sql.*;
import java.time.LocalDateTime;

public class Jdbc3CcSqlBookingDao extends AbstractSqlBookingDao{

    @Override
    public Booking create(Connection connection, Booking booking) {

        String queryString = "INSERT INTO Booking " +
                "(tripId, bookingDate, email, creditCardNumber, size, bookingPrice, cancellationDate) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString,
                Statement.RETURN_GENERATED_KEYS)) {
            int i = 1;
            preparedStatement.setLong(i++, booking.getTripId());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(booking.getBookingDate()));
            preparedStatement.setString(i++,booking.getEmail());
            preparedStatement.setString(i++,booking.getCreditCardNumber());
            preparedStatement.setInt(i++, booking.getSize() );
            preparedStatement.setDouble(i++, booking.getBookingPrice());
            //Para insertar date null
            LocalDateTime cDate = booking.getCancellationDate();
            preparedStatement.setTimestamp(i, cDate ==null?null:Timestamp.valueOf(cDate));

            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if ( !resultSet.next() )
                throw new SQLException("JDBC driver did not return generated key.");

            Long bookingId = resultSet.getLong(1);

            return new Booking(bookingId, booking.getTripId(), booking.getEmail(), booking.getCreditCardNumber(),
                    booking.getSize(),booking.getBookingPrice(),booking.getBookingDate(), booking.getCancellationDate());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
