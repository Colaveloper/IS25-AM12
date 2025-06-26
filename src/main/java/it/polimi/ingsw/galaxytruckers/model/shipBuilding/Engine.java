package it.polimi.ingsw.galaxytruckers.model.shipBuilding;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.view.Direction;

import java.util.List;
import java.util.Map;

/**
 * Represents an engine component in the ship, providing propulsion.
 */
public non-sealed class Engine extends Component implements ComponentInterface{
    /**
     * Constructs an Engine with the specified connectors and id.
     *
     * @param connectors the connectors for this component
     * @param id the unique identifier for this component
     */
    public Engine(Map<Direction, Connector> connectors, int id) {
        super(connectors, id);
    }

    /**
     * Constructs an Engine with the specified connectors.
     * Used for testing purposes.
     *
     * @param connectors the connectors for this component
     */
    @VisibleForTesting
    public Engine(Map<Direction, Connector> connectors) {
        super(connectors);
    }

    /**
     * Constructs an Engine with default connectors.
     * Used for testing purposes.
     */
    @VisibleForTesting
    public Engine() {
        super();
    }

    /**
     * Checks if the engine is valid based on its orientation.
     *
     * @return true if the engine is oriented upwards (UP), false otherwise.
     */
    public boolean isValid() {
        return getOrientation() == Direction.UP;
    }

    /**
     * Returns the engine power of this engine component.
     *
     * @return the engine power
     */
    public int getEnginePower() {
        return 1;
    }
}
