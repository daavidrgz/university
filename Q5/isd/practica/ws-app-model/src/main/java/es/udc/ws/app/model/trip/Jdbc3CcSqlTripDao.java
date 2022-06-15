package es.udc.ws.app.model.trip;

import java.sql.*;

public class Jdbc3CcSqlTripDao extends AbstractSqlTripDao {

    @Override
    public Trip create(Connection connection, Trip trip) {

        String queryString = "INSERT INTO Trip " +
                             "(city, description, startDate, price, maxSlots, remainingSlots, creationDate) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString,
                Statement.RETURN_GENERATED_KEYS)) {
            int i = 1;
            preparedStatement.setString(i++, trip.getCity());
            preparedStatement.setString(i++, trip.getDescription());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(trip.getStartDate()));
            preparedStatement.setDouble(i++, trip.getPrice());
            preparedStatement.setInt(i++, trip.getMaxSlots());
            preparedStatement.setInt(i++, trip.getRemainingSlots());
            preparedStatement.setTimestamp(i, Timestamp.valueOf(trip.getCreationDate()));

            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if ( !resultSet.next() )
                throw new SQLException("JDBC driver did not return generated key.");

            Long tripId = resultSet.getLong(1);

            return new Trip(tripId, trip.getCity(), trip.getDescription(), trip.getStartDate(), trip.getPrice(),
                            trip.getMaxSlots(), trip.getRemainingSlots(), trip.getCreationDate());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
