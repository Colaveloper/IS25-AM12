package it.polimi.ingsw.galaxytruckers.view.model.shipBuilding;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Connector;

import java.util.List;

public final class LifeSupport extends Component {
    private final CrewType crewType;

    public LifeSupport(List<Connector> connectors, int id, CrewType crewType) {
        super(connectors, id);
        this.crewType = crewType;
    }

    public CrewType getAlienType() {
        return crewType;
    }

}
