package it.polimi.ingsw.galaxytruckers.client.model.shipBuilding;

import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.Connector;
import it.polimi.ingsw.galaxytruckers.shared.enums.Direction;

import java.util.Map;

/**
 * Represents a double engine component. They implement the Activatable
 * interface to manage their power state.
 */
public final class DoubleEngine extends Engine implements Activatable {
    private boolean active;

    public DoubleEngine(Map<Direction, Connector> connectors, int id) {
        super(connectors, id);
        this.active = false;
    }

    /**
     * Gets the total engine power provided by this double engine.
     * When activated, it provides double the normal power of a standard engine.
     *
     * @return 2 if facing down and activated, or normal engine power (i.e., 0) if not activated
     */
    @Override
    public int getEnginePower() {
        if (this.active) {
            return 2;
        } else {
            return 0;
        }
    }

    /**
     * Checks if the double engine is currently activated.
     *
     * @return true if the engine is powered up, false otherwise
     */
    @Override
    public boolean isActive() {
        return this.active;
    }

    /**
     * Sets the activation state of the double engine.
     *
     * @param active true to activate the engine's enhanced power, false to deactivate
     */
    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

}
