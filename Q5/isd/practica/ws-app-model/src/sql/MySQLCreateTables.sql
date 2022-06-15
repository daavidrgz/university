-- ----------------------------------------------------------------------------
-- Model
-------------------------------------------------------------------------------
DROP TABLE Booking;
DROP TABLE Trip;

CREATE TABLE Trip (
	tripId BIGINT NOT NULL AUTO_INCREMENT,
	city VARCHAR(255) NOT NULL,
	description VARCHAR(255) NOT NULL,
	startDate DATETIME NOT NULL,
	price FLOAT NOT NULL,
	maxSlots INT NOT NULL,
	remainingSlots INT NOT NULL,
	creationDate DATETIME NOT NULL,
	CONSTRAINT TripPK PRIMARY KEY (tripId),
	CONSTRAINT validTripPrice CHECK (price >= 0),
	CONSTRAINT validMaxSlots CHECK (maxSlots > 0),
	CONSTRAINT remainingSlots CHECK (remainingSlots <= maxSlots)
) ENGINE = InnoDB;

CREATE TABLE Booking (
	bookingId BIGINT NOT NULL AUTO_INCREMENT,
	tripId BIGINT NOT NULL,
	bookingDate DATETIME NOT NULL,
	email VARCHAR(255) NOT NULL,
	creditCardNumber VARCHAR(255) NOT NULL,
	size INT NOT NULL,
	bookingPrice FLOAT NOT NULL,
	cancellationDate DATETIME,

	CONSTRAINT bookingPK PRIMARY KEY (bookingId),
	CONSTRAINT validBookingPrice CHECK (bookingPrice >= 0),
	CONSTRAINT validSize CHECK (size > 0),
	CONSTRAINT validCancellation CHECK (cancellationDate >=bookingDate),
	CONSTRAINT BookingTripIdFK FOREIGN KEY(tripId)
	    REFERENCES Trip(tripId) ON DELETE CASCADE
) ENGINE = InnoDB;
