package it.polimi.ingsw.galaxytruckers.view.cli;

import com.fasterxml.jackson.databind.JsonNode;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Connector;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.Component;
import it.polimi.ingsw.galaxytruckers.view.viewEnums.ComponentType;
import javafx.beans.property.ObjectProperty;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CliComponent extends CliElement {
    private static final String colorRed = "\u001b[31m";
    private static final String colorGreen = "\u001b[32m";
    private static final String colorBlue = "\u001b[34m";
    private static final String colorYellow = "\u001b[33m";
    private static final String colorReset = "\u001B[0m";

    private final ObjectProperty<Component> componentProperty;
    private ComponentType type;
    private boolean isSpecial;
    private final List<Connector> connectors = new ArrayList<>();

    private String crewColorOpen;
    private String crewColorClose;

    public CliComponent(ClientModel model, ObjectProperty<Component> componentProperty) throws IOException {
        super(model);
        this.componentProperty = componentProperty;
        componentProperty.addListener(this);
        componentProperty.addListener((observable, oldValue, newValue) -> {
            newValue.isSelectableProperty().addListener(this);
            newValue.directionProperty().addListener(this);
            newValue.cargoProperty().addListener(this);
            newValue.crewTypeProperty().addListener(this);
            newValue.statProperty().addListener(this);
        });
    }

    public String getConnector(int connectorDirection) {
        return switch (connectorDirection) {
            case 0 -> switch (connectors.get(componentProperty.get().directionProperty().get() % 4)) {
                case Connector.NONE -> "─";
                case Connector.SINGLE -> "┴";
                case Connector.DOUBLE -> "╨";
                case Connector.UNIVERSAL -> "╩";
            };
            case 1 -> switch (connectors.get((1 + componentProperty.get().directionProperty().get()) % 4)) {
                case Connector.NONE -> "│";
                case Connector.SINGLE -> "├";
                case Connector.DOUBLE -> "╞";
                case Connector.UNIVERSAL -> "╠";
            };
            case 2 -> switch (connectors.get((2 + componentProperty.get().directionProperty().get()) % 4)) {
                case Connector.NONE -> "─";
                case Connector.SINGLE -> "┬";
                case Connector.DOUBLE -> "╥";
                case Connector.UNIVERSAL -> "╦";
            };
            case 3 -> switch (connectors.get((3 + componentProperty.get().directionProperty().get()) % 4)) {
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
        JsonNode node = componentProperty.get().getNode();
        connectors.clear();
        connectors.addAll(parseConnectors(node.get("connectors")));
        type = ComponentType.valueOf(node.get("type").asText().toUpperCase());
        if (type == ComponentType.BATTERY) {
            componentProperty.get().statProperty().set(node.get("batteries").asInt());
        }
        if (type == ComponentType.CARGO_HOLD) {
            componentProperty.get().statProperty().set(node.get("size").asInt());
            isSpecial = node.get("special").asBoolean();
        } else {
            isSpecial = false;
        }

        String open = componentProperty.get().isSelectableProperty().get() ? colorGreen : "";
        String close = componentProperty.get().isSelectableProperty().get() ? colorReset : "";

        if (type == ComponentType.CABIN) {
            crewColorOpen = switch(componentProperty.get().crewTypeProperty().get()) {
                case CrewType.HUMAN -> "\u001B[32m";
                case CrewType.PURPLE -> "\u001B[33m";
                case CrewType.BROWN -> "\u001B[32m"; // TODO: test they're all correct
            };
        }


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
                lines.add(1, open + getConnector(3) + " " + type.getSymbol(componentProperty.get().directionProperty().get()) + componentProperty.get().statProperty().get() + getConnector(1)+close);
            }
            else if (type == ComponentType.CABIN) {
                lines.add(1, open + getConnector(3) + " " + crewColorOpen + type.getSymbol(componentProperty.get().directionProperty().get()) + colorReset + componentProperty.get().statProperty().get() + getConnector(1)+close);
            }
            else if (type == ComponentType.CARGO_HOLD){
                StringBuilder cargoPrint = new StringBuilder();

                String empty = isSpecial ? "○" : "□";
                String full = isSpecial ? "●" : "■";
                cargoPrint.append(open).append(getConnector(3)).append(close);

                for (GoodsType i : componentProperty.get().cargoProperty().get()) {
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

                cargoPrint.append(empty.repeat(componentProperty.get().statProperty().get() - componentProperty.get().cargoProperty().get().size()));
                cargoPrint.append( " ".repeat(3 - componentProperty.get().statProperty().get()));

                cargoPrint.append(open).append(getConnector(1)).append(close);
                lines.add(1, cargoPrint.toString());
               // lines.add(1, open + getConnector(3) + " " + type.getSymbol(component.get().directionProperty()) + " " + getConnector(1)+close);

            }
            else {//default
                lines.add(1, open + getConnector(3) + " " + type.getSymbol(componentProperty.get().directionProperty().get()) + " " + getConnector(1)+close);
            }

            lines.add(2, open+"╰─" + getConnector(2) + "─╯"+close);
        }
        return lines;
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


    public void setGoods(List<GoodsType> goods) {
         componentProperty.get().cargoProperty().set(goods);
    }

    public void setCrewType(CrewType crewType) {

    }

    public void setSelectable() {
        componentProperty.get().isSelectableProperty().set(true);
    }
}
