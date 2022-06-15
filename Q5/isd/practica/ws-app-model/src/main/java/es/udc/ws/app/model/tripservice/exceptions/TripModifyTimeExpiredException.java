package es.udc.ws.app.model.tripservice.exceptions;

import static es.udc.ws.app.model.util.ModelConstants.MIN_MOD_START_HOURS_DIFF;

public class TripModifyTimeExpiredException extends Exception {
    public TripModifyTimeExpiredException() {
        super("Cannot update trip, remaining time to start the trip is less than " + MIN_MOD_START_HOURS_DIFF + "h");
    }
}
