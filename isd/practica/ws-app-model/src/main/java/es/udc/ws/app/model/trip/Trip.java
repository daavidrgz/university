package es.udc.ws.app.model.trip;
import java.time.LocalDateTime;
import java.util.Objects;

public class Trip {
    private Long id;
    private String city;
    private String description;
    private LocalDateTime startDate;
    private double price;
    private int maxSlots;
    private Integer remainingSlots;
    private LocalDateTime creationDate;

    public Trip(String city, String description, LocalDateTime startDate, double price, int maxSlots) {
        this.city = city;
        this.description = description;
        this.startDate = (startDate != null) ? startDate.withNano(0) : null;
        this.price = price;
        this.maxSlots = maxSlots;
    }

    public Trip(Long id, String city, String description, LocalDateTime startDate, double price,
                int maxSlots, Integer remainingSlots, LocalDateTime creationDate) {
        this(city, description, startDate, price, maxSlots);
        this.id = id;
        this.remainingSlots = remainingSlots;
        this.creationDate = (creationDate != null) ? creationDate.withNano(0) : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = (startDate != null) ? startDate.withNano(0) : null;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getMaxSlots() {
        return maxSlots;
    }

    public void setMaxSlots(int maxSlots) {
        this.maxSlots = maxSlots;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = (creationDate != null) ? creationDate.withNano(0) : null;
    }

    public Integer getRemainingSlots() {
        return remainingSlots;
    }

    public void setRemainingSlots(Integer remainingSlots) {
        this.remainingSlots = remainingSlots;
    }

    @Override
    public boolean equals(Object o) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;
        Trip trip = (Trip) o;
        return Double.compare(trip.price, price) == 0
                && maxSlots == trip.maxSlots
                && Objects.equals(remainingSlots,trip.remainingSlots)
                && Objects.equals(id, trip.id)
                && Objects.equals(city, trip.city)
                && Objects.equals(description, trip.description)
                && Objects.equals(startDate, trip.startDate)
                && Objects.equals(creationDate, trip.creationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, city, description, startDate, price, maxSlots, remainingSlots, creationDate);
    }
}
