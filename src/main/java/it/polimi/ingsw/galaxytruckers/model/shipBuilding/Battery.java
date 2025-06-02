package it.polimi.ingsw.galaxytruckers.model.shipBuilding;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.view.Direction;

import java.util.List;
import java.util.Map;

public class Battery extends Component{
    int numBatteries;

    public Battery(Map<Direction, Connector> connectors, int id, int numBatteries) {
        super(connectors,id);
        if (numBatteries != 2 && numBatteries != 3) {
            throw new IllegalArgumentException("Number of batteries must be 2 or 3");
        }
        this.numBatteries = numBatteries;
    }

    @VisibleForTesting
    public Battery(Map<Direction, Connector> connectors, int numBatteries) {
        super(connectors);
        if (numBatteries != 2 && numBatteries != 3) {
            throw new IllegalArgumentException("Number of batteries must be 2 or 3");
        }
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
