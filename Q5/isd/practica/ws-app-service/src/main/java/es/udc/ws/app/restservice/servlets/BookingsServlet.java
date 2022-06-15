package es.udc.ws.app.restservice.servlets;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import es.udc.ws.app.model.booking.Booking;
import es.udc.ws.app.model.tripservice.TripServiceFactory;
import es.udc.ws.app.model.tripservice.exceptions.*;
import es.udc.ws.app.restservice.dto.BookingToRestBookingDtoConversor;
import es.udc.ws.app.restservice.dto.RestBookingDto;
import es.udc.ws.app.restservice.json.AppExceptionToJsonConversor;
import es.udc.ws.app.restservice.json.JsonToRestBookingDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.servlet.RestHttpServletTemplate;
import es.udc.ws.util.servlet.ServletUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class BookingsServlet extends RestHttpServletTemplate {
    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse res) throws
            IOException, InputValidationException, InstanceNotFoundException {
        String path = ServletUtils.normalizePath(req.getPathInfo());

        if ( path == null || path.length() == 0 )
            doBookTrip(req, res);
        else
            doCancelBooking(req, res);
    }

    private void doBookTrip(HttpServletRequest req, HttpServletResponse res) throws
            InputValidationException, IOException, InstanceNotFoundException {

        Long tripId = ServletUtils.getMandatoryParameterAsLong(req, "tripId");
        String email = ServletUtils.getMandatoryParameter(req, "email");
        String sizeStr = ServletUtils.getMandatoryParameter(req, "size");
        String creditCardNumber = ServletUtils.getMandatoryParameter(req, "creditCardNumber");

        int size;
        try {
            size = Integer.parseInt(sizeStr);
        } catch(NumberFormatException e) {
            throw new InputValidationException("Invalid size parameter format");
        }

        try {
            Long bookingId = TripServiceFactory.getService().bookTrip(tripId, email, size, creditCardNumber);

            String bookingUrl = ServletUtils.normalizePath(req.getRequestURL().toString()) + "/" + bookingId;
            Map<String, String> headers = new HashMap<>(1);
            headers.put("Location", bookingUrl);

            LongNode idNode = new LongNode(bookingId);
            ServletUtils.writeServiceResponse(res, HttpServletResponse.SC_CREATED, idNode, headers);

        } catch(TripBookingTooLateException e) {
            ServletUtils.writeServiceResponse(res, HttpServletResponse.SC_GONE,
                    AppExceptionToJsonConversor.toTripBookingTooLateException(e), null);
        } catch(TripNoRemainingSlotsException e) {
            ServletUtils.writeServiceResponse(res, HttpServletResponse.SC_NOT_FOUND,
                    AppExceptionToJsonConversor.toTripNoRemainingSlotsException(e), null);
        }
    }

    private void doCancelBooking(HttpServletRequest req, HttpServletResponse res) throws
            IOException, InputValidationException, InstanceNotFoundException {

        String path = ServletUtils.normalizePath(req.getPathInfo());
        path = path.substring(1); // Substring para ignorar la primera barra
        String[] elements = path.split("/");

        if ( elements.length != 2 || !elements[1].equals("cancel") )
            throw new InputValidationException("Invalid Request: invalid path " + path);

        long id;
        try {
            id = Long.parseLong(elements[0]);
        } catch(NumberFormatException e) {
            throw new InputValidationException("Invalid Request: invalid path " + path);
        }

        String email = ServletUtils.getMandatoryParameter(req, "email");

        try {
            TripServiceFactory.getService().cancelBooking(id, email);

            ServletUtils.writeServiceResponse(res, HttpServletResponse.SC_NO_CONTENT, null, null);

        } catch(BookingCancellationTooLateException e) {
            ServletUtils.writeServiceResponse(res, HttpServletResponse.SC_GONE,
                    AppExceptionToJsonConversor.toBookingCancellationTooLateException(e), null);
        } catch(BookingAlreadyCancelledException e) {
            ServletUtils.writeServiceResponse(res, HttpServletResponse.SC_FORBIDDEN,
                    AppExceptionToJsonConversor.toBookingAlreadyCancelledException(e), null);
        } catch(BookingCancellationNotAllowedException e) {
            ServletUtils.writeServiceResponse(res, HttpServletResponse.SC_FORBIDDEN,
                    AppExceptionToJsonConversor.toBookingCancellationNotAllowedException(e), null);
        }
    }


    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse res) throws
            IOException, InputValidationException{
        ServletUtils.checkEmptyPath(req);
        String email = ServletUtils.getMandatoryParameter(req, "email");

        List<Booking> bookings = TripServiceFactory.getService().getBookings(email);

        List<RestBookingDto> bookingDtos = BookingToRestBookingDtoConversor.toRestBookingDtos(bookings);

        ServletUtils.writeServiceResponse(res,HttpServletResponse.SC_OK,
                JsonToRestBookingDtoConversor.toArrayNode(bookingDtos),null);

    }
}
