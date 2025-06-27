package it.polimi.ingsw.galaxytruckers.client.model.shipBuilding;

import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.Connector;
import it.polimi.ingsw.galaxytruckers.shared.enums.Direction;

import java.util.Map;

/**
 * Represents a battery component on the spaceship.
 */
public final class Battery extends Component {
    /** The number of battery units available in this component */
    int numBatteries;

    /**
     * Constructs a new Battery component.
     *
     * @param connectors   Map of directional connectors for this component
     * @param id          Unique identifier for this component
     * @param numBatteries Initial number of battery units available
     */
    public Battery(Map<Direction, Connector> connectors, int id, int numBatteries) {
        super(connectors,id);
        this.numBatteries = numBatteries;
    }

    /**
     * Gets the current number of battery units available.
     *
     * @return The number of battery units remaining
     */
    public int getNumBatteries() {
        return numBatteries;
    }

    /**
     * Removes one battery unit from this component.
     * Called when a battery is used to power ship systems.
     */
    public void removeBattery() {
        this.numBatteries--;
    }

    /**
     * Sets the number of battery units in this component.
     *
     * @param numBatteries The new number of battery units
     */
    public void setNumBatteries(int numBatteries) {
        this.numBatteries = numBatteries;
    }
}
