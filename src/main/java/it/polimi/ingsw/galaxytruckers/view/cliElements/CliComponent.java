package it.polimi.ingsw.galaxytruckers.view.cliElements;

import com.fasterxml.jackson.databind.JsonNode;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Connector;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.view.enums.Highlights;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.view.enums.ComponentType;
import javafx.beans.property.ObjectProperty;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CliComponent extends CliElement {

    private final Component component;
    private boolean isSpecial;
    private final List<Connector> connectors = new ArrayList<>();
    private String open;
    private String close;


    private String crewColorOpen;

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
            case 0 -> switch (connectors.get(0)) {
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

    public void highlight(Highlights color) {
        open = color.getHighlight();
        close = Highlights.RESET.getHighlight();
    }

    @Override
    public List<String> getDescription() {
        JsonNode node = componentProperty.get().getNode();

        Highlights color = Highlights.getSomeColors(componentProperty.get().getDisconnectedShipIndex()).getLast();
        open = color.getHighlight();
        close = Highlights.RESET.getHighlight();

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

        if (componentProperty.get().isSelectableProperty().get()) {
            open = Highlights.GREEN.getHighlight();
            close = Highlights.RESET.getHighlight();
        }

        if (type == ComponentType.CABIN) {
            crewColorOpen = switch(componentProperty.get().crewTypeProperty().get()) {
                case CrewType.HUMAN -> Highlights.WHITE.getHighlight();
                case CrewType.PURPLE -> Highlights.PURPLE.getHighlight();
                case CrewType.BROWN -> Highlights.YELLOW.getHighlight();
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
                lines.add(1, open + getConnector(3) + " " + crewColorOpen + type.getSymbol(componentProperty.get().directionProperty().get()) + Highlights.RESET.getHighlight() + componentProperty.get().statProperty().get() + getConnector(1)+close);
            }
            else if (type == ComponentType.CARGO_HOLD){
                StringBuilder cargoPrint = new StringBuilder();

                String empty = isSpecial ? "○" : "□";
                String full = isSpecial ? "●" : "■";
                cargoPrint.append(open).append(getConnector(3)).append(close);

                for (GoodsType i : componentProperty.get().cargoProperty().get()) {
                    switch (i) {
                        case RED:
                            cargoPrint.append(Highlights.RED.getHighlight()).append(full).append(Highlights.RESET.getHighlight());
                            break;
                        case YELLOW:
                            cargoPrint.append(Highlights.YELLOW.getHighlight()).append(full).append(Highlights.RESET.getHighlight());
                            break;
                        case BLUE:
                            cargoPrint.append(Highlights.BLUE.getHighlight()).append(full).append(Highlights.RESET.getHighlight());
                            break;
                        case GREEN:
                            cargoPrint.append(Highlights.GREEN.getHighlight()).append(full).append(Highlights.RESET.getHighlight());
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
 
}
