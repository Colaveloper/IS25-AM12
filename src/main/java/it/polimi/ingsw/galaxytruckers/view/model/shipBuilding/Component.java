package it.polimi.ingsw.galaxytruckers.view.model.shipBuilding;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Connector;
import it.polimi.ingsw.galaxytruckers.view.Direction;

import java.util.Map;

public sealed class Component permits
        Battery,
        Cabin,
        Cannon,
        CargoHold,
        Engine,
        LifeSupport,
        Shield {

    private final Map<Direction, Connector> connectors;
    private Direction orientation;
    private final int id;

    public Component(Map<Direction, Connector> connectors, int id) {
        this.connectors = connectors;
        this.orientation = Direction.UP;
        this.id = id;
    }

    public Map<Direction, Connector> getConnectors() {
        return connectors;
    }

    public int getId() {
        return id;
    }

    public Direction getOrientation() {
        return orientation;
    }

    public void setOrientation(Direction orientation) {
        this.orientation = orientation;
    }
}
