package it.polimi.ingsw.galaxytruckers.server.model.shipBuilding;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.shared.enums.Direction;

import java.util.Map;

/**
 * Represents a battery component in the ship, used to store and provide energy.
 */
public non-sealed class Battery extends Component implements ComponentInterface{
    int numBatteries;

    /**
     * Constructs a Battery with the specified connectors, id, and number of batteries.
     *
     * @param connectors the connectors for this component
     * @param id the unique identifier for this component
     * @param numBatteries the number of batteries in this component
     */
    public Battery(Map<Direction, Connector> connectors, int id, int numBatteries) {
        super(connectors,id);
        this.numBatteries = numBatteries;
    }

    /**
     * Constructs a Battery with the specified connectors and number of batteries.
     * Used for testing purposes.
     *
     * @param connectors the connectors for this component
     * @param numBatteries the number of batteries in this component
     */
    @VisibleForTesting
    public Battery(Map<Direction, Connector> connectors, int numBatteries) {
        super(connectors);
        if (numBatteries != 2 && numBatteries != 3) {
            throw new IllegalArgumentException("Number of batteries must be 2 or 3");
        }
        this.numBatteries = numBatteries;
    }

    /**
     * Constructs a Battery with the specified number of batteries.
     * Used for testing purposes.
     *
     * @param numBatteries the number of batteries in this component
     */
    @VisibleForTesting
    public Battery(int numBatteries) {
        super();
        this.numBatteries = numBatteries;
    }

    /**
     * @return the number of batteries on this component.
     */
    public int getNumBatteries() {
        return numBatteries;
    }

    /**
     * Sets the number of batteries on this component.
     * @param numBatteries the new number of batteries
     */
    public void setNumBatteries(int numBatteries) {
        this.numBatteries = numBatteries;
    }

    /**
     * Decreases the number of batteries by one.
     * @throws IllegalArgumentException if there are no more batteries left.
     */
    public void useBatteries() throws IllegalArgumentException {
        if (this.numBatteries <= 0) {
            throw new IllegalArgumentException("There are no more batteries here");
        }
        this.numBatteries--;
    }
}
