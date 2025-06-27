package it.polimi.ingsw.galaxytruckers.server.model.shipBuilding;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.shared.enums.Direction;

import java.util.Map;

/**
 * Represents a double engine component in the ship, which can be activated to provide increased engine power.
 */
public non-sealed class DoubleEngine extends Engine implements Activatable{
    private boolean active;

    /**
     * Constructs a DoubleEngine with the specified connectors and id.
     *
     * @param connectors the connectors for this component
     * @param id the unique identifier for this component
     */
    public DoubleEngine(Map<Direction, Connector> connectors, int id) {
        super(connectors, id);
        this.active = false;
    }

    /**
     * Constructs a DoubleEngine with the specified connectors.
     * Used for testing purposes.
     *
     * @param connectors the connectors for this component
     */
    @VisibleForTesting
    public DoubleEngine(Map<Direction, Connector> connectors) {
        super(connectors);
        this.active = false;
    }

    /**
     * Constructs a DoubleEngine with default universal connectors.
     * Used for testing purposes.
     */
    @VisibleForTesting
    public DoubleEngine() {
        super();
        this.active = false;
    }

    /**
     * {@inheritDoc}
     * @return 0 if the engine is not active, otherwise behaves like
     * a normal engine with doubled power
     */
    @Override
    public int getEnginePower() {
        if (this.active) {
            return 2;
        } else {
            return 0;
        }
    }

    @Override
    public void activate() {
        this.active = true;
    }

    @Override
    public void deactivate() {
        this.active = false;
    }

    @Override
    public boolean isActive() {
        return this.active;
    }
}
