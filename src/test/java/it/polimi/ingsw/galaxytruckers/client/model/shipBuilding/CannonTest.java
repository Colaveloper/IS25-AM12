package it.polimi.ingsw.galaxytruckers.client.model.shipBuilding;

import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.Connector;
import it.polimi.ingsw.galaxytruckers.shared.enums.Direction;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CannonTest {
    Map<Direction, Connector> connectors = new EnumMap<>(Direction.class);
    Cannon testCannon = new Cannon(connectors, 1);

    @Test
    void getFirePower() {
        // test cannon facing up, should return 2
        testCannon.setOrientation(Direction.UP);
        assertEquals(2, testCannon.getFirePower(), "Cannon facing UP should have firepower 2");

        // test cannon facing right, should return 1
        testCannon.setOrientation(Direction.RIGHT);
        assertEquals(1, testCannon.getFirePower(), "Cannon facing RIGHT should have firepower 1");

        //test cannon facing left, should return 1
        testCannon.setOrientation(Direction.LEFT);
        assertEquals(1, testCannon.getFirePower(), "Cannon facing LEFT should have firepower 1");
    }
}