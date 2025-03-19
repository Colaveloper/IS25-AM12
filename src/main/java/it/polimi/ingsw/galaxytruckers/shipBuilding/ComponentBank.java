package it.polimi.ingsw.galaxytruckers.shipBuilding;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ComponentBank {
    private static ComponentBank instance;

    private final List<Component> coveredComponents;
    private final Map<Integer, Component> uncoveredComponents;

    public ComponentBank() {
        //TODO: read components from file and shuffle them
        this.coveredComponents = new ArrayList<>();
        this.uncoveredComponents = new HashMap<>();
    }

    public static ComponentBank getInstance() {
        if (instance == null) {
            instance = new ComponentBank();
        }
        return instance;
    }

    public Component getComponent(int id) {
        if (!uncoveredComponents.containsKey(id)) {
            throw new IllegalArgumentException("No component with id " + id + " exists");
        }
        return uncoveredComponents.remove(id);
    }

    public Component getRanComponent() {
        return coveredComponents.removeLast();
    }

    public void addUncovered(Component component) {
        uncoveredComponents.put(component.hashCode(), component);
    }

    public static List<Component> loadComponents(File jsonFile) throws IOException{
        //reading from json file and adding to coveredComponents
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonFile);

        List<Component> components = new ArrayList<>();

        for (JsonNode node : rootNode){
            String type = node.get("type").asText();
            Component component;
            List<Connector> connectors = parseConnectors(node.get("connectors"));

            switch(type){
                case "shield":
                    component = new Shield(connectors);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown component type: " + type);
            }

            components.add(component);
        }
        return components;
    }

    private static List<Connector> parseConnectors(JsonNode connectorNode){
        List<Connector> connectors = new ArrayList<>();
        if(connectorNode != null && connectorNode.isArray()){
            for (JsonNode conn : connectorNode){
                connectors.add(Connector.valueOf(conn.asText().toUpperCase()));
            }
        }
        return connectors;
    }
}