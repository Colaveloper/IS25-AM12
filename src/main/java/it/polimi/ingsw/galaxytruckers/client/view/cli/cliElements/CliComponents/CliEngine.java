package it.polimi.ingsw.galaxytruckers.client.view.cli.cliElements.CliComponents;

import it.polimi.ingsw.galaxytruckers.shared.enums.Direction;
import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.Engine;

import java.util.List;
import java.util.Map;

/**
 * Represents a CLI component for displaying an engine on the ship board.
 */
public class CliEngine extends CliComponent {

    private final Engine engine;
    private static final Map<Direction, String> symbols = Map.of(
            Direction.UP, "↓",
            Direction.RIGHT, "←",
            Direction.DOWN, "↑",
            Direction.LEFT,"→"
    );

    /**
     * Creates a new CLI representation of an Engine component.
     *
     * @param engine the Engine component to be represented
     */
    public CliEngine(Engine engine) {
        super(engine);
        this.engine = engine;
    }

    @Override
    protected List<String> getNewDescription() {
        return addBorders(" " + symbols.get(engine.getOrientation()) + " ");
    }
}