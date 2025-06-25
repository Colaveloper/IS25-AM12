package it.polimi.ingsw.galaxytruckers.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Connector;
import it.polimi.ingsw.galaxytruckers.serverController.dto.ShipBoardDTO;
import it.polimi.ingsw.galaxytruckers.utils.json.PointKeyDeserializer;
import it.polimi.ingsw.galaxytruckers.utils.json.PointSerializer;
import it.polimi.ingsw.galaxytruckers.view.Direction;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for JSON-related operations in the Galaxy Truckers project.
 * Provides helper methods to convert JSON nodes to model objects.
 */
public class JsonUtils {

    /**
     * Converts a JSON array node representing connectors into a map of directions to connectors.
     * The order of connectors in the array is assumed to be [UP, LEFT, DOWN, RIGHT].
     *
     * @param connectorNode the JSON node containing connector information as an array
     * @return a map associating each direction with its corresponding connector
     */
    public static Map<Direction, Connector> nodeToConnector(JsonNode connectorNode) {
        Direction[] directions = {Direction.UP, Direction.LEFT, Direction.DOWN, Direction.RIGHT};
        Map<Direction, Connector> connectors = new HashMap<>();
        if(connectorNode != null && connectorNode.isArray()) {
            for (int i = 0; i < directions.length && i < connectorNode.size(); i++) {
                connectors.put(
                        directions[i],
                        Connector.valueOf(
                                connectorNode.get(i).asText().toUpperCase()
                        )
                );
            }
        }
        return connectors;
    }

    // old implementation

//    List<Connector> connectors = new ArrayList<>();
//        if(connectorNode != null && connectorNode.isArray()){
//        for (JsonNode conn : connectorNode){
//            connectors.add(Connector.valueOf(conn.asText().toUpperCase())); //convert string to enum
//        }
//    }
//        return connectors;

    public static JsonNode serializeShipBoardDTO(ShipBoardDTO shipBoardDTO) {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addKeySerializer(Point.class, new PointSerializer());
        module.addKeyDeserializer(Point.class, new PointKeyDeserializer());
        mapper.registerModule(module);
        return mapper.valueToTree(shipBoardDTO);
    }

    public static ShipBoardDTO deserializeShipBoardDTO(JsonNode shipBoardDTO) {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addKeySerializer(Point.class, new PointSerializer());
        module.addKeyDeserializer(Point.class, new PointKeyDeserializer());
        mapper.registerModule(module);
        return mapper.convertValue(shipBoardDTO, ShipBoardDTO.class);
    }
}
