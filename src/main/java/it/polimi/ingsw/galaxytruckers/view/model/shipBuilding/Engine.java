package it.polimi.ingsw.galaxytruckers.view.model.shipBuilding;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Connector;
import it.polimi.ingsw.galaxytruckers.view.Direction;

import java.util.List;
import java.util.Map;

public sealed class Engine extends Component permits DoubleEngine{
    public Engine(Map<Direction, Connector> connectors, int id) {
        super(connectors, id);
    }

    public boolean isValid() {
        return getOrientation() == Direction.UP;
    }

    public int getEnginePower() {
        return 1;
    }

}
