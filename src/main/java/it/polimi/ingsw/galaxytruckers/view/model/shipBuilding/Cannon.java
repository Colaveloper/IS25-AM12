package it.polimi.ingsw.galaxytruckers.view.model.shipBuilding;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Connector;
import it.polimi.ingsw.galaxytruckers.view.Direction;

import java.util.List;
import java.util.Map;

public sealed class Cannon extends Component permits DoubleCannon {

    public Cannon(Map<Direction, Connector> connectors, int id) {
        super(connectors, id);
    }

    public int getFirePower() {
        return (getOrientation() == Direction.UP) ? 2 : 1;
    }

}
