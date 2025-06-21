package it.polimi.ingsw.galaxytruckers.model.shipBuilding;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.view.Direction;

import java.util.Map;

public non-sealed class Cabin extends Component implements ComponentInterface{
    private CrewType crewType;
    private int numResidents;

    public Cabin(Map<Direction, Connector> connectors, int id) {
        super(connectors,id);
        this.crewType = CrewType.HUMAN;
        this.numResidents = 0;
    }

    @VisibleForTesting
    public Cabin(Map<Direction, Connector> connectors) {
        super(connectors);
        this.crewType = CrewType.HUMAN;
        this.numResidents = 0;
    }

    @VisibleForTesting
    public Cabin() {
        super();
        this.crewType = CrewType.HUMAN;
        this.numResidents = 0;
    }

    public void initialize(CrewType crewType) {
        this.crewType = crewType;
        if (this.crewType == CrewType.HUMAN) {
            this.numResidents = 2;
        } else {
            this.numResidents = 1;
        }
    }

    public int getNumResidents() {
        return numResidents;
    }

    public CrewType getCrewType() {
        return crewType;
    }

    public void loseResidents() {
        if (this.numResidents <= 0) {
            throw new IllegalStateException("There are no more residents here");
        }
        this.numResidents--;
    }

    @Override
    public void addToVisitor(ComponentVisitor visitor) {
        visitor.add(this);
    }

    @Override
    public void removeFromVisitor(ComponentVisitor visitor) {
        visitor.remove(this);
    }
}
