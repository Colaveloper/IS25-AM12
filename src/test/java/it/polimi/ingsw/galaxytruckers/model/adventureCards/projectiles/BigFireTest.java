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
    Component component;

    @BeforeEach
    void setUp() {
        shipBoard = new ShipBoard(null, null) {
            @Override
            protected boolean containsPoint(Point point) {
                return false;
            }

            @Override
            public Image getImage() {
                return null;
            }
        };
        component = new Component(null, List.of(Connector.NONE, Connector.NONE, Connector.NONE, Connector.NONE));
    }

    @Test
    void getActivatablePointsReturnsEmptySet() {
        bigFire = new BigFire(0);

        assertTrue(bigFire.getActivatablePoints(shipBoard).isEmpty());
    }

    @Test
    void getComponentToRemoveReturnsFirstFoundComponent() {
        class RiggedProjectile extends BigFire {
            public RiggedProjectile(IntSupplier dice, int direction) {
                super(dice, direction);
            }

            @Override
            public Optional<Component> getFirstFoundComponent (ShipBoard shipBoard) {
                return Optional.of(component);
            }
        };

        Projectile projectile = new RiggedProjectile(()->0, 0);

        assertEquals(Optional.of(component), projectile.getComponentToRemove(shipBoard));
    }
}