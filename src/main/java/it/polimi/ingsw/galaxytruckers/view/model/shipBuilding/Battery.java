package it.polimi.ingsw.galaxytruckers.view.model.shipBuilding;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Connector;

import java.util.List;

public final class Battery extends Component {
    int numBatteries;

    public Battery(List<Connector> connectors, int id, int numBatteries) {
        super(connectors,id);
        this.numBatteries = numBatteries;
    }

    public int getNumBatteries() {
        return numBatteries;
    }

    public void setNumBatteries(int numBatteries) {
        this.numBatteries = numBatteries;
    }
}
