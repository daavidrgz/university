package es.udc.ws.app.model.booking;
import java.time.LocalDateTime;
import java.util.Objects;


public class Booking {
    private Long id;
    private Long tripId;
    private LocalDateTime bookingDate;
    private String email;
    private String creditCardNumber;
    private int size;
    private double bookingPrice;
    private LocalDateTime cancellationDate;

    public Booking(Long tripId, String email,String creditCardNumber, int size,double bookingPrice,LocalDateTime bookingDate){
        this.tripId = tripId;
        this.email = email;
        this.creditCardNumber = creditCardNumber;
        this.size = size;
        this.bookingDate = (bookingDate != null) ? bookingDate.withNano(0) : null;
        this.bookingPrice = bookingPrice;
        this.cancellationDate = null;
    }
    public Booking(Long id,Long tripId, String email, String creditCardNumber, int size, double bookingPrice,
                   LocalDateTime bookingDate,LocalDateTime cancellationDate){

        this(tripId,email,creditCardNumber,size,bookingPrice,bookingDate);
        this.id = id;
        this.cancellationDate = (cancellationDate != null) ? cancellationDate.withNano(0) : null;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = (bookingDate != null) ? bookingDate.withNano(0) : null;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreditCardNumber(){
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber){
        this.creditCardNumber = creditCardNumber;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public LocalDateTime getCancellationDate() {
        return cancellationDate;
    }

    public void setCancellationDate(LocalDateTime cancellationDate) {
        this.cancellationDate = (cancellationDate != null) ? cancellationDate.withNano(0) : null;
    }

    public double getBookingPrice() {
        return bookingPrice;
    }

    public void setBookingPrice(double bookingPrice) {
        this.bookingPrice = bookingPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return size == booking.size
                && Double.compare(booking.bookingPrice, bookingPrice) == 0
                && Objects.equals(id, booking.id)
                && Objects.equals(tripId, booking.tripId)
                && Objects.equals(bookingDate, booking.bookingDate)
                && Objects.equals(email, booking.email)
                && Objects.equals(creditCardNumber, booking.creditCardNumber)
                && Objects.equals(cancellationDate, booking.cancellationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tripId, bookingDate, email, creditCardNumber, size,
                bookingPrice, cancellationDate);
    }
}
