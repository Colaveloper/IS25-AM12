package it.polimi.ingsw.galaxytruckers.view.model.shipBuilding;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Connector;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;

import java.util.List;
import java.util.Map;

public final class Battery extends Component {
    int numBatteries;

    public Battery(Map<Direction, Connector> connectors, int id, int numBatteries) {
        super(connectors,id);
        this.numBatteries = numBatteries;
    }

    public int getNumBatteries() {
        return numBatteries;
    }

    public void removeBattery() {
        this.numBatteries--;
    }
}
