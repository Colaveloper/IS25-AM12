package it.polimi.ingsw.galaxytruckers.view.model.shipBuilding;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Connector;
import it.polimi.ingsw.galaxytruckers.view.Direction;

import java.util.Map;

/**
 * Base class for all client ship components.
 * This sealed class defines the common properties and behaviors shared by all ship components,
 * restricting its implementation to specific component types: Battery, Cabin, Cannon,
 * CargoHold, Engine, LifeSupport, and Shield.
 */
public sealed class Component permits
        Battery,
        Cabin,
        Cannon,
        CargoHold,
        Engine,
        LifeSupport,
        Shield {

    /** Map of connectors that define how this component connects to adjacent components in each direction */
    private final Map<Direction, Connector> connectors;
    /** Current orientation of the component on the ship */
    private Direction orientation;
    /** Unique identifier for this component instance */
    private final int id;

    /**
     * Constructs a new component with specified connectors and ID.
     * The component is initialized with an upward orientation.
     *
     * @param connectors Map defining connection points for each direction
     * @param id Unique identifier for this component
     */
    public Component(Map<Direction, Connector> connectors, int id) {
        this.connectors = connectors;
        this.orientation = Direction.UP;
        this.id = id;
    }

    /**
     * Gets the connector configuration for this component.
     *
     * @return Map of directional connectors
     */
    public Map<Direction, Connector> getConnectors() {
        return connectors;
    }

    /**
     * Gets the unique identifier of this component.
     *
     * @return The component's unique ID
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the current orientation of this component.
     *
     * @return The direction this component is facing
     */
    public Direction getOrientation() {
        return orientation;
    }

    /**
     * Sets the orientation of this component.
     *
     * @param orientation The new direction for this component to face
     */
    public void setOrientation(Direction orientation) {
        this.orientation = orientation;
    }

    /**
     * Get the actual connector for a given orientation, accounting for the component's rotation.
     *
     * @param direction The requested orientation
     * @return The connector in that orientation after rotation
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
