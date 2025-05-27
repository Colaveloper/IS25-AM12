package it.polimi.ingsw.galaxytruckers.view.model.shipBuilding;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Connector;

import java.util.List;

public sealed class Engine extends Component permits DoubleEngine{
    public Engine(List<Connector> connectors, int id) {
        super(connectors, id);
    }

    public boolean isValid() {
        return getOrientation() == 0;
    }

    public int getEnginePower() {
        return 1;
    }

}
