# REST
## Recursos Coleccion

- trips -> Representa a las excursiones
- bookings -> representa a las reservas

## Métodos

- addTrip -> POST /trips, body data => trip json
- updateTrip -> PUT /trips/{id}, body data => trip json
- findtripByCity -> GET /trips?city={City}&startDate={year-month-day}&endDate={year-month-day}
- bookTrip -> POST /bookings,  body parameters => tripId, email,size, creditCardNumber
- cancelBooking -> POST /bookings/{id}/cancel, body parameters => email
- getBookings -> GET /bookings?email={email}


