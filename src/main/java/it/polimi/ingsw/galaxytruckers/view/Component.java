package it.polimi.ingsw.galaxytruckers.view;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Connector;
import javafx.scene.image.Image;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Component implements Physical{
    private final ComponentType type;
    private List<Connector> connectors = new ArrayList<>();   //TODO: enum for connector type?
    private int rotation;
    private boolean isSelectable;

    public Component(int direction, int componentId) throws IOException {
        rotation = direction;
        String jsonPath = "src/main/resources/tiles.json";
        File jsonFile = new File(jsonPath);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonFile);
        JsonNode node = rootNode.get(componentId);
        type = ComponentType.valueOf(node.get("type").asText().toUpperCase());
        connectors = parseConnectors(node.get("connectors"));
    }

    public Component(ComponentType type) {
        this.type = type;
    }

    public String getConnector(int connectorDirection) {
        return switch (connectorDirection) {
            case 0 -> switch (connectors.get(rotation % 4)) {
                case Connector.NONE -> "─";
                case Connector.SINGLE -> "┴";
                case Connector.DOUBLE -> "╨";
                case Connector.UNIVERSAL -> "╩";
            };
            case 1 -> switch (connectors.get((1 + rotation) % 4)) {
                case Connector.NONE -> "│";
                case Connector.SINGLE -> "├";
                case Connector.DOUBLE -> "╞";
                case Connector.UNIVERSAL -> "╠";
            };
            case 2 -> switch (connectors.get((2 + rotation) % 4)) {
                case Connector.NONE -> "─";
                case Connector.SINGLE -> "┬";
                case Connector.DOUBLE -> "╥";
                case Connector.UNIVERSAL -> "╦";
            };
            case 3 -> switch (connectors.get((3 + rotation) % 4)) {
                case Connector.NONE -> "│";
                case Connector.SINGLE -> "┤";
                case Connector.DOUBLE -> "╡";
                case Connector.UNIVERSAL -> "╣";
            };
            default -> "error";
        };
    }

    @Override
    public Image getImage() {
        return null;
    }

    @Override
    public List<String> getDescription() {
        String open = isSelectable ? "\u001B[32m" : "";
        String close = isSelectable ? "\u001B[0m" : "";

        List<String> lines = new ArrayList<>();
        if (type == ComponentType.EMPTY_AREA) {
            lines.add(0, "     ");
            lines.add(1, "  X  ");
            lines.add(2, "     ");
        } else if (type == ComponentType.EMPTY_SPACE) {
            lines.add(0, "     ");
            lines.add(1, "     ");
            lines.add(2, "     ");
        } else {
            lines.add(0, open+"╭─" + getConnector(0) + "─╮"+close);
            lines.add(1, open+getConnector(3)+" "+type.getSymbol(rotation)+" "+getConnector(1)+close);
            lines.add(2, open+"╰─" + getConnector(2) + "─╯"+close);
        }
        return lines;
    }

    public void setRotation(int direction) {
        this.rotation = direction;
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

    public void setSelectable() {
        isSelectable = true;
    }
}
