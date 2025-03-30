package it.polimi.ingsw.galaxytruckers.adventureCards.projectiles;

import it.polimi.ingsw.galaxytruckers.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;
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

    @BeforeEach
    void setUp() {
        up = new Component(null, null);
        right = new Component(null, null);
        down = new Component(null, null);
        left = new Component(null, null);

        myShipBoard = new ShipBoard(null, null) {

            @Override
            protected boolean containsPoint(Point point) {
                return false;
            }

            @Override
            public Image getImage() {
                return null;
            }

            @Override
            public Map<Point, Component> getComponentMap() {
                return Map.of(
                        new Point(0, -1), up,
                        new Point(1, 0), right,
                        new Point(0, 1), down,
                        new Point(-1, 0), left
                );
            }
        };
    }

    @Test
    void getFirstFoundComponent() {
        // makes getFirstFoundComponent public for testing
        class TransparentProjectile extends Projectile{
            public TransparentProjectile(IntSupplier dice, int direction) {
                super(dice, direction);
            }

            @Override
            public Set<Point> getActivatablePoints(ShipBoard shipBoard) {
                return Set.of();
            }

            public Optional<Component> getFirstFoundComponent(ShipBoard shipBoard) {
                return super.getFirstFoundComponent(shipBoard);
            }

            @Override
            protected Optional<Component> getComponentToRemove(ShipBoard shipBoard) {
                return Optional.empty();
            }
        }
        assertEquals(Optional.of(up), new TransparentProjectile(()->0, 0).getFirstFoundComponent(myShipBoard));
        assertEquals(Optional.of(right), new TransparentProjectile(()->0, 1).getFirstFoundComponent(myShipBoard));
        assertEquals(Optional.of(down), new TransparentProjectile(()->0, 2).getFirstFoundComponent(myShipBoard));
        assertEquals(Optional.of(left), new TransparentProjectile(()->0, 3).getFirstFoundComponent(myShipBoard));

        Projectile badProjectile = new TransparentProjectile(()->0, 4);

        assertThrows(
                IllegalArgumentException.class,
                () -> badProjectile.getFirstFoundComponent(myShipBoard)
        );
    }

    @Test
    void fireAtRemovesComponentIfPresent() {
        Component targetComponent = new Component(null, null);
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
            protected Optional<Component> getComponentToRemove(ShipBoard shipBoard) {
                return Optional.of(targetComponent);
            }
        }

        ShipBoard myShipBoard = new ShipBoard(null, null) {
            @Override
            public Image getImage() {
                return null;
            }

            @Override
            protected boolean containsPoint(Point point) {
                return false;
            }

            @Override
            public void remove(Component component) {
                getsRemoved[0] = true;
            }
        };

        TargetedProjectile targetedProjectile = new TargetedProjectile(0);

        assertTrue(targetedProjectile.fireAt(myShipBoard));
        assertTrue(getsRemoved[0]);
    }
}