package it.polimi.ingsw.galaxytruckers.view.cliElements.CliComponents;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Connector;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliElement;
import it.polimi.ingsw.galaxytruckers.view.enums.Highlights;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.*;

import java.util.ArrayList;
import java.util.List;

public class CliComponent extends CliElement {

    protected final Component component;
//    private boolean isSpecial;
    private final List<Connector> connectors = new ArrayList<>();
    private String open;
    private String close;


    private String crewColorOpen;

    protected CliComponent(Component component) {
        this.component = component;
    }

    public static CliComponent of(Component component) {
        return switch (component) {
            case Battery battery -> new CliBattery(battery);
            case Cabin cabin -> new CliCabin(cabin);
            case Cannon cannon -> new CliCannon(cannon);
            case CargoHold cargoHold -> new CliCargoHold(cargoHold);
            case Engine engine -> new CliEngine(engine);
            case LifeSupport lifeSupport -> new CliLifeSupport(lifeSupport);
            case Shield shield -> new CliShield(shield);
            case Component comp -> new CliComponent(comp);
        };
    }

    public String getConnector(int connectorDirection) {
        return switch (connectorDirection) {
            case 0 -> switch (connectors.get(0)) {
                case Connector.NONE -> "─";
                case Connector.SINGLE -> "┴";
                case Connector.DOUBLE -> "╨";
                case Connector.UNIVERSAL -> "╩";
            };
            case 1 -> switch (connectors.get((1 + component.getOrientation()) % 4)) {
                case Connector.NONE -> "│";
                case Connector.SINGLE -> "├";
                case Connector.DOUBLE -> "╞";
                case Connector.UNIVERSAL -> "╠";
            };
            case 2 -> switch (connectors.get((2 + component.getOrientation()) % 4)) {
                case Connector.NONE -> "─";
                case Connector.SINGLE -> "┬";
                case Connector.DOUBLE -> "╥";
                case Connector.UNIVERSAL -> "╦";
            };
            case 3 -> switch (connectors.get((3 + component.getOrientation()) % 4)) {
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
        return generateborders("   ");
    };

    protected List<String> generateborders(String middle) {

        open = Highlights.RESET.getHighlight();
        close = Highlights.RESET.getHighlight();

        connectors.addAll(component.getConnectors());

        List<String> lines = new ArrayList<>();
            lines.add(0, open+"╭─" + getConnector(0) + "─╮"+close);
            lines.add(1, open + getConnector(3) + middle + getConnector(1)+close);
            lines.add(2, open+"╰─" + getConnector(2) + "─╯"+close);
        return lines;
    }

//    private static List<Connector> parseConnectors(JsonNode connectorNode){
//        List<Connector> connectors = new ArrayList<>();
//        if(connectorNode != null && connectorNode.isArray()){
//            for (JsonNode conn : connectorNode){
//                connectors.add(Connector.valueOf(conn.asText().toUpperCase()));
//            }
//        }
//        return connectors;
//    }

//
//    public void setGoods(List<GoodsType> goods) {
//         componentProperty.get().cargoProperty().set(goods);
//    }
//
//    public void setCrewType(CrewType crewType) {
//
//    }
}
