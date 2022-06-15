package es.udc.ws.app.model.tripservice;

import es.udc.ws.app.model.booking.Booking;
import es.udc.ws.app.model.booking.SqlBookingDao;
import es.udc.ws.app.model.booking.SqlBookingDaoFactory;
import es.udc.ws.app.model.trip.SqlTripDao;
import es.udc.ws.app.model.trip.SqlTripDaoFactory;
import es.udc.ws.app.model.trip.Trip;

import es.udc.ws.app.model.tripservice.exceptions.*;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.validation.PropertyValidator;

import static es.udc.ws.app.model.util.ModelConstants.*;

public class TripServiceImpl implements TripService {
    private final DataSource dataSource;
    private SqlTripDao tripDao = null;
    private SqlBookingDao bookingDao = null;

    public TripServiceImpl() {
        dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);
        tripDao = SqlTripDaoFactory.getDao();
        bookingDao = SqlBookingDaoFactory.getDao();
    }

    private void validateTrip(Trip trip) throws InputValidationException{
        PropertyValidator.validateMandatoryString("city",trip.getCity());
        PropertyValidator.validateMandatoryString("description",trip.getDescription());
        if(trip.getPrice()<0){
            throw new InputValidationException("Invalid Price " +
                    "value (it must be greater than 0)");
        }
        if(trip.getMaxSlots()<=0){
            throw new InputValidationException("Invalid maxSlots " +
                    "value (it must be greater than 0)");
        }
        if(trip.getStartDate() == null){
            throw new InputValidationException("Invalid startDate " +
                    "value (trip must have a startDate)");
        }
        //Si quitandole 72h la fecha de inicio del trip es anterior a la fecha de alta, lanzar error


    }
    private void validateEmail(String email) throws InputValidationException{
        //Regex para que en el email pueda haber carácteres alfannuméricos y ,'+', '_','.','-'
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9+_.-]+$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher =pattern.matcher(email);
        if(!matcher.matches()){
            throw new InputValidationException("Invalid email value (Valid chars are: alphanumeric," +
                    " '+', '_', '.','-')");
        }
    }

    @Override
    public Trip addTrip(Trip trip) throws InputValidationException {

        trip.setCreationDate(LocalDateTime.now());
        trip.setRemainingSlots(trip.getMaxSlots());
        //Validar después de poner el creationDate, para comprobar que la fecha de inicio
        //es 72h posterior a la del alta
        validateTrip(trip);
        //Validar caso específico del add trip. No queremos validar esto en updateTrip, por ejemplo.
        if(trip.getStartDate().minusHours(MIN_START_CREATION_HOURS_DIFF).isBefore(trip.getCreationDate())){
            throw new InputValidationException("Invalid startDate " +
                    "value (startDate must be at least +"+MIN_START_CREATION_HOURS_DIFF+
                    "h after creationDate)");
        }

        try (Connection connection = dataSource.getConnection()) {

            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                Trip createdTrip = tripDao.create(connection, trip);

                connection.commit();

                return createdTrip;

            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void updateTrip(Trip trip) throws InputValidationException, InstanceNotFoundException, TripModifyTimeExpiredException, TripStartDateEarlierException, TripSlotsReducedException {

            validateTrip(trip);

            try(Connection connection = dataSource.getConnection()){

                try{

                    connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                    connection.setAutoCommit(false);

                    Trip DBTrip = tripDao.find(connection, trip.getId());
                    trip.setCreationDate(DBTrip.getCreationDate());
                    trip.setRemainingSlots(DBTrip.getRemainingSlots());

                    //Mirar se trata de modificar una excursion a la que le faltan menos de 72h para comenzar
                    if(DBTrip.getStartDate().minusHours(MIN_MOD_START_HOURS_DIFF).isBefore(LocalDateTime.now()))
                        throw new TripModifyTimeExpiredException();

                    //Comprobar que no se adelanta la fecha de inicio del trip
                    if(trip.getStartDate().isBefore(DBTrip.getStartDate()))
                        throw new TripStartDateEarlierException();

                    //Comprobar que el numero de plazas modificado no  sea menor que el numero de plazas reservadas
                    if(trip.getMaxSlots() < (DBTrip.getMaxSlots() - DBTrip.getRemainingSlots()))
                        throw new TripSlotsReducedException();
                    trip.setRemainingSlots(trip.getRemainingSlots() + (trip.getMaxSlots() - DBTrip.getMaxSlots()));
                    tripDao.update(connection,trip);
                    connection.commit();

                } catch(InstanceNotFoundException | TripStartDateEarlierException | TripSlotsReducedException | TripModifyTimeExpiredException e){
                    connection.commit();
                    throw e;
                } catch(SQLException e){
                    connection.rollback();
                    throw new RuntimeException(e);
                } catch (RuntimeException | Error e){
                    connection.rollback();
                    throw e;
                }
            } catch(SQLException e){
                throw new RuntimeException(e);
            }


    }

    @Override
    public List<Trip> findTripByCity(String city, LocalDateTime startDate, LocalDateTime endDate){
        try(Connection connection = dataSource.getConnection()){
            List<Trip> foundTripList = tripDao.findByCity(connection,city,startDate,endDate);
            LocalDateTime limit = LocalDateTime.now().plusHours(24).withNano(0);
            //Devolver solo las excusiones que puedan ser reservadas
            foundTripList.removeIf(trip -> trip.getStartDate().compareTo(limit) < 0);
            return foundTripList;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Long bookTrip(Long tripId, String email, int size, String creditCardNumber) throws InputValidationException,
            InstanceNotFoundException, TripBookingTooLateException, TripNoRemainingSlotsException {

        PropertyValidator.validateMandatoryString("email",email);
        //Comprobar formato del email
        validateEmail(email);
        PropertyValidator.validateCreditCard(creditCardNumber);

        if (size < MIN_BOOKING_SIZE || size > MAX_BOOKING_SIZE){
            throw new InputValidationException("Invalid Size " +
                    "value (it must be between "+MIN_BOOKING_SIZE+" AND "+MAX_BOOKING_SIZE+")");
        }
        try(Connection connection = dataSource.getConnection()){

            try{

                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                Trip trip = tripDao.find(connection, tripId);

                //Si ya ha pasado el plazo de reserva, Si startDate-24h es <= now.
                if(!trip.getStartDate().minusHours(BOOKING_TIME_LIMIT).isAfter(LocalDateTime.now())){
                    throw new TripBookingTooLateException();
                }
                //Si no hay suficientes plazas disponibles
                if(trip.getRemainingSlots()<size){
                    throw new TripNoRemainingSlotsException();
                }

                //Restar a las plazas restantes las que se van a reservar
                trip.setRemainingSlots(trip.getRemainingSlots()-size);
                tripDao.update(connection,trip);

                Booking booking = new Booking(tripId,email,creditCardNumber,size,trip.getPrice(),LocalDateTime.now());
                Booking createdBooking = bookingDao.create(connection,booking);

                connection.commit();

                return createdBooking.getId();

            } catch(InstanceNotFoundException | TripBookingTooLateException | TripNoRemainingSlotsException e){
                connection.commit();
                throw e;
            } catch(SQLException e){
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e){
                connection.rollback();
                throw e;
            }
        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void cancelBooking(Long bookingID, String email) throws InputValidationException, InstanceNotFoundException, BookingCancellationTooLateException, BookingAlreadyCancelledException, BookingCancellationNotAllowedException {
        PropertyValidator.validateMandatoryString("email",email);
        validateEmail(email);

        try (Connection connection = dataSource.getConnection()) {
            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                Booking booking = bookingDao.find(connection, bookingID);

                // Check first if the user can change this booking
                if ( !booking.getEmail().equals(email) )
                    throw new BookingCancellationNotAllowedException();

                if ( booking.getCancellationDate() != null )
                    throw new BookingAlreadyCancelledException();

                Trip trip = tripDao.find(connection, booking.getTripId());

                if ( trip.getStartDate().isBefore(LocalDateTime.now().plusHours(CANCELLATION_TIME_LIMIT)) )
                    throw new BookingCancellationTooLateException();

                booking.setCancellationDate(LocalDateTime.now());
                bookingDao.update(connection, booking);

                trip.setRemainingSlots(trip.getRemainingSlots() + booking.getSize());
                tripDao.update(connection, trip);

                connection.commit();

            } catch(InstanceNotFoundException | BookingCancellationTooLateException | BookingCancellationNotAllowedException | BookingAlreadyCancelledException e){
                connection.commit();
                throw e;
            } catch(SQLException e){
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e){
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Booking> getBookings(String email) throws InputValidationException {
        PropertyValidator.validateMandatoryString("email",email);
        validateEmail(email);

        try(Connection connection = dataSource.getConnection()){
            return bookingDao.findByEmail(connection,email);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
