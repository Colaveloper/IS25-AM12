package it.polimi.ingsw.galaxytruckers.model.shipBuilding;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import org.checkerframework.common.returnsreceiver.qual.This;

import java.awt.*;
import java.util.Map;

public non-sealed class Battery extends Component implements ComponentInterface{
    int numBatteries;

    public Battery(Map<Direction, Connector> connectors, int id, int numBatteries) {
        super(connectors,id);
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

    @VisibleForTesting
    public Battery(int numBatteries) {
        super();
        this.numBatteries = numBatteries;
    }

    public int getNumBatteries() {
        return numBatteries;
    }

    /**
     * Decreases the number of batteries by one.
     * @throws IllegalArgumentException if there are no more batteries left.
     */
    public void useBatteries() throws IllegalArgumentException {
        if (this.numBatteries <= 0) {
            throw new IllegalArgumentException("There are no more batteries here");
        }
        this.numBatteries--;
    }

    @Override
    public void addToVisitor(ComponentVisitor visitor, Point point) {
        visitor.add(this, point);
    }

    @Override
    public void removeFromVisitor(ComponentVisitor visitor, Point point) {
        visitor.remove(this, point);
    }
}
