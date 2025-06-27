package it.polimi.ingsw.galaxytruckers.client.model.shipBuilding;

import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.Connector;
import it.polimi.ingsw.galaxytruckers.shared.enums.Direction;

import java.util.Map;

/**
 * Represents a life support system component on the spaceship.
 */
public final class LifeSupport extends Component {
    private final CrewType crewType;

    public LifeSupport(Map<Direction, Connector> connectors, int id, CrewType crewType) {
        super(connectors, id);
        this.crewType = crewType;
    }

    public CrewType getAlienType() {
        return crewType;
    }

}
