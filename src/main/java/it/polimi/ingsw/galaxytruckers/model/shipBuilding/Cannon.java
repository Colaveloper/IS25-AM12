package it.polimi.ingsw.galaxytruckers.model.shipBuilding;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.view.Direction;

import java.util.List;
import java.util.Map;

/**
 * Represents a cannon component in the ship, providing firepower.
 */
public non-sealed class Cannon extends Component implements ComponentInterface{

    /**
     * Constructs a Cannon with the specified connectors and id.
     *
     * @param connectors the connectors for this component
     * @param id the unique identifier for this component
     */
    public Cannon(Map<Direction, Connector> connectors, int id) {
        super(connectors, id);
    }

    /**
     * Constructs a Cannon with the specified connectors.
     * Used for testing purposes.
     *
     * @param connectors the connectors for this component
     */
    @VisibleForTesting
    public Cannon(Map<Direction, Connector> connectors) {
        super(connectors);
    }

    /**
     * Constructs a Cannon with default connectors.
     * Used for testing purposes.
     */
    @VisibleForTesting
    public Cannon() {
        super();
    }

    /**
     * Returns the cannon's firepower based on its orientation.
     *
     * @return the firepower of the cannon as 2*nominal firepower to account for
     * halved firepower when the cannon is not oriented upwards.
     */
    public int getFirePower() {
        return (getOrientation() == Direction.UP) ? 2 : 1;
    }
}
