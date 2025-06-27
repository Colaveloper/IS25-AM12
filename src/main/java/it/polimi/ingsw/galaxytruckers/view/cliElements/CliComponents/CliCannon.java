package it.polimi.ingsw.galaxytruckers.view.cliElements.CliComponents;

import it.polimi.ingsw.galaxytruckers.view.Direction;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Cannon;

import java.util.List;
import java.util.Map;

/**
 * Represents a CLI component for a Cannon in the ship.
 */
public class CliCannon extends CliComponent {

    private final Cannon cannon;
    private static final Map<Direction, String> symbols = Map.of(
            Direction.UP, "△",
            Direction.RIGHT, "▷",
            Direction.DOWN, "▽",
            Direction.LEFT,"◁"
    );

    /**
     * Constructs a CLI representation of a Cannon component.
     *
     * @param component the Cannon component to be represented in the CLI
     */
    public CliCannon(Cannon component) {
        super(component);
        this.cannon = component;
    }

    @Override
    protected List<String> getNewDescription() {
        return addBorders(" " + symbols.get(cannon.getOrientation()) + " ");
    }
}

