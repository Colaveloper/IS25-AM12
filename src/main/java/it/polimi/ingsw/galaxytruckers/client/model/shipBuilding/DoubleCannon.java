package it.polimi.ingsw.galaxytruckers.client.model.shipBuilding;

import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.Connector;
import it.polimi.ingsw.galaxytruckers.shared.enums.Direction;

import java.util.Map;

/**
 * A double cannon component on the spaceship.
 */
public final class DoubleCannon extends Cannon implements Activatable {
    private boolean active;

    public DoubleCannon(Map<Direction, Connector> connectors, int id) {
        super(connectors, id);
        this.active = false;
    }

    /**
     * Gets the total firepower provided by this double cannon.
     * When activated, provides double the normal firepower of a standard cannon.
     *
     * @return 4 if facing up and activated, 2 if facing another direction and activated,
     *         or normal cannon firepower (i.e., 0) if not activated
     */
    @Override
    public int getFirePower() {
        int power = this.active ? 2 : 0;
        if (getOrientation() == Direction.UP) return power*2;
        else return power;
    }

    /**
     * Checks if the double cannon is currently activated.
     *
     * @return true if the cannon is powered up, false otherwise
     */
    @Override
    public boolean isActive() {
        return this.active;
    }

    /**
     * Sets the activation state of the double cannon.
     *
     * @param active true to activate the cannon's enhanced firepower, false to deactivate
     */
    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

}
