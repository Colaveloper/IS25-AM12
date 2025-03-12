package it.polimi.ingsw.galaxytruckers.shipBuilding;

import java.util.List;
import java.util.Optional;

public class Cabin extends Component {
    private Optional<CrewType> crewType;
    private int numResidents;

    public Cabin(List<Connector> connectors) {
        super(connectors);
        this.crewType = Optional.empty();
        this.numResidents = 0;
    }

    public void initialize(CrewType crewType) {
        this.crewType = Optional.of(crewType);
        if (this.crewType.get() == CrewType.HUMAN) {
            this.numResidents = 2;
        } else {
            this.numResidents = 1;
        }
    }

    public int getNumResidents() {
        if (!crewType.isPresent()) {
            throw new IllegalStateException("Cabin not initialized");
        }
        return numResidents;
    }

    public CrewType getCrewType() {
        if (!crewType.isPresent()) {
            throw new IllegalStateException("Cabin not initialized");
        }
        return crewType.get();
    }

    public void loseResidents(int numResidents) {
        if (!crewType.isPresent()) {
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
