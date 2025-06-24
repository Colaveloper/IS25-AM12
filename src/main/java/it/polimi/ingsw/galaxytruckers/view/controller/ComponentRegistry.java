package it.polimi.ingsw.galaxytruckers.view.controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.galaxytruckers.utils.JsonUtils;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.*;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Connector;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Singleton registry for ship components in the game.
 * <p>
 * This class loads component data from a JSON file and provides access to component metadata and image paths.
 * It supports retrieving components by ID, the mapping from component IDs to image paths, and starting cabins for each color.
 * </p>
 */
public class ComponentRegistry {
    private static ComponentRegistry instance;
    private static final File componentJson = new File("src/main/resources/tiles.json");

    private final Map<Integer, JsonNode> components;
    private final Map<GameColor, JsonNode> startingCabins;

    /**
     * Returns the singleton instance of the registry, creating it if necessary.
     *
     * @return the singleton instance
     */
    public static ComponentRegistry getInstance() {
        if (instance == null) {
            instance = new ComponentRegistry();
        }
        return instance;
    }

    private ComponentRegistry() {
        this.components = new HashMap<>();
        this.startingCabins = new HashMap<>();
        try {
            loadComponents();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves a component by its ID.
     *
     * @param id the ID of the component
     * @return the {@link Component} corresponding to the ID
     * @throws IllegalArgumentException if no component with the given ID exists
     */
    public Component getComponent(int id) {
        if (!components.containsKey(id))
            throw new IllegalArgumentException("No component with id " + id);
        try {
            return parseComponent(id, components.get(id));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns a mapping from component IDs to their image file paths.
     *
     * @return a map from component ID to image path
     */
    public Map<Integer, Path> getIdToImagePath() {
        return components.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> Path.of(e.getValue().get("path").asText())
                ));
    }

    /**
     * Retrieves the starting cabin component for a given player color.
     *
     * @param color the color of the player
     * @return the starting {@link Component} for the player color
     * @throws RuntimeException if there is an error parsing the component
     */
    public Component getStartingCabin(GameColor color) {
        try {
            return parseComponent(startingCabins.get(color));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the total number of components in the registry.
     *
     * @return the number of components
     */
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
            String type = node.get("type").asText();
            if (type.equals("cabin") && node.get("color") != null) {
                startingCabins.put(GameColor.valueOf(node.get("color").asText().toUpperCase()), node);
            }
        }
    }

    private Component parseComponent(int id, JsonNode node) throws IOException{

        String type = node.get("type").asText();
        Component component;
        Map<Direction, Connector> connectors = parseConnectors(node.get("connectors"));

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

    private Component parseComponent(JsonNode node) throws IOException {
        return parseComponent(node.get("id").asInt(),node);
    }

    private Map<Direction, Connector> parseConnectors(JsonNode connectorNode){
        return JsonUtils.nodeToConnector(connectorNode);
    }

    private static CrewType parseCrewType(JsonNode crewTypeNode) throws JsonParseException {
        if(crewTypeNode != null && !crewTypeNode.isNull()){
            return CrewType.valueOf(crewTypeNode.asText().toUpperCase()); //convert string to enum
        }
        throw new JsonParseException("Cannot parse crew type");
    }
}
