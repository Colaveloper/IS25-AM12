package it.polimi.ingsw.galaxytruckers.view.cliElements.CliComponents;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Connector;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliElement;
import it.polimi.ingsw.galaxytruckers.view.enums.CliHighlights;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CliComponent extends CliElement {

    private final Component component;
    private final Map<Direction, Connector> connectors;
    private String open;
    private String close;

    protected CliComponent(Component component) {
        this.component = component;
        this.connectors = component.getConnectors();
        open = CliHighlights.RESET.getHighlight();
        close = CliHighlights.RESET.getHighlight();
    }

    public static CliComponent of(Component component) {
        return switch (component) {
            case DoubleCannon doubleCannon-> new CliDoubleCannon(doubleCannon);
            case DoubleEngine doubleEngine -> new CliDoubleEngine(doubleEngine);
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

    public String getConnector(Direction direction) {
        Connector connector = component.getRotatedConnector(direction);
        return switch (direction) {
            case UP -> switch (connector) {
                case NONE -> "─";
                case SINGLE -> "┴";
                case DOUBLE -> "╨";
                case UNIVERSAL -> "╩";
            };
            case LEFT -> switch (connector) {
                case NONE -> "│";
                case SINGLE -> "┤";
                case DOUBLE -> "╡";
                case UNIVERSAL -> "╣";
            };
            case DOWN -> switch (connector) {
                case NONE -> "─";
                case SINGLE -> "┬";
                case DOUBLE -> "╥";
                case UNIVERSAL -> "╦";
            };
            case RIGHT -> switch (connector) {
                case NONE -> "│";
                case SINGLE -> "├";
                case DOUBLE -> "╞";
                case UNIVERSAL -> "╠";
            };
        };
    }


    public int getId() {
        return component.getId();
    }

    public void highlight(CliHighlights color) {
        open = color.getHighlight();
        close = CliHighlights.RESET.getHighlight();
        setDirty();
    }

    @Override
    protected List<String> getNewDescription() {
        return addBorders("   ");
    }

    protected List<String> addBorders(String middle) {
        //open = CliHighlights.RESET.getHighlight();
        close = CliHighlights.RESET.getHighlight();

        List<String> lines = new ArrayList<>();
            lines.add(0, open+"╭─" + getConnector(Direction.UP) + "─╮"+close);
            lines.add(1, open+ getConnector(Direction.LEFT) + close + middle + open + getConnector(Direction.RIGHT)+close);
            lines.add(2, open+"╰─" + getConnector(Direction.DOWN) + "─╯"+close);
        return lines;
    }
}
