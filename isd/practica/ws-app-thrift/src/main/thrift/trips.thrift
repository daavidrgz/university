namespace java es.udc.ws.app.thrift

// Usamos campos optional para no tener que mandar valores inválidos como -1 en el id.
// Para ver si es inválido, se utilizan los metodos isSetXXX()

struct ThriftTripDto{
	1: optional i64 id;
	2: string city;
	3: string description;
	4: string startDate;
	5: double price;
	6: i32 maxSlots;
	7: optional i32 remainingSlots;
}

struct ThriftBookingDto{
	1: i64 id;
	2: i64 tripId;
	3: string bookingDate;
	4: string email;
	5: string lastCCDigits;
	6: i32 size;
	7: double bookingPrice;
	8: string cancellationDate;
}

exception ThriftInputValidationException{
	1: string message;
}

exception ThriftInstanceNotFoundException{
	1: string instanceId
	2: string instanceType
}

exception ThriftTripModifyTimeExpiredException{
    1: string message;
}

exception ThriftTripStartDateEarlierException{
    1: string message;
}

exception ThriftTripSlotsReducedException{
    1: string message;
}

exception ThriftTripBookingTooLateException{
    1: string message;
}

exception ThriftTripNoRemainingSlotsException{
    1: string message;
}

exception ThriftBookingCancellationTooLateException{
    1: string message;
}

exception ThriftBookingAlreadyCancelledException{
    1: string message;
}

exception ThriftBookingCancellationNotAllowedException{
    1: string message;
}


service ThriftTripService{
    ThriftTripDto addTrip(1: ThriftTripDto trip) throws (1 : ThriftInputValidationException e1)

    void updateTrip(1 : ThriftTripDto trip) throws (1 : ThriftInputValidationException e1, ThriftInstanceNotFoundException e2,
        3: ThriftTripModifyTimeExpiredException e3, 4: ThriftTripStartDateEarlierException e4, 5: ThriftTripSlotsReducedException e5);

    list<ThriftTripDto> findTripByCity(1: string city, 2: string startDate, 3: string endDate)

    i64 bookTrip(1: i64 tripId, 2: string email, 3: i32 size, 4: string creditCardNumber) throws (1: ThriftInputValidationException e1,
        2: ThriftInstanceNotFoundException e2, 3: ThriftTripBookingTooLateException e3, 4: ThriftTripNoRemainingSlotsException e4)

    void cancelBooking(1: i64 bookingId, 2: string email) throws (1: ThriftInputValidationException e1, 2: ThriftInstanceNotFoundException e2,
        3: ThriftBookingCancellationTooLateException e3, 4: ThriftBookingAlreadyCancelledException e4, 5: ThriftBookingCancellationNotAllowedException e5)

    list<ThriftBookingDto> getBookings(1: string email) throws (1: ThriftInputValidationException e1)
}