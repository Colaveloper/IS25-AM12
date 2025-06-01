package it.polimi.ingsw.galaxytruckers.view.controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.*;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Connector;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ComponentRegistry {
    private static ComponentRegistry instance;
    private static final File componentJson = new File("src/main/resources/tiles.json");

    private final Map<Integer, JsonNode> components;

    public static ComponentRegistry getInstance() {
        if (instance == null) {
            instance = new ComponentRegistry();
        }
        return instance;
    }

    private ComponentRegistry() {
        this.components = new HashMap<>();
        try {
            loadComponents();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Component getComponent(int id) {
        if (!components.containsKey(id))
            throw new IllegalArgumentException("No component with id " + id);
        try {
            return parseComponent(id, components.get(id));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<Integer, Path> getIdToPath() {
        return components.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> Path.of(e.getValue().get("path").asText())
                ));
    }

    public int getSize() {
        return components.size();
    }

    private void loadComponents() throws IOException {
        //reading from the JSON file and returning the list of components
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(componentJson);

        //iterating through nodes and adding each as a component to the list
        for (JsonNode node : rootNode){
            int id = node.get("id").asInt();
            components.put(id, node);
        }
    }

    private Component parseComponent(int id, JsonNode node) throws IOException{

        String type = node.get("type").asText();
        Component component;
        List<Connector> connectors = parseConnectors(node.get("connectors"));

        switch (type) {
            case "shield" -> {
                component = new Shield(connectors, id);
            }
            case "life_support" -> {
                CrewType crewType = parseCrewType(node.get("crewtype"));
                component = new LifeSupport(connectors, id, crewType);
            }
            case "double_cannon" ->
                component = new DoubleCannon(connectors, id);
            case "cannon" ->
                component = new Cannon(connectors, id);
            case "double_engine" ->
                component = new DoubleEngine(connectors, id);
            case "engine" ->
                component = new Engine(connectors, id);
            case "cargo_hold" -> {
                int size = node.get("size").asInt();
                Boolean isSpecial = node.get("special").asBoolean();
                component = new CargoHold(connectors, id, isSpecial, size);
            }
            case "structural" ->
                component = new Component(connectors, id);
            case "battery" -> {
                int numBatteries = node.get("batteries").asInt();
                component = new Battery(connectors, id, numBatteries);
            }
            case "cabin" ->
                component = new Cabin(connectors, id);
            default ->
                throw new IllegalArgumentException("Unknown component type: " + type);
        }
        return component;
    }

    private static List<Connector> parseConnectors(JsonNode connectorNode){
        List<Connector> connectors = new ArrayList<>();
        if(connectorNode != null && connectorNode.isArray()){
            for (JsonNode conn : connectorNode){
                connectors.add(Connector.valueOf(conn.asText().toUpperCase())); //convert string to enum
            }
        }
        return connectors;
    }

    private static CrewType parseCrewType(JsonNode crewTypeNode) throws JsonParseException {
        if(crewTypeNode != null && !crewTypeNode.isNull()){
            return CrewType.valueOf(crewTypeNode.asText().toUpperCase()); //convert string to enum
        }
        throw new JsonParseException("Cannot parse crew type");
    }
}
