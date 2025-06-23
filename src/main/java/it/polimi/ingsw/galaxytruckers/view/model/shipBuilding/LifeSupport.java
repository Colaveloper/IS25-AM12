package it.polimi.ingsw.galaxytruckers.view.model.shipBuilding;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Connector;
import it.polimi.ingsw.galaxytruckers.view.Direction;

import java.util.List;
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
