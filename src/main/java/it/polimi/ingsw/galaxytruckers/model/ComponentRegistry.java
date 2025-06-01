package it.polimi.ingsw.galaxytruckers.model;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.*;
import it.polimi.ingsw.galaxytruckers.utils.JsonUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComponentRegistry {
    private static ComponentRegistry instance;

    private final File componentJson = new File("src/main/resources/tiles.json");

    private final List<JsonNode> bankComponents = new ArrayList<>();
    private final Map<GameColor, JsonNode> startingCabins = new HashMap<>();

    public static ComponentRegistry getInstance() {
        if (instance == null) instance = new ComponentRegistry();
        return instance;
    }

    private ComponentRegistry() {
        try {
            loadFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Component> getBankComponents() {
        List<Component> res = new ArrayList<>();
        for (JsonNode node: bankComponents) {
            try {
                res.add(parseComponent(node));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return res;
    }

    public Component getStartingCabin(GameColor color) {
        try {
            return parseComponent(startingCabins.get(color));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadFile() throws IOException {
        //reading from json file and returning the list of components
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(componentJson);

        for (JsonNode node : rootNode) {
            String type = node.get("type").asText();
            if (type.equals("cabin")) {
                if (node.get("color") != null) {
                    GameColor cabinColor = GameColor.valueOf(node.get("color").asText().toUpperCase());
                    startingCabins.put(cabinColor, node);
                } else {
                    bankComponents.add(node);
                }
            } else {
                bankComponents.add(node);
            }
        }
    }

    protected Component parseComponent(JsonNode node) throws IOException {
        //iterating through nodes and adding each as a component to list
        String type = node.get("type").asText();
        Component component;
        List<Connector> connectors = parseConnectors(node.get("connectors"));
        int id = node.get("id").asInt();
        switch(type){
            case "shield" ->
                component = new Shield(connectors, id);
            case "life_support" -> {
                CrewType crewType = parseCrewType(node.get("crewtype"));
                component = new LifeSupport(connectors, id, crewType);
            }
            case "double_cannon" ->
                component = new DoubleCannon(connectors);
            case "double cannon" ->
                component = new DoubleCannon(connectors, id);
            case "cannon" ->
                component = new Cannon(connectors, id);
            case "double_engine" ->
                component = new DoubleEngine(connectors);
            case "double engine" ->
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
                component = new Cabin(connectors,id);
            default ->
                throw new IllegalArgumentException("Unknown component type: " + type);
        }
        return component;
    }

    private static List<Connector> parseConnectors(JsonNode connectorNode){
        return JsonUtils.nodeToConnector(connectorNode);
    }

    private static CrewType parseCrewType(JsonNode crewTypeNode) throws JsonParseException {
        if(crewTypeNode != null && !crewTypeNode.isNull()){
            return CrewType.valueOf(crewTypeNode.asText().toUpperCase()); //convert string to enum
        }
        throw new JsonParseException("Cannot parse crew type");
    }
}
