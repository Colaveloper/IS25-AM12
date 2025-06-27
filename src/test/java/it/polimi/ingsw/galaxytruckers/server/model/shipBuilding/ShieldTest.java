package it.polimi.ingsw.galaxytruckers.server.model.shipBuilding;

import it.polimi.ingsw.galaxytruckers.shared.enums.Direction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ShieldTest extends ComponentTest {

    private Shield myShield;

    @BeforeEach
    void setUp() {
        super.setUp();
        myShield = new Shield(myConnectors);
    }

    @Test
    void protectedDirectionChangesWithRotation() {
        myShield.activate();
        for (Direction direction : Direction.values()) {
            myShield.setOrientation(direction);
            assertEquals(Set.of(direction, direction.getRight()), myShield.getDefensibleDirections());
        }
    }
}