package it.polimi.ingsw.galaxytruckers.client.model.shipBuilding;

import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.Connector;
import it.polimi.ingsw.galaxytruckers.shared.enums.Direction;

import java.util.Map;

/**
 * A standard cannon component on the spaceship.
 * This sealed class permits only DoubleCannon as its subclass.
 */
public sealed class Cannon extends Component permits DoubleCannon {

    /**
     * Constructs a new Cannon component.
     *
     * @param connectors Map of directional connectors for this component
     * @param id Unique identifier for this component
     */
    public Cannon(Map<Direction, Connector> connectors, int id) {
        super(connectors, id);
    }

    /**
     * Calculates the firepower of this cannon based on its orientation.
     * A cannon facing upward (Direction.UP) provides 2 units of firepower,
     * while cannons in other orientations provide 1 unit.
     *
     * @return 2 if the cannon is facing upward, 1 otherwise
     */
    public int getFirePower() {
        return (getOrientation() == Direction.UP) ? 2 : 1;
    }
}
