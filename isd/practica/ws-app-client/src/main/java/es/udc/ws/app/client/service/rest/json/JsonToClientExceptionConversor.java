package es.udc.ws.app.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import es.udc.ws.app.client.service.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;

public class JsonToClientExceptionConversor {
    public static Exception fromBadRequestErrorCode(InputStream ex) throws ParsingException {
        try{
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if ( rootNode.getNodeType() != JsonNodeType.OBJECT ) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                switch ( errorType  ) {
                    case "InputValidation":
                        return toInputValidationException(rootNode);
                    case "TripStartDateEarlier":
                        return toTripStartDateEarlierException(rootNode);
                    case "TripSlotsReduced":
                        return toTripSlotsReducedException(rootNode);
                    default:
                        throw new ParsingException("Unrecognized error type: " + errorType);
                }
            }
        }catch (ParsingException e) {
            throw e;
        }catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static Exception fromGoneErrorCode(InputStream ex) throws ParsingException {
        try{
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if ( rootNode.getNodeType() != JsonNodeType.OBJECT ) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                switch (errorType) {
                    case "TripModifyTimeExpired":
                        return toTripModifyTimeExpiredException(rootNode);
                    case "BookingCancellationTooLate":
                        return toBookingCancellationTooLateException(rootNode);
                    case "TripBookingTooLate":
                        return toTripBookingCancellationTooLateException(rootNode);
                    default:
                        throw new ParsingException("Unrecognized error type: " + errorType);
                }
            }
        }catch (ParsingException e) {
            throw e;
        }catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static Exception fromForbiddenErrorCode(InputStream ex) throws ParsingException {
        try{
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if ( rootNode.getNodeType() != JsonNodeType.OBJECT ) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                if ( errorType.equals("BookingAlreadyCancelled") )
                    return toBookingAlreadyCancelledException(rootNode);
                else if ( errorType.equals("BookingCancellationNotAllowed") )
                    return toBookingCancellationNotAllowedException(rootNode);
                else
                    throw new ParsingException("Unrecognized error type: " + errorType);
            }
        }catch (ParsingException e) {
            throw e;
        }catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static Exception fromNotFoundErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                if ( errorType.equals("InstanceNotFound") )
                    return toInstanceNotFoundException(rootNode);
                else if(errorType.equals("TripNoRemainingSlots"))
                    return toTripNoRemainingSlotsException(rootNode);
                else
                    throw new ParsingException("Unrecognized error type: " + errorType);
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static InputValidationException toInputValidationException(JsonNode rootNode ){
        String message = rootNode.get("message").textValue();
        return new InputValidationException(message);
    }
    private static ClientTripStartDateEarlierException toTripStartDateEarlierException(JsonNode rootNode) {
        String message = rootNode.get("message").textValue();
        return new ClientTripStartDateEarlierException(message);
    }
    private static ClientTripSlotsReducedException toTripSlotsReducedException(JsonNode rootNode) {
        String message = rootNode.get("message").textValue();
        return new ClientTripSlotsReducedException(message);
    }
    private static ClientTripBookingTooLateException toTripBookingCancellationTooLateException(JsonNode rootNode) {
        String message = rootNode.get("message").textValue();
        return new ClientTripBookingTooLateException(message);
    }
    private static ClientTripNoRemainingSlotsException toTripNoRemainingSlotsException(JsonNode rootNode) {
        String message = rootNode.get("message").textValue();
        return new ClientTripNoRemainingSlotsException(message);
    }
    private static ClientBookingCancellationTooLateException toBookingCancellationTooLateException(JsonNode rootNode) {
        String message = rootNode.get("message").textValue();
        return new ClientBookingCancellationTooLateException(message);
    }

    private static ClientTripModifyTimeExpiredException toTripModifyTimeExpiredException(JsonNode rootNode) {
        String message = rootNode.get("message").textValue();
        return new ClientTripModifyTimeExpiredException(message);
    }
    private static ClientBookingAlreadyCancelledException toBookingAlreadyCancelledException(JsonNode rootNode) {
        String message = rootNode.get("message").textValue();
        return new ClientBookingAlreadyCancelledException(message);
    }
    private static ClientBookingCancellationNotAllowedException toBookingCancellationNotAllowedException(JsonNode rootNode) {
        String message = rootNode.get("message").textValue();
        return new ClientBookingCancellationNotAllowedException(message);
    }

    private static InstanceNotFoundException toInstanceNotFoundException(JsonNode rootNode) {
        String instanceId = rootNode.get("instanceId").textValue();
        String instanceType = rootNode.get("instanceType").textValue();
        return new InstanceNotFoundException(instanceId, instanceType);
    }
}
