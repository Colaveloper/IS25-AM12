package it.polimi.ingsw.galaxytruckers.server.model.shipBuilding;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.shared.enums.Direction;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Base class for all ship components, providing connector and orientation management.
 */
public non-sealed class Component implements ComponentInterface {
    private final Map<Direction, Connector> connectors;
    private Direction orientation;
    private final int id;

    /**
     * Constructs a Component with the specified connectors and id.
     *
     * @param connectors the connectors for this component
     * @param id         the unique identifier for this component
     */
    public Component(Map<Direction, Connector> connectors, int id) {
        this.connectors = new EnumMap<>(connectors);
        this.orientation = Direction.UP;
        this.id = id;
    }

    /**
     * Constructs a Component with the specified connectors.
     * Used for testing purposes.
     *
     * @param connectors the connectors for this component
     */
    @VisibleForTesting
    public Component(Map<Direction, Connector> connectors) {
        this.connectors = new EnumMap<>(connectors);
        this.orientation = Direction.UP;
        this.id = 0;
    }

    /**
     * Constructs a Component with default universal connectors.
     * Used for testing purposes.
     */
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

    /**
     * @return a map of connectors for this component, keyed by direction.
     */
    public Map<Direction, Connector> getConnectors() {
        return connectors;
    }

    /**
     * @return the unique identifier of the component
     */
    public int getId() {
        return id;
    }

    /**
     * @return the current orientation of the component
     */
    public Direction getOrientation() {
        return orientation;
    }

    /**
     * Sets the orientation of the component.
     *
     * @param orientation the new orientation to set
     */
    public void setOrientation(Direction orientation) {
        Map<Direction, Connector> rotatedConnectors = Direction.rotateDirectionMap(connectors, this.orientation, orientation);
        this.orientation = orientation;
        this.connectors.putAll(rotatedConnectors);
    }
}
