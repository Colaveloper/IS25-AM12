package it.polimi.ingsw.galaxytruckers.client.view.cli.cliElements.CliComponents;

import it.polimi.ingsw.galaxytruckers.shared.enums.Direction;
import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.DoubleEngine;

import java.util.List;
import java.util.Map;

/**
 * Represents a CLI representation of a Double Engine component in the Galaxy Truckers game.
 */
public class CliDoubleEngine extends CliComponent {

    private final DoubleEngine doubleEngine;

    /**
     * Constructor for CliDoubleEngine.
     *
     * @param component the DoubleEngine component to be represented in the CLI
     */
    protected CliDoubleEngine(DoubleEngine component) {
        super(component);
        this.doubleEngine = component;
    }

    private static final Map<Direction, String> symbols = Map.of(
            Direction.UP,   "⇓",
            Direction.RIGHT,"⇐",
            Direction.DOWN, "⇑",
            Direction.LEFT, "⇒"
    );

    @Override
    protected List<String> getNewDescription() {
        return addBorders(" " + symbols.get(doubleEngine.getOrientation()) + " ");
    }
}
