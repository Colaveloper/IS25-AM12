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
        //reading from json file and returning the list of components
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonFile);

        List<Component> components = new ArrayList<>();

        //iterating through nodes and adding each as a component to list
        for (JsonNode node : rootNode){
            String type = node.get("type").asText();
            Component component;
            List<Connector> connectors = parseConnectors(node.get("connectors"));

            switch(type){
                case "shield":
                    component = new Shield(connectors);
                    break;
                case "life support":
                    CrewType crewType = parseCrewType(node.get("crewtype"));
                    component = new LifeSupport(connectors, crewType);
                    break;
                case "double cannon":
                    component = new DoubleCannon(connectors);
                    break;
                case "cannon":
                    component = new Cannon(connectors);
                    break;
                case "double engine":
                    component = new DoubleEngine(connectors);
                    break;
                case "engine":
                    component = new Engine(connectors);
                    break;
                case "cargo hold":
                    int size = node.get("size").asInt();
                    Boolean isSpecial = node.get("special").asBoolean();
                    component = new CargoHold(connectors, size, isSpecial);
                    break;
                case "structural":
                    component = new Component(connectors);
                    break;
                case "battery":
                    int numBatteries = node.get("batteries").asInt();
                    component = new Battery(connectors, numBatteries);
                    break;
                case "cabin":
                    component = new Cabin(connectors);
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
                connectors.add(Connector.valueOf(conn.asText().toUpperCase())); //convert string to enum
            }
        }
        return connectors;
    }

    private static CrewType parseCrewType(JsonNode crewTypeNode){
        CrewType crewType;
        if(crewTypeNode != null && !crewTypeNode.isNull()){
            return CrewType.valueOf(crewTypeNode.asText().toUpperCase()); //convert string to enum
        }
        return null; //TODO: potentially make this throw an exception
    }
}