package es.udc.ws.app.model.trip;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSqlTripDao implements SqlTripDao {

    @Override
    public Trip find(Connection connection, Long tripId) throws InstanceNotFoundException {

        String queryString = "SELECT city, description, startDate, price, maxSlots, " +
                             "remainingSlots, creationDate FROM Trip WHERE tripId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            int i = 1;
            preparedStatement.setLong(i, tripId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if ( !resultSet.next() )
                throw new InstanceNotFoundException(tripId, Trip.class.getName());

            String city = resultSet.getString(i++);
            String description = resultSet.getString(i++);
            LocalDateTime startDate = resultSet.getTimestamp(i++).toLocalDateTime();
            double price = resultSet.getDouble(i++);
            int maxSlots = resultSet.getInt(i++);
            int remainingSlots = resultSet.getInt(i++);
            LocalDateTime creationDate = resultSet.getTimestamp(i).toLocalDateTime();

            return new Trip(tripId, city, description, startDate, price, maxSlots, remainingSlots, creationDate);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Trip> findByCity(Connection connection, String city,
                                 LocalDateTime startDate, LocalDateTime endDate) {

        String queryString = "SELECT tripId, description, startDate, price, maxSlots, " +
                "remainingSlots, creationDate FROM Trip WHERE city = ?";

        if ( startDate != null && endDate != null )
            queryString += " AND startDate >= ? AND startDate <= ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            int i = 1;

            preparedStatement.setString(i++, city);

            if ( startDate != null && endDate != null ) {
                preparedStatement.setTimestamp(i++, Timestamp.valueOf(startDate));
                preparedStatement.setTimestamp(i, Timestamp.valueOf(endDate));
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            List<Trip> tripList = new ArrayList<>();

            while ( resultSet.next() ) {
                i = 1;
                Long tripId = resultSet.getLong(i++);
                String description = resultSet.getString(i++);
                LocalDateTime startDateTrip = resultSet.getTimestamp(i++).toLocalDateTime();
                double price = resultSet.getDouble(i++);
                int maxSlots = resultSet.getInt(i++);
                int remainingSlots = resultSet.getInt(i++);
                LocalDateTime creationDate = resultSet.getTimestamp(i).toLocalDateTime();

                tripList.add(new Trip(tripId, city, description,
                        startDateTrip, price, maxSlots, remainingSlots, creationDate));
            }

            return tripList;

        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Connection connection, Trip trip) throws InstanceNotFoundException {

        String queryString = "UPDATE Trip"+
                " SET city = ?, description = ?, startDate = ?, price = ?,"+
                " maxSlots = ?, remainingSlots = ?, creationDate = ? WHERE tripId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)){
            int i = 1;

            preparedStatement.setString(i++, trip.getCity());
            preparedStatement.setString(i++, trip.getDescription());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(trip.getStartDate()));
            preparedStatement.setDouble(i++, trip.getPrice());
            preparedStatement.setInt(i++, trip.getMaxSlots());
            preparedStatement.setInt(i++, trip.getRemainingSlots());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(trip.getCreationDate()));
            preparedStatement.setLong(i, trip.getId());

            int updatedRows = preparedStatement.executeUpdate();

            if ( updatedRows == 0 )
                throw new InstanceNotFoundException(trip.getId(),Trip.class.getName());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(Connection connection, Long tripId) throws InstanceNotFoundException {

        String queryString = "DELETE FROM Trip WHERE tripId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)){
            int i = 1;
            preparedStatement.setLong(i, tripId);

            int removeRows = preparedStatement.executeUpdate();

            if ( removeRows == 0 )
                throw new InstanceNotFoundException(tripId,Trip.class.getName());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
