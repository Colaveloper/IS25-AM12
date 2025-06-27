package it.polimi.ingsw.galaxytruckers.client.model.shipBuilding;

import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.Connector;
import it.polimi.ingsw.galaxytruckers.shared.enums.Direction;

import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class ComponentTest {

    @Test
    public void testConstructorAndGetters() {
        Map<Direction, Connector> connectors = new EnumMap<>(Direction.class);
        connectors.put(Direction.UP, Connector.SINGLE);
        connectors.put(Direction.RIGHT, Connector.DOUBLE);
        connectors.put(Direction.DOWN, Connector.UNIVERSAL);
        connectors.put(Direction.LEFT, Connector.NONE);
        int id = 42;
        Component c = new Cabin(connectors, id);
        assertEquals(connectors, c.getConnectors());
        assertEquals(id, c.getId());
        assertEquals(Direction.UP, c.getOrientation());
    }

    @Test
    public void testSetAndGetOrientation() {
        Component c = new Cabin(new EnumMap<>(Direction.class), 1);
        c.setOrientation(Direction.LEFT);
        assertEquals(Direction.LEFT, c.getOrientation());
        c.setOrientation(Direction.DOWN);
        assertEquals(Direction.DOWN, c.getOrientation());
    }

    @Test
    public void getRotatedConnectorNoRotation() {
        // no rotation means UP since it's the default orientation
        Map<Direction, Connector> connectors = new EnumMap<>(Direction.class);
        connectors.put(Direction.UP, Connector.SINGLE);
        connectors.put(Direction.RIGHT, Connector.DOUBLE);
        connectors.put(Direction.DOWN, Connector.UNIVERSAL);
        connectors.put(Direction.LEFT, Connector.NONE);
        Component c = new Cabin(connectors, 1);
        assertEquals(Connector.SINGLE, c.getRotatedConnector(Direction.UP));
        assertEquals(Connector.DOUBLE, c.getRotatedConnector(Direction.RIGHT));
        assertEquals(Connector.UNIVERSAL, c.getRotatedConnector(Direction.DOWN));
        assertEquals(Connector.NONE, c.getRotatedConnector(Direction.LEFT));
    }

    @Test
    public void getRotatedConnectorRightRotation() {
        Map<Direction, Connector> connectors = new EnumMap<>(Direction.class);
        connectors.put(Direction.UP, Connector.SINGLE);
        connectors.put(Direction.RIGHT, Connector.DOUBLE);
        connectors.put(Direction.DOWN, Connector.UNIVERSAL);
        connectors.put(Direction.LEFT, Connector.NONE);
        Component c = new Cabin(connectors, 1);
        c.setOrientation(Direction.RIGHT);
        // After rotating right, the mapping is:
        // getRotatedConnector(UP) -> connectors.get(LEFT) -> NONE
        // getRotatedConnector(RIGHT) -> connectors.get(UP) -> SINGLE
        // getRotatedConnector(DOWN) -> connectors.get(RIGHT) -> DOUBLE
        // getRotatedConnector(LEFT) -> connectors.get(DOWN) -> UNIVERSAL
        assertEquals(Connector.NONE, c.getRotatedConnector(Direction.UP));
        assertEquals(Connector.SINGLE, c.getRotatedConnector(Direction.RIGHT));
        assertEquals(Connector.DOUBLE, c.getRotatedConnector(Direction.DOWN));
        assertEquals(Connector.UNIVERSAL, c.getRotatedConnector(Direction.LEFT));
    }

    @Test
    public void getRotatedConnectorDownRotation() {
        Map<Direction, Connector> connectors = new EnumMap<>(Direction.class);
        connectors.put(Direction.UP, Connector.SINGLE);
        connectors.put(Direction.RIGHT, Connector.DOUBLE);
        connectors.put(Direction.DOWN, Connector.UNIVERSAL);
        connectors.put(Direction.LEFT, Connector.NONE);
        Component c = new Cabin(connectors, 1);
        c.setOrientation(Direction.DOWN);
        // After rotating down, UP->DOWN, RIGHT->LEFT, DOWN->UP, LEFT->RIGHT
        assertEquals(Connector.UNIVERSAL, c.getRotatedConnector(Direction.UP));
        assertEquals(Connector.NONE, c.getRotatedConnector(Direction.RIGHT));
        assertEquals(Connector.SINGLE, c.getRotatedConnector(Direction.DOWN));
        assertEquals(Connector.DOUBLE, c.getRotatedConnector(Direction.LEFT));
    }

    @Test
    public void getRotatedConnectorLeftRotation() {
        Map<Direction, Connector> connectors = new EnumMap<>(Direction.class);
        connectors.put(Direction.UP, Connector.SINGLE);
        connectors.put(Direction.RIGHT, Connector.DOUBLE);
        connectors.put(Direction.DOWN, Connector.UNIVERSAL);
        connectors.put(Direction.LEFT, Connector.NONE);
        Component c = new Cabin(connectors, 1);
        c.setOrientation(Direction.LEFT);
        // After rotating left, the mapping is:
        // getRotatedConnector(UP) -> connectors.get(RIGHT) -> DOUBLE
        // getRotatedConnector(RIGHT) -> connectors.get(DOWN) -> UNIVERSAL
        // getRotatedConnector(DOWN) -> connectors.get(LEFT) -> NONE
        // getRotatedConnector(LEFT) -> connectors.get(UP) -> SINGLE
        assertEquals(Connector.DOUBLE, c.getRotatedConnector(Direction.UP));
        assertEquals(Connector.UNIVERSAL, c.getRotatedConnector(Direction.RIGHT));
        assertEquals(Connector.NONE, c.getRotatedConnector(Direction.DOWN));
        assertEquals(Connector.SINGLE, c.getRotatedConnector(Direction.LEFT));
    }
}