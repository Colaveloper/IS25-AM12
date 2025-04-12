package it.polimi.ingsw.galaxytruckers.model.adventureCards.projectiles;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Connector;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import javafx.scene.image.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.function.IntSupplier;

import static org.junit.jupiter.api.Assertions.*;

class BigFireTest {
    BigFire bigFire;
    ShipBoard shipBoard;
    Point position;

    @BeforeEach
    void setUp() {
        shipBoard = new ShipBoard(null, null) {
            @Override
            protected boolean containsPoint(Point point) {
                return false;
            }

        };
        position = new Point();
    }

    @Test
    void getActivatablePointsReturnsEmptySet() {
        bigFire = new BigFire(0);

        assertTrue(bigFire.getActivatablePoints(shipBoard).isEmpty());
    }

    @Test
    void getComponentToRemoveReturnsFirstFoundComponentPosition() {
        class RiggedProjectile extends BigFire {
            public RiggedProjectile(IntSupplier dice, int direction) {
                super(dice, direction);
            }

            @Override
            public Optional<Point> getFirstFoundComponentPosition(ShipBoard shipBoard) {
                return Optional.of(position);
            }
        };

        Projectile projectile = new RiggedProjectile(()->0, 0);

        assertEquals(Optional.of(position), projectile.getComponentPositionToRemove(shipBoard));
    }
}