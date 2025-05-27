package it.polimi.ingsw.galaxytruckers.view.model.shipBuilding;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Connector;

import java.util.List;

public sealed class Cannon extends Component permits DoubleCannon {

    public Cannon(List<Connector> connectors, int id) {
        super(connectors, id);
    }

    public int getFirePower() {
        return (getOrientationProperty() == 0) ? 2 : 1;
    }

}
