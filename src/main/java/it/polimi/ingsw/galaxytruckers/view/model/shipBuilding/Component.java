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

    /**
     * Get the actual connector for a given direction, accounting for the component's rotation.
     *
     * @param direction The requested direction
     * @return The connector in that direction after rotation
     */
    public Connector getRotatedConnector(Direction direction) {
        int steps = 0;
        Direction currentDir = Direction.UP;

        while (currentDir != orientation) {
            currentDir = currentDir.getRight();
            steps++;
        }

        Direction rotatedDirection = direction;
        for (int i = 0; i < steps; i++) {
            rotatedDirection = rotatedDirection.getLeft();
        }

        return connectors.get(rotatedDirection);
    }
}
