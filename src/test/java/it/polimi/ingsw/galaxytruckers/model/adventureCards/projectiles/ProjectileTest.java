package it.polimi.ingsw.galaxytruckers.model.adventureCards.projectiles;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import javafx.scene.image.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.IntSupplier;

import static org.junit.jupiter.api.Assertions.*;

class ProjectileTest {
    ShipBoard myShipBoard;
    Component up, right, down, left;
    Point upp, rightp, downp, leftp;

    @BeforeEach
    void setUp() {
        up = new Component(null);
        right = new Component(null);
        down = new Component(null);
        left = new Component(null);
        upp = new Point(0, -1);
        rightp = new Point(1, 0);
        downp = new Point(0, 1);
        leftp = new Point(-1, 0);

        myShipBoard = new ShipBoard(null) {

            @Override
            protected boolean containsPoint(Point point) {
                return false;
            }

            @Override
            public Map<Point, Component> getComponentMap() {
                return Map.of(
                        upp, up,
                        rightp, right,
                        downp, down,
                        leftp, left
                );
            }
        };
    }

    @Test
    void getFirstFoundComponentPosition() {
        // makes getFirstFoundComponent public for testing
        class TransparentProjectile extends Projectile {
            public TransparentProjectile(IntSupplier dice, int direction) {
                super(dice, direction);
            }

            @Override
            public Set<Point> getActivatablePoints(ShipBoard shipBoard) {
                return Set.of();
            }

            public Optional<Point> getFirstFoundComponentPosition(ShipBoard shipBoard) {
                return super.getFirstFoundComponentPosition(shipBoard);
            }

            @Override
            protected Optional<Point> getComponentPositionToRemove(ShipBoard shipBoard) {
                return Optional.empty();
            }
        }
        assertEquals(Optional.of(upp), new TransparentProjectile(()->0, 0).getFirstFoundComponentPosition(myShipBoard));
        assertEquals(Optional.of(rightp), new TransparentProjectile(()->0, 1).getFirstFoundComponentPosition(myShipBoard));
        assertEquals(Optional.of(downp), new TransparentProjectile(()->0, 2).getFirstFoundComponentPosition(myShipBoard));
        assertEquals(Optional.of(leftp), new TransparentProjectile(()->0, 3).getFirstFoundComponentPosition(myShipBoard));

        Projectile badProjectile = new TransparentProjectile(()->0, 4);

        assertThrows(
                IllegalArgumentException.class,
                () -> badProjectile.getFirstFoundComponentPosition(myShipBoard)
        );
    }

    @Test
    void fireAtRemovesComponentIfPresent() {
        Point targetComponentPosition = new Point();
        final boolean[] getsRemoved = {false};

        class TargetedProjectile extends Projectile{
            public TargetedProjectile(int direction) {
                super(direction);
            }

            @Override
            public Set<Point> getActivatablePoints(ShipBoard shipBoard) {
                return Set.of();
            }

            @Override
            protected Optional<Point> getComponentPositionToRemove(ShipBoard shipBoard) {
                return Optional.of(targetComponentPosition);
            }
        }

        ShipBoard myShipBoard = new ShipBoard(null) {
            @Override
            protected boolean containsPoint(Point point) {
                return false;
            }

            @Override
            public void discardComponent(Point point) {
                getsRemoved[0] = true;
            }
        };

        TargetedProjectile targetedProjectile = new TargetedProjectile(0);

        assertTrue(targetedProjectile.fireAt(myShipBoard));
        assertTrue(getsRemoved[0]);
    }
}