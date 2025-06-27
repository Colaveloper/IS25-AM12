package it.polimi.ingsw.galaxytruckers.client.model.shipBuilding;

import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.Connector;
import it.polimi.ingsw.galaxytruckers.shared.enums.Direction;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EngineTest {
    private Engine createEngineWithOrientation(Direction orientation) {
        Map<Direction, Connector> connectors = new EnumMap<>(Direction.class);
        Engine engine = new Engine(connectors, 1);
        engine.setOrientation(orientation);
        return engine;
    }

    @Test
    void isValidWhenUpOrientationReturnsTrue() {
        Engine engine = createEngineWithOrientation(Direction.UP);
        assertTrue(engine.isValid());
    }

    @Test
    void isValidWhenNotUpOrientationReturnsFalse() {
        for (Direction dir : Direction.values()) {
            if (dir != Direction.UP) {
                Engine engine = createEngineWithOrientation(dir);
                assertFalse(engine.isValid(), "Should be false for " + dir);
            }
        }
    }

    @Test
    void getEnginePowerAlwaysReturnsOne() {
        Engine engine = createEngineWithOrientation(Direction.UP);
        assertEquals(1, engine.getEnginePower());
        engine.setOrientation(Direction.DOWN);
        assertEquals(1, engine.getEnginePower());
    }

    @Test
    void setAndGetOrientationWorksCorrectly() {
        Engine engine = createEngineWithOrientation(Direction.LEFT);
        assertEquals(Direction.LEFT, engine.getOrientation());
        engine.setOrientation(Direction.RIGHT);
        assertEquals(Direction.RIGHT, engine.getOrientation());
    }
}