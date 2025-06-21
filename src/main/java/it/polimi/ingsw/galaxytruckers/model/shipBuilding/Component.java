package it.polimi.ingsw.galaxytruckers.model.shipBuilding;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.view.Direction;

import java.util.*;

public non-sealed class Component implements ComponentInterface {
    private final Map<Direction, Connector> connectors;
    private Direction orientation;
    private final int id;

    public Component(Map<Direction, Connector> connectors, int id) {
        this.connectors = new EnumMap<>(connectors);
        this.orientation = Direction.UP;
        this.id = id;
    }

    @VisibleForTesting
    public Component(Map<Direction, Connector> connectors) {
        this.connectors = new EnumMap<>(connectors);
        this.orientation = Direction.UP;
        this.id = 0;
    }

    @VisibleForTesting
    public Component() {
        this.connectors = new HashMap<>();
        this.connectors.putAll(Map.of(
                Direction.UP, Connector.UNIVERSAL,
                Direction.RIGHT, Connector.UNIVERSAL,
                Direction.DOWN, Connector.UNIVERSAL,
                Direction.LEFT, Connector.UNIVERSAL
        ));
        this.orientation = Direction.UP;
        this.id = 0;
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
        Map<Direction, Connector> rotatedConnectors = Direction.rotateDirectionMap(connectors, this.orientation, orientation);
        this.orientation = orientation;
        this.connectors.putAll(rotatedConnectors);
    }

    public void addToVisitor(ComponentVisitor visitor) {
    }

    public void removeFromVisitor(ComponentVisitor visitor) {
    }
}
