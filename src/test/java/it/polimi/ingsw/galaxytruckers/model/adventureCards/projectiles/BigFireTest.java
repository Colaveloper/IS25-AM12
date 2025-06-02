package it.polimi.ingsw.galaxytruckers.model.adventureCards.projectiles;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.Optional;
import java.util.function.IntSupplier;

import static org.junit.jupiter.api.Assertions.*;

class BigFireTest {
    BigFire bigFire;
    ShipBoard shipBoard;
    Point position;

    @BeforeEach
    void setUp() {
        shipBoard = new ShipBoard(GameColor.BLUE) {
            @Override
            protected boolean containsPoint(Point point) {
                return true;
            }

        };
        position = new Point();
    }

    @Test
    void getActivatablePointsReturnsEmptySet() {
        bigFire = new BigFire(Direction.UP);

        assertTrue(bigFire.getActivatablePoints(shipBoard).isEmpty());
    }

    @Test
    void getComponentToRemoveReturnsFirstFoundComponentPosition() {
        class RiggedProjectile extends BigFire {
            public RiggedProjectile(IntSupplier dice, Direction direction) {
                super(dice, direction);
            }

            @Override
            public Optional<Point> getFirstFoundComponentPosition(ShipBoard shipBoard) {
                return Optional.of(position);
            }
        };

        Projectile projectile = new RiggedProjectile(()->0, Direction.UP);

        assertEquals(Optional.of(position), projectile.getComponentPositionToRemove(shipBoard));
    }
}