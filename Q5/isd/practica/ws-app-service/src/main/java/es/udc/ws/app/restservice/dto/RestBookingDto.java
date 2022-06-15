package es.udc.ws.app.restservice.dto;

import java.time.LocalDateTime;

public class RestBookingDto {
    private Long id;
    private Long tripId;
    private LocalDateTime bookingDate;
    private String email;
    private String lastCCDigits;
    private int size;
    private double bookingPrice;
    private LocalDateTime cancellationDate;

    public RestBookingDto(Long id,Long tripId, String email, String lastCCDigits, int size, double bookingPrice,
                   LocalDateTime bookingDate,LocalDateTime cancellationDate){

        this.id = id;
        this.tripId = tripId;
        this.email = email;
        this.lastCCDigits = lastCCDigits;
        this.size = size;
        this.bookingPrice = bookingPrice;
        this.bookingDate = (bookingDate != null) ? bookingDate.withNano(0) : null;
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
        this.bookingDate = bookingDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastCCDigits() {
        return lastCCDigits;
    }

    public void setLastCCDigits(String lastCCDigits) {
        this.lastCCDigits = lastCCDigits;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public double getBookingPrice() {
        return bookingPrice;
    }

    public void setBookingPrice(double bookingPrice) {
        this.bookingPrice = bookingPrice;
    }

    public LocalDateTime getCancellationDate() {
        return cancellationDate;
    }

    public void setCancellationDate(LocalDateTime cancellationDate) {
        this.cancellationDate = cancellationDate;
    }

    @Override
    public String toString() {
        return "RestBookingDto{" +
                "id=" + id +
                ", tripId=" + tripId +
                ", bookingDate=" + bookingDate +
                ", email='" + email + '\'' +
                ", lastCCDigits='" + lastCCDigits + '\'' +
                ", size=" + size +
                ", bookingPrice=" + bookingPrice +
                ", cancellationDate=" + cancellationDate +
                '}';
    }
}
