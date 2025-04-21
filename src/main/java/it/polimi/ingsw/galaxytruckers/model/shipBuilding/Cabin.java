package it.polimi.ingsw.galaxytruckers.model.shipBuilding;

import com.google.common.annotations.VisibleForTesting;

import java.util.List;

public class Cabin extends Component {
    private CrewType crewType;
    private int numResidents;

    public Cabin(List<Connector> connectors, int id) {
        super(connectors,id);
        this.crewType = null;
        this.numResidents = 0;
    }

    @VisibleForTesting
    public Cabin(List<Connector> connectors) {
        super(connectors);
        this.crewType = null;
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
        if (crewType == null) {
            throw new IllegalStateException("Cabin not initialized");
        }
        return numResidents;
    }

    public CrewType getCrewType() {
        if (crewType == null) {
            throw new IllegalStateException("Cabin not initialized");
        }
        return crewType;
    }

    public void loseResidents(int numResidents) {
        if (crewType == null) {
            throw new IllegalStateException("Cabin not initialized");
        }

        if (numResidents <= 0) {
            throw new IllegalArgumentException("Number of residents to remove is not positive");
        } else if (numResidents > this.numResidents) {
            throw new IllegalArgumentException("Number of residents to remove exceeds the current number of residents");
        }
        this.numResidents -= numResidents;
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
