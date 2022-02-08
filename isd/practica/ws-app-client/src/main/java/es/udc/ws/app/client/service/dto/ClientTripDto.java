package es.udc.ws.app.client.service.dto;

import java.time.LocalDateTime;

public class ClientTripDto {
    private Long id;
    private String city;
    private String description;
    private LocalDateTime startDate;
    private double price;
    private int maxSlots;
    private Integer bookedSlots;

    public ClientTripDto(Long id, String city, String description, LocalDateTime startDate, double price,
                       int maxSlots, Integer bookedSlots) {

        this.id = id;
        this.city = city;
        this.description = description;
        this.startDate = (startDate != null) ? startDate.withNano(0) : null;
        this.price = price;
        this.maxSlots = maxSlots;
        this.bookedSlots = bookedSlots;
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

    public Integer getBookedSlots() {
        return bookedSlots;
    }

    public void setBookedSlots(Integer bookedSlots) {
        this.bookedSlots = bookedSlots;
    }

    @Override
    public String toString() {
        return "ClientTripDto{" +
                "id=" + id +
                ", city='" + city + '\'' +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", price=" + price +
                ", maxSlots=" + maxSlots +
                ", bookedSlots=" + bookedSlots +
                '}';
    }
}
