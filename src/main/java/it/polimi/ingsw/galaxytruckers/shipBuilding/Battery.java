package it.polimi.ingsw.galaxytruckers.shipBuilding;

import java.util.List;

public class Battery extends Component{
    int numBatteries;

    public Battery(List<Connector> connectors, int numBatteries) {
        super(connectors);
        this.numBatteries = numBatteries;
    }

    public int getNumBatteries() {
        return numBatteries;
    }

    public void useBatteries(int numBatteries) throws IllegalArgumentException {
        if (numBatteries > this.numBatteries) {
            throw new IllegalArgumentException("The number of requested batteries is greater that the number of available batteries");
        } else if (numBatteries < 1) {
            throw new IllegalArgumentException("The number of requested batteries is not positive");
        }
        this.numBatteries -= numBatteries;
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
