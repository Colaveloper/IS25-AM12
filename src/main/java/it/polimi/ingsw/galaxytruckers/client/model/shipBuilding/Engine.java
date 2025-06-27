package it.polimi.ingsw.galaxytruckers.client.model.shipBuilding;

import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.Connector;
import it.polimi.ingsw.galaxytruckers.shared.enums.Direction;

import java.util.Map;

/**
 * Represents a standard engine component on the spaceship.
 * This sealed class permits only DoubleEngine as its subclass.
 */
public sealed class Engine extends Component permits DoubleEngine{
    public Engine(Map<Direction, Connector> connectors, int id) {
        super(connectors, id);
    }

    public boolean isValid() {
        return getOrientation() == Direction.UP;
    }

    /**
     * Gets the engine power provided by this engine.
     * Base engines provide 1 unit of power when facing downward.
     *
     * @return The amount of engine power provided by this component
     */
    public int getEnginePower() {
        return 1;
    }

}
