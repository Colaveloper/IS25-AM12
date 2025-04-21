package it.polimi.ingsw.galaxytruckers.model.shipBuilding;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ComponentBank {
    private List<Component> coveredComponents;
    private final Map<Integer, Component> uncoveredComponents;
    private static final File componentJson = new File("src/main/resources/tiles.json");

    public ComponentBank() {
        //TODO: read components from file and shuffle them
        this.coveredComponents = new ArrayList<>();
        this.uncoveredComponents = new HashMap<>();
    }

    public void initialize() throws IOException {
        this.coveredComponents = loadComponents();
    }

    public Component removeUncoveredComponent(int id) {
        if (!uncoveredComponents.containsKey(id)) {
            throw new IllegalArgumentException("No component with id " + id + " exists");
        }
        return uncoveredComponents.remove(id);
    }

    public Component drawRandComponent() {
        return coveredComponents.removeLast();
    }

    public void addToUncoveredComponents(Component component) {
        uncoveredComponents.put(component.getId(), component);
    }

    @VisibleForTesting
    public List<Component> getCoveredComponents() {
        return new ArrayList<>(coveredComponents);
    }

    @VisibleForTesting
    public Map<Integer, Component> getUncoveredComponents() {
        return new HashMap<>(uncoveredComponents);
    }

    @VisibleForTesting
    protected static List<Component> loadComponents() throws IOException{
        //reading from json file and returning the list of components
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(componentJson);
        List<Component> components = new ArrayList<>();

        //iterating through nodes and adding each as a component to list
        for (JsonNode node : rootNode){
            String type = node.get("type").asText();
            Component component;
            List<Connector> connectors = parseConnectors(node.get("connectors"));
            int id = node.get("id").asInt();

            switch(type){
                case "shield":
                    component = new Shield(connectors, id);
                    break;
                case "life support":
                    CrewType crewType = parseCrewType(node.get("crewtype"));
                    component = new LifeSupport(connectors, id, crewType);
                    break;
                case "double cannon":
                    component = new DoubleCannon(connectors, id);
                    break;
                case "cannon":
                    component = new Cannon(connectors, id);
                    break;
                case "double engine":
                    component = new DoubleEngine(connectors, id);
                    break;
                case "engine":
                    component = new Engine(connectors, id);
                    break;
                case "cargo hold":
                    int size = node.get("size").asInt();
                    Boolean isSpecial = node.get("special").asBoolean();
                    component = new CargoHold(connectors, id, isSpecial, size);
                    break;
                case "structural":
                    component = new Component(connectors, id);
                    break;
                case "battery":
                    int numBatteries = node.get("batteries").asInt();
                    component = new Battery(connectors, id, numBatteries);
                    break;
                case "cabin":
                    component = new Cabin(connectors, id);
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
        if(crewTypeNode != null && !crewTypeNode.isNull()){
            return CrewType.valueOf(crewTypeNode.asText().toUpperCase()); //convert string to enum
        }
        return null; //TODO: potentially make this throw an exception
    }
}