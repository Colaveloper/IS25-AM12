package it.polimi.ingsw.galaxytruckers.view.cliElements.CliComponents;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Connector;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliElement;
import it.polimi.ingsw.galaxytruckers.view.enums.CliHighlights;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents a component on the ship board in the CLI.
 */
public class CliComponent extends CliElement {

    private final Component component;
    private final Map<Direction, Connector> connectors;
    private String open;
    private String close;

    /**
     * Constructor for CliComponent. Used by subclasses when constructed in {@link CliComponent#of(Component)}.
     *
     * @param component The component to be represented in the CLI
     */
    protected CliComponent(Component component) {
        this.component = component;
        this.connectors = component.getConnectors();
        open = CliHighlights.RESET.getHighlight();
        close = CliHighlights.RESET.getHighlight();
    }

    /**
     * Factory method to create a CliComponent based on the type of Component.
     * The switch is based on the sealed class Component.
     *
     * @param component The component to be represented in the CLI
     * @return A CliComponent or its subclass based on the type of the component
     */
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

    /**
     * Returns the connector character for the specified direction.
     * The character is determined based on the component's rotated connector in that direction.
     *
     * @param direction The direction for which to get the connector character
     * @return A string representing the connector character for the specified direction
     */
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

    /**
     * Returns the ID of the component.
     * This ID is used to identify the component in the game state.
     *
     * @return The unique identifier of the component
     */
    public int getId() {
        return component.getId();
    }

    /**
     * Highlights the component with the specified color.
     * This method sets the open and close strings to the highlight color.
     *
     * @param color The color to use for highlighting
     */
    public void highlight(CliHighlights color) {
        open = color.getHighlight();
        close = CliHighlights.RESET.getHighlight();
        setDirty();
    }

    @Override
    protected List<String> getNewDescription() {
        return addBorders("   ");
    }

    /**
     * Adds borders to the middle string and returns a list of strings representing the component.
     * The borders are added based on the connectors in the specified directions.
     *
     * @param middle The string to be placed in the middle of the component representation
     * @return A list of strings representing the component with borders
     */
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
