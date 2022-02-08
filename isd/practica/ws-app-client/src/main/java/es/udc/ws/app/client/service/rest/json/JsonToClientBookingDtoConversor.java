package es.udc.ws.app.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.client.service.dto.ClientBookingDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JsonToClientBookingDtoConversor {

    public static ClientBookingDto toClientBookingDto(InputStream jsonBooking) throws ParsingException{
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonBooking);
            return toClientBookingDto(rootNode);
        }catch (ParsingException ex){
            throw ex;
        }catch (Exception e){
            throw new ParsingException(e);
        }
    }

    public static List<ClientBookingDto> toClientBookingDtos(InputStream jsonBookings){
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonBookings);
            if(rootNode.getNodeType() != JsonNodeType.ARRAY){
                throw new ParsingException("Unrecognized JSON (array expected)");
            }else {
                ArrayNode bookingArray = (ArrayNode) rootNode;
                List<ClientBookingDto> bookingDtos = new ArrayList<>(bookingArray.size());
                for(JsonNode bookingNode : bookingArray){
                    bookingDtos.add(toClientBookingDto(bookingNode));
                }
                return bookingDtos;
            }
        }catch (ParsingException ex){
            throw ex;
        }catch (Exception e){
            throw new ParsingException(e);
        }

    }

    public static ClientBookingDto toClientBookingDto(JsonNode bookingNode) throws ParsingException {
        if(bookingNode.getNodeType() != JsonNodeType.OBJECT){
            throw  new ParsingException("Unrecognized JSON (object expected)");
        } else {
             ObjectNode tripObject = (ObjectNode) bookingNode;

             Long id = tripObject.get("id").longValue();
             Long tripID = tripObject.get("tripId").longValue();
             String email = tripObject.get("email").textValue().trim();
             String lastCCDigits = tripObject.get("lastCCDigits").textValue().trim();
             int size = tripObject.get("size").intValue();
             double bookingPrice = tripObject.get("bookingPrice").doubleValue();
             LocalDateTime bookingDate = LocalDateTime.parse(tripObject.get("bookingDate").textValue().trim());

            JsonNode cancellationDateNode = tripObject.get("cancellationDate");
            LocalDateTime cancellationDate= (cancellationDateNode != null)? LocalDateTime.parse(cancellationDateNode.textValue().trim()):null;

            return new ClientBookingDto(id,tripID,email,lastCCDigits,size,bookingPrice,bookingDate,cancellationDate);
        }
    }


}
