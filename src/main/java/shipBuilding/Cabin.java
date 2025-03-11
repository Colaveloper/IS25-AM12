package shipBuilding;

import java.util.List;

public class Cabin extends Component{
    private CrewType crewType;
    private int numResidents;

    public Cabin(List<Connector> connectors) {
        super(connectors);
        this.numResidents = 0;
        this.crewType = null;
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

    public void loseResidents(int numResidents) throws IllegalArgumentException {
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
