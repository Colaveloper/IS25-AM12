package it.polimi.ingsw.galaxytruckers.view;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Connector;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
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

    private int componentStat;
    private CrewType crewType;

    private final String colorRed = "\u001b[31m";
    private final String colorGreen = "\u001b[32m";
    private final String colorBlue = "\u001b[34m";
    private final String colorYellow = "\u001b[33m";
    private final String colorReset = "\u001B[0m";

    private List<GoodsType> cargo;

    private String crewColorOpen;
    private String crewColorClose;
    private StringBuilder cargoPrint;

    public Component(int direction, int componentId) throws IOException {
        rotation = direction;
        String jsonPath = "src/main/resources/tiles.json";
        File jsonFile = new File(jsonPath);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonFile);
        JsonNode node = rootNode.get(componentId);
        type = ComponentType.valueOf(node.get("type").asText().toUpperCase());
        connectors = parseConnectors(node.get("connectors"));
        if (type == ComponentType.BATTERY) {
            this.componentStat = node.get("batteries").asInt();
        }
        if (type == ComponentType.CABIN) {
            this.crewType = CrewType.HUMAN; //TODO: initialize cabins in test
            this.componentStat = 2;
        }
        if (type == ComponentType.CARGO_HOLD) {
            this.componentStat = node.get("size").asInt();
            cargo = new ArrayList<>();
            //cargo.add(GoodsType.RED);
        }

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
        String open = isSelectable ? colorGreen : "";
        String close = isSelectable ? colorReset : "";

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

            if (type == ComponentType.BATTERY) {
                lines.add(1, open + getConnector(3) + " " + type.getSymbol(rotation) + componentStat + getConnector(1)+close);
            }
            else if (type == ComponentType.CABIN) {
                lines.add(1, open + getConnector(3) + " " + crewColorOpen + type.getSymbol(rotation) + crewColorClose + componentStat + getConnector(1)+close);
            }
            else if (type == ComponentType.CARGO_HOLD){
                cargoPrint = new StringBuilder();
                cargoPrint.append(open + getConnector(3) + close);

                for (GoodsType i : cargo) {
                    switch (i) {
                        case RED:
                            cargoPrint.append(colorRed + "●" + colorReset);
                            break;
                        case YELLOW:
                            cargoPrint.append(colorYellow + "●" + colorReset);
                            break;
                        case BLUE:
                            cargoPrint.append(colorBlue + "●" + colorReset);
                            break;
                        case GREEN:
                            cargoPrint.append(colorGreen + "●" + colorReset);
                            break;
                    }
                }

                cargoPrint.append("□".repeat(componentStat - cargo.size()));
                cargoPrint.append( " ".repeat(3 - componentStat));

                cargoPrint.append(open + getConnector(1) + close);
                lines.add(1, cargoPrint.toString());
               // lines.add(1, open + getConnector(3) + " " + type.getSymbol(rotation) + " " + getConnector(1)+close);

            }
            else {//default
                lines.add(1, open + getConnector(3) + " " + type.getSymbol(rotation) + " " + getConnector(1)+close);
            }

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

    public void setStat(int stat) {
        this.componentStat = stat;
    }

    public void setGoods(List<GoodsType> goods) {
        this.cargo = goods;
    }

    public void setCrewRace(CrewType crewType) {
        switch (crewType) {
            case CrewType.HUMAN:
                crewColorOpen = "\u001B[32m";
                crewColorClose = "\u001B[0m";
                this.componentStat = 2;
                break;
            case CrewType.PURPLE:
                crewColorOpen = "\u001B[32m";
                crewColorClose = "\u001B[0m";
                this.componentStat = 1;
                break;
            case CrewType.BROWN:
                crewColorOpen = "\u001B[32m";
                crewColorClose = "\u001B[0m";
                this.componentStat = 1;
                break;
            default:
                this.componentStat = -1;
        }
        this.crewType = crewType;
    }

    public void subtractStat(int stat) {
        this.componentStat -= stat;
    }

    public void setSelectable() {
        isSelectable = true;
    }
}
