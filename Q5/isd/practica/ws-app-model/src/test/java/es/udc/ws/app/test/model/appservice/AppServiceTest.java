package es.udc.ws.app.test.model.appservice;

import es.udc.ws.app.model.booking.Booking;
import es.udc.ws.app.model.booking.SqlBookingDao;
import es.udc.ws.app.model.booking.SqlBookingDaoFactory;
import es.udc.ws.app.model.trip.SqlTripDao;
import es.udc.ws.app.model.trip.SqlTripDaoFactory;
import es.udc.ws.app.model.trip.Trip;
import es.udc.ws.app.model.tripservice.TripService;
import es.udc.ws.app.model.tripservice.TripServiceFactory;
import es.udc.ws.app.model.tripservice.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.sql.SimpleDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import static es.udc.ws.app.model.util.ModelConstants.*;
import static org.junit.jupiter.api.Assertions.*;

public class AppServiceTest {
    private static TripService tripService = null;
    private static SqlTripDao tripDao = null;
    private static SqlBookingDao bookingDao = null;
    private static final String VALID_CREDIT_CARD = "1234567891234567";
    private static final String INVALID_CREDIT_CARD = "1";
    private static final String VALID_EMAIL = "test@test.com";
    private static final String VALID_EMAIL_ALT = "isd@isd.com";
    private static final String INVALID_EMAIL = "a";
    private static final long INVALID_ID = -1;
    @BeforeAll
    private static void init(){
        DataSource dataSource = new SimpleDataSource();

        DataSourceLocator.addDataSource(APP_DATA_SOURCE,dataSource);
        tripService = TripServiceFactory.getService();
        tripDao = SqlTripDaoFactory.getDao();
        bookingDao = SqlBookingDaoFactory.getDao();
    }

    private Trip getValidTrip(String city){
        // Añadir 1 día más del límite de creación del trip
        LocalDateTime startDate = LocalDateTime.now().plusHours(MIN_START_CREATION_HOURS_DIFF).plusDays(1);
        return new Trip(city,"Trip description",startDate,10.0,100);
    }
    private Trip getValidTrip(){
        return getValidTrip("City Name");
    }

    private Trip createTrip(Trip trip){
        
        Trip addedTrip = null;
        try {
            addedTrip = tripService.addTrip(trip);
        } catch(InputValidationException e){
            throw new RuntimeException(e);
        }
        return addedTrip;
    }

    private Booking createBooking(Long tripId) {
        try{
            Long bookingId = tripService.bookTrip(tripId,VALID_EMAIL,MIN_BOOKING_SIZE,VALID_CREDIT_CARD);
            return findBooking(bookingId);
        }catch(InstanceNotFoundException | TripBookingTooLateException | TripNoRemainingSlotsException|InputValidationException e){
            throw new RuntimeException(e);
        }

    }
    
    private void removeTrip(Long tripId) {
        DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);

        try (Connection connection = dataSource.getConnection()) {
            try{
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                tripDao.remove(connection, tripId);

                connection.commit();
            } catch (InstanceNotFoundException e){
                connection.commit();
                throw new RuntimeException(e);
            } catch (SQLException e){
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e){
                connection.rollback();
                throw e;
            }

        }catch ( SQLException e){
            throw new RuntimeException(e);
        }
    }

    private Trip findTrip(Long tripId){
        DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);

        try (Connection connection = dataSource.getConnection()) {
            try{
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                Trip foundTrip = tripDao.find(connection, tripId);

                connection.commit();
                return foundTrip;
            } catch (InstanceNotFoundException e){
                connection.commit();
                throw new RuntimeException(e);
            } catch (SQLException e){
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e){
                connection.rollback();
                throw e;
            }

        }catch ( SQLException e){
            throw new RuntimeException(e);
        }
    }

    private void updateTrip(Trip trip){
        DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);

        try (Connection connection = dataSource.getConnection()) {
            try{
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                tripDao.update(connection, trip);

                connection.commit();
            } catch (InstanceNotFoundException e){
                connection.commit();
                throw new RuntimeException(e);
            } catch (SQLException e){
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e){
                connection.rollback();
                throw e;
            }

        }catch ( SQLException e){
            throw new RuntimeException(e);
        }
    }
    private void updateBooking(Booking booking){
        DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);

        try (Connection connection = dataSource.getConnection()) {
            try{
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                bookingDao.update(connection, booking);

                connection.commit();
            } catch (InstanceNotFoundException e){
                connection.commit();
                throw new RuntimeException(e);
            } catch (SQLException e){
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e){
                connection.rollback();
                throw e;
            }

        }catch ( SQLException e){
            throw new RuntimeException(e);
        }
    }

    private Booking findBooking(Long bookId){
        DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);

        try (Connection connection = dataSource.getConnection()) {
            try{
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                Booking foundBook = bookingDao.find(connection, bookId);

                connection.commit();
                return foundBook;
            } catch (InstanceNotFoundException e){
                connection.commit();
                throw new RuntimeException(e);
            } catch (SQLException e){
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e){
                connection.rollback();
                throw e;
            }

        }catch ( SQLException e){
            throw new RuntimeException(e);
        }
    }

    private void removeBooking(Long BookingId) {
        DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);

        try (Connection connection = dataSource.getConnection()) {
            try{
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                bookingDao.remove(connection, BookingId);

                connection.commit();
            } catch (InstanceNotFoundException e){
                connection.commit();
                throw new RuntimeException(e);
            } catch (SQLException e){
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e){
                connection.rollback();
                throw e;
            }

        }catch ( SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testAddTripAndFindTrip() throws InputValidationException{
        Trip trip = getValidTrip();
        Trip addedTrip = null;
        try{
            LocalDateTime beforeCreationDate = LocalDateTime.now().withNano(0);
            addedTrip = tripService.addTrip(trip);
            LocalDateTime afterCreationDate = LocalDateTime.now().withNano(0);
            
            Trip foundTrip = findTrip(addedTrip.getId());

            assertEquals(addedTrip,foundTrip);
            assertEquals(foundTrip.getCity(),trip.getCity());
            assertEquals(foundTrip.getDescription(),trip.getDescription());
            assertEquals(foundTrip.getStartDate(),trip.getStartDate());
            assertEquals(foundTrip.getPrice(),trip.getPrice());
            assertEquals(foundTrip.getMaxSlots(),trip.getMaxSlots());
            assertEquals(foundTrip.getRemainingSlots(),trip.getRemainingSlots());

            assertTrue((foundTrip.getCreationDate().compareTo(beforeCreationDate) >= 0)
                    && (foundTrip.getCreationDate().compareTo(afterCreationDate) <= 0));

        } finally{
            assert addedTrip != null;
            removeTrip(addedTrip.getId());
        }
    }

    @Test
    public void testFindByCity(){

        List<Trip> tripList = new LinkedList<>();
        List<Trip> betweenDatesTripList;
        Trip trip = createTrip(getValidTrip());
        tripList.add(trip);
        Trip trip2 = createTrip(getValidTrip());
        tripList.add(trip2);
        //añadir  trip fuera del rango de fechas de búsqueda
        Trip trip3 = getValidTrip();
        trip3.setStartDate(trip.getStartDate().plusDays(2));
        trip3 = createTrip(trip3);
        tripList.add(trip3);
        
        betweenDatesTripList = new LinkedList<>(tripList);
        betweenDatesTripList.remove(trip3);

        try{
            String tripCity = trip.getCity();
            LocalDateTime startDate = trip.getStartDate().minusDays(1);
            LocalDateTime endDate = trip.getStartDate().plusDays(1);

            List <Trip> foundTripList = tripService.findTripByCity(tripCity,startDate,endDate);
            assertEquals(betweenDatesTripList,foundTripList);

            //Comparar si se puede reservar
            foundTripList.forEach( tripItem ->
                assertTrue(tripItem.getStartDate().compareTo(LocalDateTime.now().plusHours(24)) >= 0)
            );

            List <Trip> allTrips = tripService.findTripByCity(tripCity,null,null);
            assertEquals(tripList,allTrips);

            List <Trip> emptyTripList = tripService.findTripByCity("city2",null,null);
            assertEquals(0,emptyTripList.size());

        } finally {
            tripList.forEach(tripItem -> removeTrip(tripItem.getId()));
        }
    }
    @Test
    public void testAddInvalidTrip() {
        //Check trip city not null
        assertThrows(InputValidationException.class, () -> {
            Trip trip = getValidTrip();
            trip.setCity(null);
            Trip addedTrip = tripService.addTrip(trip);
            removeTrip(addedTrip.getId());
        });
        //Check trip city not empty
        assertThrows(InputValidationException.class, () -> {
            Trip trip = getValidTrip();
            trip.setCity("");
            Trip addedTrip = tripService.addTrip(trip);
            removeTrip(addedTrip.getId());
        });
        //Check trip description not null
        assertThrows(InputValidationException.class, () -> {
            Trip trip = getValidTrip();
            trip.setDescription(null);
            Trip addedTrip = tripService.addTrip(trip);
            removeTrip(addedTrip.getId());
        });
        //Check trip description not empty
        assertThrows(InputValidationException.class, () -> {
            Trip trip = getValidTrip();
            trip.setDescription("");
            Trip addedTrip = tripService.addTrip(trip);
            removeTrip(addedTrip.getId());
        });
        //Check trip price >=0
        assertThrows(InputValidationException.class, () -> {
            Trip trip = getValidTrip();
            trip.setPrice(-1);
            Trip addedTrip = tripService.addTrip(trip);
            removeTrip(addedTrip.getId());
        });
        //Check trip maxSlots >=0
        assertThrows(InputValidationException.class, () -> {
            Trip trip = getValidTrip();
            trip.setMaxSlots(-1);
            Trip addedTrip = tripService.addTrip(trip);
            removeTrip(addedTrip.getId());
        });
        //Check trip startDate is not null
        assertThrows(InputValidationException.class, () -> {
            Trip trip = getValidTrip();
            trip.setStartDate(null);
            Trip addedTrip = tripService.addTrip(trip);
            removeTrip(addedTrip.getId());
        });
        //Check trip startDate is 72h after creation
        assertThrows(InputValidationException.class, () -> {
            Trip trip = getValidTrip();
            trip.setStartDate(LocalDateTime.now().plusHours(MIN_START_CREATION_HOURS_DIFF-1));
            Trip addedTrip = tripService.addTrip(trip);
            removeTrip(addedTrip.getId());
        });
    }


    @Test
    public void testUpdateTrip() throws TripStartDateEarlierException, InstanceNotFoundException, InputValidationException, TripModifyTimeExpiredException, TripSlotsReducedException {
        Trip trip = createTrip(getValidTrip());
        try {
            trip.setCity("Updated city");
            trip.setMaxSlots(trip.getMaxSlots() + 50);
            tripService.updateTrip(trip);

            // Retrieve updated Trip to check
            Trip updatedTrip = findTrip(trip.getId());
            assertEquals(updatedTrip, trip);

            // Invalid update values
            assertThrows(InputValidationException.class, () -> {
                trip.setCity("");
                tripService.updateTrip(trip);
            });
            assertThrows(InputValidationException.class, () -> {
                trip.setDescription(null);
                tripService.updateTrip(trip);
            });
            assertThrows(InputValidationException.class, () -> {
                trip.setMaxSlots(-1);
                tripService.updateTrip(trip);
            });

            // Instance not found changing tripId
            assertThrows(InstanceNotFoundException.class, () -> {
                Trip trip1 = createTrip(getValidTrip());
                removeTrip(trip1.getId());

                // There is no trip1 in the database
                trip1.setCity("Deleted trip");
                tripService.updateTrip(trip1);
            });

            // Change start date
            assertThrows(TripStartDateEarlierException.class, () -> {
                Trip trip2 = createTrip(getValidTrip());
                try {
                    trip2.setStartDate(trip2.getStartDate().minusHours(1));
                    tripService.updateTrip(trip2);
                } finally {
                    removeTrip(trip2.getId());
                }
            });

            // Reduce maxSlots below current reserved slots
            assertThrows(TripSlotsReducedException.class, () -> {
                Trip trip3 = createTrip(getValidTrip());
                // Reservar todos los slots
                int BOOKED_SLOTS = trip3.getMaxSlots();
                try {
                    trip3.setRemainingSlots(trip3.getMaxSlots() - BOOKED_SLOTS);
                    updateTrip(trip3);

                    // Quitar un slot reservado
                    trip3.setMaxSlots(trip3.getMaxSlots() - 1);
                    tripService.updateTrip(trip3);
                } finally {
                    removeTrip(trip3.getId());
                }
            });

            // Modify trip 72h hours before start
            assertThrows(TripModifyTimeExpiredException.class, () -> {
                Trip trip4 = createTrip(getValidTrip());
                try {
                    trip4.setStartDate(LocalDateTime.now().plusHours(MIN_MOD_START_HOURS_DIFF - 1));
                    updateTrip(trip4);

                    trip4.setDescription("Too late to change");
                    tripService.updateTrip(trip4);
                } finally {
                    removeTrip(trip4.getId());
                }
            });

        } finally {
            removeTrip(trip.getId());
        }
    }

    @Test
    public void testBookTrip() throws InstanceNotFoundException, TripBookingTooLateException, InputValidationException, TripNoRemainingSlotsException {

        Trip trip = createTrip(getValidTrip());
        try {
            Trip trip1 = createTrip(getValidTrip());

            int remainingSlotsBeforeBooking = trip1.getRemainingSlots();

            Long bookingId = tripService.bookTrip(trip1.getId(),VALID_EMAIL,MAX_BOOKING_SIZE,VALID_CREDIT_CARD);

            Booking testBooking = findBooking(bookingId);

            trip1 = findTrip(trip1.getId());

            assertEquals((remainingSlotsBeforeBooking - MAX_BOOKING_SIZE), trip1.getRemainingSlots());
            assertEquals(VALID_EMAIL,testBooking.getEmail());
            assertEquals(VALID_CREDIT_CARD,testBooking.getCreditCardNumber());
            assertEquals(MAX_BOOKING_SIZE,testBooking.getSize());
            assertEquals(trip1.getId(),testBooking.getTripId());
            assertEquals(trip1.getPrice(),testBooking.getBookingPrice());
            assertNull(testBooking.getCancellationDate());
            removeBooking(bookingId);
            removeTrip(trip1.getId());

            //Probar que salte excepcion al poner una cc invalida
            assertThrows(InputValidationException.class,() -> {
                Long test = tripService.bookTrip(trip.getId(),VALID_EMAIL,MAX_BOOKING_SIZE,INVALID_CREDIT_CARD);
                removeBooking(test);
            });

            //Probar formato de email y que salte excepcion
            assertThrows(InputValidationException.class,() -> {
                Long test = tripService.bookTrip(trip.getId(),INVALID_EMAIL,MAX_BOOKING_SIZE,VALID_CREDIT_CARD);
                removeBooking(test);
            });

            //Probar que la reserva se hace con un tamaño valido
            assertThrows(InputValidationException.class, () -> {
                Long test = tripService.bookTrip(trip.getId(), VALID_EMAIL, MIN_BOOKING_SIZE-1, VALID_CREDIT_CARD);
                removeBooking(test);
            });

            //Probar que la reserva se hace con un tamaño valido
            assertThrows(InputValidationException.class, () -> {
                Long test = tripService.bookTrip(trip.getId(), VALID_EMAIL, MAX_BOOKING_SIZE+1, VALID_CREDIT_CARD);
                removeBooking(test);
            });

            //Probar que si hacemos una reserva fuera de plazo devuelva una excepcion
            assertThrows(TripBookingTooLateException.class, () -> {
               Trip trip2 = createTrip(getValidTrip());
               try {
                   trip2.setStartDate(LocalDateTime.now().plusHours(BOOKING_TIME_LIMIT-1));
                   updateTrip(trip2);
                   Long test = tripService.bookTrip(trip2.getId(), VALID_EMAIL, MAX_BOOKING_SIZE, VALID_CREDIT_CARD);
                   removeBooking(test);

               }finally {
                   removeTrip(trip2.getId());
               }
            });

            //Probar si intetamos hacer una reserva donde no hay plazas suficientes
            assertThrows(TripNoRemainingSlotsException.class, () ->{
                Trip trip3 = createTrip(getValidTrip());
                try{
                    trip3.setRemainingSlots(1);
                    updateTrip(trip3);
                    Long test = tripService.bookTrip(trip3.getId(),VALID_EMAIL,MAX_BOOKING_SIZE,VALID_CREDIT_CARD);
                    removeBooking(test);
                }finally {
                    removeTrip(trip3.getId());
                }
            });
        }finally {
            removeTrip(trip.getId());
        }
    }


    @Test
    public void testCancelBooking() throws InputValidationException, InstanceNotFoundException, BookingCancellationTooLateException, BookingAlreadyCancelledException, BookingCancellationNotAllowedException {
        Trip trip = createTrip(getValidTrip());
        // Make sure the start date is after the cancellation date limit (48h)
        trip.setStartDate(LocalDateTime.now().plusHours(CANCELLATION_TIME_LIMIT + 1));
        updateTrip(trip);
        Booking booking = createBooking(trip.getId());
        // Trip with updated bookings
        trip = findTrip(trip.getId());

        try {
            tripService.cancelBooking(booking.getId(), booking.getEmail());

            Booking cancelledBooking = findBooking(booking.getId());
            Trip updatedTrip = findTrip(trip.getId());

            // Checking that the cancellationDate was set
            assertNotEquals(cancelledBooking.getCancellationDate(), null);
            // Checking that the remainingSlots in the trip were incremented
            assertEquals(updatedTrip.getRemainingSlots(), trip.getRemainingSlots() + booking.getSize());

            assertThrows(InputValidationException.class, () -> {
                Trip trip1 = createTrip(getValidTrip());
                Booking booking1 = createBooking(trip1.getId());
                try {
                    tripService.cancelBooking(booking.getId(), INVALID_EMAIL);
                } finally {
                    removeBooking(booking1.getId());
                    removeTrip(trip1.getId());
                }
            });

            assertThrows(InstanceNotFoundException.class, () -> {
                Trip trip2 = createTrip(getValidTrip());
                Booking booking2 = createBooking(trip2.getId());
                try {
                    tripService.cancelBooking(INVALID_ID, booking2.getEmail());
                } finally {
                    removeBooking(booking2.getId());
                    removeTrip(trip2.getId());
                }
            });
            assertThrows(InstanceNotFoundException.class, () -> {
               Trip trip3 = createTrip(getValidTrip());
               Booking booking3 = createBooking(trip3.getId());

               removeTrip(trip3.getId());
               tripService.cancelBooking(booking3.getId(), booking3.getEmail());
               removeBooking(booking3.getId());
            });

            assertThrows(BookingCancellationTooLateException.class, () -> {
               Trip trip4 = createTrip(getValidTrip());
               Booking booking4 = createBooking(trip4.getId());
               trip4 = findTrip(trip4.getId());
               trip4.setStartDate(LocalDateTime.now().plusHours(CANCELLATION_TIME_LIMIT - 1));
               updateTrip(trip4);
               try {
                   tripService.cancelBooking(booking4.getId(), booking4.getEmail());
               } finally {
                   removeBooking(booking4.getId());
                   removeTrip(trip4.getId());
               }
            });

            assertThrows(BookingAlreadyCancelledException.class, () -> {
                Trip trip5 = createTrip(getValidTrip());
                Booking booking5 = createBooking(trip5.getId());
                trip5 = findTrip(trip5.getId());
                trip5.setStartDate(LocalDateTime.now().plusHours(CANCELLATION_TIME_LIMIT + 1));
                updateTrip(trip5);
                try {
                    tripService.cancelBooking(booking5.getId(), booking5.getEmail());
                    tripService.cancelBooking(booking5.getId(), booking5.getEmail());
                } finally {
                    removeBooking(booking5.getId());
                    removeTrip(trip5.getId());
                }
            });

            assertThrows(BookingCancellationNotAllowedException.class, () -> {
                Trip trip6 = createTrip(getValidTrip());
                Booking booking6 = createBooking(trip6.getId());
                trip6 = findTrip(trip6.getId());
                trip6.setStartDate(LocalDateTime.now().plusHours(CANCELLATION_TIME_LIMIT + 1));
                updateTrip(trip6);
                try {
                    tripService.cancelBooking(booking6.getId(), VALID_EMAIL_ALT);
                } finally {
                    removeBooking(booking6.getId());
                    removeTrip(trip6.getId());
                }
            });

        } finally {
            removeBooking(booking.getId());
            removeTrip(trip.getId());
        }
    }

    @Test
    public void testGetBookings() throws InputValidationException{
        List<Booking> bookings = new LinkedList<>();
        Trip trip = createTrip(getValidTrip());

        Booking booking1 = createBooking(trip.getId());
        bookings.add(booking1);

        Booking booking2 = createBooking(trip.getId());
        //Disminuír la fecha del booking para comprobar que se devuelven ordenadas
        booking2.setBookingDate(booking2.getBookingDate().minusHours(1));
        updateBooking(booking2);
        bookings.add(booking2);

        Booking booking3 = createBooking(trip.getId());
        //Disminuír la fecha del booking para comprobar que se devuelven ordenadas
        booking3.setBookingDate(booking3.getBookingDate().minusHours(2));
        updateBooking(booking3);
        bookings.add(booking3);

        try{
            List<Booking> foundBookings = tripService.getBookings(VALID_EMAIL);
            assertEquals(bookings,foundBookings);

            //Not is before -> is equal or is after
            //Hacemos esto para comprobar el orden. Tienen que estar ordenadas de más reciente a menos, entonces,
            //el primer elemento tiene que ser posterior o igual al segundo (Not before) y viceversa

            //Usar assertTrue en vez de assertFalse, creo que se entiende mejor así
            assertTrue(!foundBookings.get(0).getBookingDate().isBefore(foundBookings.get(1).getBookingDate()));
            assertTrue(!foundBookings.get(1).getBookingDate().isBefore(foundBookings.get(2).getBookingDate()));


            //Para poner un email distinto del que usamos para crear los bookings, le concatenamos un string
            //válido al final
            List<Booking> noBookings = tripService.getBookings(VALID_EMAIL.concat(".com"));
            assertEquals(0,noBookings.size());

            //Comprobar que se lanza excepción al mandar un email inválido
            assertThrows(InputValidationException.class,()->{
                tripService.getBookings(INVALID_EMAIL);
            });

        }finally{
            bookings.forEach(booking -> removeBooking(booking.getId()));
            removeTrip(trip.getId());
        }

    }

}
