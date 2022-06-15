package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.restservice.dto.RestTripDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

public class JsonToRestTripDtoConversor {
    public static ObjectNode toObjectNode(RestTripDto trip) {
        ObjectNode tripObject = JsonNodeFactory.instance.objectNode();
        if (trip.getId() != null) {
            tripObject.put("id", trip.getId());
        }
        tripObject.put("city", trip.getCity()).
                put("description", trip.getDescription()).
                put("startDate", trip.getStartDate().toString()).
                put("price", trip.getPrice()).
                put("maxSlots", trip.getMaxSlots()).
                put("remainingSlots", trip.getRemainingSlots());

        return tripObject;
    }

    public static ArrayNode toArrayNode(List<RestTripDto> trips){
        ArrayNode tripsNode = JsonNodeFactory.instance.arrayNode();
        for (RestTripDto trip : trips){
            ObjectNode tripObject = toObjectNode(trip);
            tripsNode.add(tripObject);
        }
        return tripsNode;
    }

    public static RestTripDto toRestTripDto(InputStream jsonTrip) throws ParsingException{
        try{
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonTrip);

            if (rootNode.getNodeType() != JsonNodeType.OBJECT){
                throw new ParsingException("Unrecognized JSON (object expected)");
            }else{
                ObjectNode tripObject = (ObjectNode) rootNode;

                JsonNode tripIdNode = tripObject.get("id");
                Long id = (tripIdNode != null)? tripIdNode.longValue():null;

                String city = tripObject.get("city").textValue().trim();
                String description = tripObject.get("description").textValue().trim();
                LocalDateTime startDate = LocalDateTime.parse((tripObject.get("startDate").textValue().trim()));
                double price = tripObject.get("price").doubleValue();
                int maxSlots = tripObject.get("maxSlots").intValue();


                return new RestTripDto(id,city,description,startDate,price,maxSlots,null);
            }
        }catch (ParsingException e){
            throw e;
        }catch(Exception e){
            throw new ParsingException(e);
        }
    }
}
