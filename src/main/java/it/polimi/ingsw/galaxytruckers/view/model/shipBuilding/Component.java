package it.polimi.ingsw.galaxytruckers.view.model.shipBuilding;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Connector;

import java.util.ArrayList;
import java.util.List;

public sealed class Component permits
        Battery,
        Cabin,
        Cannon,
        CargoHold,
        DoubleCannon,
        DoubleEngine,
        Engine,
        LifeSupport,
        Shield
{
    private final List<Connector> connectors;
    private int orientation;
    private final int id;

    public Component(List<Connector> connectors, int id) {
        this.connectors = connectors;
        this.orientation = 0;
        this.id = id;
    }

    public List<Connector> getConnectors() {
        List<Connector> res = new ArrayList<>();
        for (int i = 0; i < connectors.size(); i++) {
            res.add(connectors.get((i-orientation+connectors.size())%connectors.size()));
        }
        return res;
    }

    public int getId() {
        return id;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }
}
