package it.polimi.ingsw.galaxytruckers.view.model.shipBuilding;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Connector;

import java.util.List;

public final class Cabin extends Component {
    private CrewType crewType;
    private int numResidents;

    public Cabin(List<Connector> connectors, int id) {
        super(connectors,id);
        this.crewType = CrewType.HUMAN;
        this.numResidents = 0;
    }

    public CrewType getCrewType() {
        return crewType;
    }

    public void setCrewType(CrewType crewType) {
        this.crewType = crewType;
    }

    public int getNumResidents() {
        return numResidents;
    }

    public void setNumResidents(int numResidents) {
        this.numResidents = numResidents;
    }

}
