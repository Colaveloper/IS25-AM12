package it.polimi.ingsw.galaxytruckers.utils;

import com.fasterxml.jackson.databind.JsonNode;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Connector;
import it.polimi.ingsw.galaxytruckers.view.Direction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonUtils {

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
}
