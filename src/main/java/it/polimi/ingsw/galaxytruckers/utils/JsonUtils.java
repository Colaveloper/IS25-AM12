package it.polimi.ingsw.galaxytruckers.utils;

import com.fasterxml.jackson.databind.JsonNode;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Connector;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
    public static List<Connector> nodeToConnector(JsonNode connectorNode) {
        List<Connector> connectors = new ArrayList<>();
        if(connectorNode != null && connectorNode.isArray()){
            for (JsonNode conn : connectorNode){
                connectors.add(Connector.valueOf(conn.asText().toUpperCase())); //convert string to enum
            }
        }
        return connectors;
    }
}
