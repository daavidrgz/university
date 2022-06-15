package es.udc.ws.app.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.client.service.dto.ClientTripDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JsonToClientTripDtoConversor {
    public static ObjectNode toObjectNode(ClientTripDto trip) {
        ObjectNode tripObject = JsonNodeFactory.instance.objectNode();
        if (trip.getId() != null) {
            tripObject.put("id", trip.getId());
        }
        tripObject.put("city", trip.getCity()).
                put("description", trip.getDescription()).
                put("startDate", trip.getStartDate().toString()).
                put("price", trip.getPrice()).
                put("maxSlots", trip.getMaxSlots());
        //No hace falta mandar el remaining slots, el modelo lo va a ignorar siempre

        return tripObject;
    }

    public static ClientTripDto toClientTripDto(InputStream jsonTrip) throws ParsingException {
        try{
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonTrip);
            return toClientTripDto(rootNode);
        }catch (ParsingException e){
            throw e;
        }catch(Exception e){
            throw new ParsingException(e);
        }
    }
    public static List<ClientTripDto> toClientTripDtos(InputStream jsonTrips) throws  ParsingException{
        try{
            ObjectMapper objectMappger = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMappger.readTree(jsonTrips);
            if(rootNode.getNodeType() != JsonNodeType.ARRAY){
                throw new ParsingException("Unrecognized JSON (Array expected)");
            }else{
                ArrayNode tripsArray = (ArrayNode) rootNode;
                List<ClientTripDto> tripDtos= new ArrayList<>(tripsArray.size());
                for (JsonNode tripNode : tripsArray){
                    tripDtos.add(toClientTripDto(tripNode));
                }
                return tripDtos;
            }
        }catch (ParsingException e){
            throw e;
        }catch(Exception e){
            throw new ParsingException(e);
        }
    }
    private static ClientTripDto toClientTripDto(JsonNode tripNode) throws ParsingException{
        if (tripNode.getNodeType() != JsonNodeType.OBJECT){
            throw new ParsingException("Unrecognized JSON (object expected)");
        }else{
            ObjectNode tripObject = (ObjectNode) tripNode;

            //No hace falta comprobar si no nos manda el id. El servicio
            //siempre va a mandar la representación del trip con id
            Long id = tripObject.get("id").longValue();

            String city = tripObject.get("city").textValue().trim();
            String description = tripObject.get("description").textValue().trim();
            LocalDateTime startDate = LocalDateTime.parse((tripObject.get("startDate").textValue().trim()));
            double price = tripObject.get("price").doubleValue();
            int maxSlots = tripObject.get("maxSlots").intValue();

            //No hace falta comprobar si es nulo. El servicio siempre va a mandar la
            //representación del trip con remaining slots
            int remainingSlots = tripObject.get("remainingSlots").intValue();

            int bookedSlots = maxSlots - remainingSlots;

            return new ClientTripDto(id,city,description,startDate,price,maxSlots,bookedSlots);
        }
    }
}
