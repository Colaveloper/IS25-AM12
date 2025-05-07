package it.polimi.ingsw.galaxytruckers.view.shipBuildingClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Connector;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import it.polimi.ingsw.galaxytruckers.view.Physical;
import it.polimi.ingsw.galaxytruckers.view.viewEnums.ComponentType;
import javafx.animation.RotateTransition;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Component extends Physical {
    private final ComponentType type;
    private List<Connector> connectors = new ArrayList<>();
    private IntegerProperty direction;
    private boolean isSelectable;
    private int componentId;
    private String imagePath;

    private int componentStat;
    private CrewType crewType;

    private final String colorRed = "\u001b[31m";
    private final String colorGreen = "\u001b[32m";
    private final String colorBlue = "\u001b[34m";
    private final String colorYellow = "\u001b[33m";
    private final String colorReset = "\u001B[0m";

    private List<GoodsType> cargo;
    private Boolean isSpecial;

    private String crewColorOpen;
    private String crewColorClose;
    private StringBuilder cargoPrint;

    public Component(int direction, int componentId) throws IOException {
        this.componentId = componentId;
        this.direction = new SimpleIntegerProperty(direction);
        super.registerObservables(this.direction);
        directionProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("[COMPONENT] rotation changed "+oldVal+"-->"+newVal);
        });
        String jsonPath = "src/main/resources/tiles.json";
        File jsonFile = new File(jsonPath);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonFile);
        JsonNode node = rootNode.get(componentId);
        imagePath = node.get("path").asText();
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
            this.isSpecial = node.get("special").asBoolean();
            //cargo.add(GoodsType.RED);
        }
    }

    public Component(ComponentType type) {
        this.type = type;
        this.direction = new SimpleIntegerProperty(0);
        super.registerObservables(direction);
    }

    public String getConnector(int connectorDirection) {
        return switch (connectorDirection) {
            case 0 -> switch (connectors.get(direction.get() % 4)) {
                case Connector.NONE -> "─";
                case Connector.SINGLE -> "┴";
                case Connector.DOUBLE -> "╨";
                case Connector.UNIVERSAL -> "╩";
            };
            case 1 -> switch (connectors.get((1 + direction.get()) % 4)) {
                case Connector.NONE -> "│";
                case Connector.SINGLE -> "├";
                case Connector.DOUBLE -> "╞";
                case Connector.UNIVERSAL -> "╠";
            };
            case 2 -> switch (connectors.get((2 + direction.get()) % 4)) {
                case Connector.NONE -> "─";
                case Connector.SINGLE -> "┬";
                case Connector.DOUBLE -> "╥";
                case Connector.UNIVERSAL -> "╦";
            };
            case 3 -> switch (connectors.get((3 + direction.get()) % 4)) {
                case Connector.NONE -> "│";
                case Connector.SINGLE -> "┤";
                case Connector.DOUBLE -> "╡";
                case Connector.UNIVERSAL -> "╣";
            };
            default -> "error";
        };
    }

    @Override
    public List<String> getNewDescription() {
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
                lines.add(1, open + getConnector(3) + " " + type.getSymbol(direction.get()) + componentStat + getConnector(1)+close);
            }
            else if (type == ComponentType.CABIN) {
                lines.add(1, open + getConnector(3) + " " + crewColorOpen + type.getSymbol(direction.get()) + crewColorClose + componentStat + getConnector(1)+close);
            }
            else if (type == ComponentType.CARGO_HOLD){
                cargoPrint = new StringBuilder();

                String empty = isSpecial ? "○" : "□";
                String full = isSpecial ? "●" : "■";
                cargoPrint.append(open).append(getConnector(3)).append(close);

                for (GoodsType i : cargo) {
                    switch (i) {
                        case RED:
                            cargoPrint.append(colorRed).append(full).append(colorReset);
                            break;
                        case YELLOW:
                            cargoPrint.append(colorYellow).append(full).append(colorReset);
                            break;
                        case BLUE:
                            cargoPrint.append(colorBlue).append(full).append(colorReset);
                            break;
                        case GREEN:
                            cargoPrint.append(colorGreen).append(full).append(colorReset);
                            break;
                    }
                }

                cargoPrint.append(empty.repeat(componentStat - cargo.size()));
                cargoPrint.append( " ".repeat(3 - componentStat));

                cargoPrint.append(open).append(getConnector(1)).append(close);
                lines.add(1, cargoPrint.toString());
               // lines.add(1, open + getConnector(3) + " " + type.getSymbol(direction) + " " + getConnector(1)+close);

            }
            else {//default
                lines.add(1, open + getConnector(3) + " " + type.getSymbol(direction.get()) + " " + getConnector(1)+close);
            }

            lines.add(2, open+"╰─" + getConnector(2) + "─╯"+close);
        }
        return lines;
    }

    @Override
    public Node getNode(VirtualServer server) {
        Image image = new Image(imagePath);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
        imageView.setRotate(0); // initial rotation

        // Track and force counterclockwise rotation (by -90° per click)
        direction.addListener((obs, oldVal, newVal) -> {
            // Counterclockwise rotation: Always rotate by -90°
            RotateTransition rt = new RotateTransition(Duration.millis(300), imageView);
            rt.setByAngle(-90); // negative to rotate counterclockwise
            rt.play();
        });

        // Click to increment direction (counterclockwise)
        imageView.setOnMouseClicked(e -> rotateLeft());

        return imageView;
    }

    public void rotateLeft() {
        direction.setValue((direction.get()+3)%4);
    }

    public IntegerProperty directionProperty() {
        return direction;
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

    public void setSelectable() {
        isSelectable = true;
    }
}
