package it.polimi.ingsw.galaxytruckers.model.shipBuilding;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.view.Direction;

import java.util.List;
import java.util.Map;

/**
 * Represents a double cannon component in the ship, which can be activated to provide increased firepower.
 * The firepower is doubled when oriented upwards.
 */
public non-sealed class DoubleCannon extends Cannon implements Activatable {
    private boolean active;

    /**
     * Constructs a DoubleCannon with the specified connectors and id.
     *
     * @param connectors the connectors for this component
     * @param id the unique identifier for this component
     */
    public DoubleCannon(Map<Direction, Connector> connectors, int id) {
        super(connectors, id);
        this.active = false;
    }

    /**
     * Constructs a DoubleCannon with the specified connectors.
     * Used for testing purposes.
     *
     * @param connectors the connectors for this component
     */
    @VisibleForTesting
    public DoubleCannon(Map<Direction, Connector> connectors) {
        super(connectors);
        this.active = false;
    }

    /**
     * Constructs a DoubleCannon with default connectors.
     * Used for testing purposes.
     */
    @VisibleForTesting
    public DoubleCannon() {
        super();
        this.active = false;
    }

    /**
     * {@inheritDoc}
     *
     * @return 0 if the cannon is not active, otherwise behaves like
     * a normal cannon with doubled firepower
     */
    @Override
    public int getFirePower() {
        int power = this.active ? 2 : 0;
        if (getOrientation() == Direction.UP) return power*2;
        else return power;
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
