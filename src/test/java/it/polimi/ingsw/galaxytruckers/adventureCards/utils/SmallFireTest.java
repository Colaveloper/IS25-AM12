package it.polimi.ingsw.galaxytruckers.adventureCards.utils;

import it.polimi.ingsw.galaxytruckers.shipBuilding.Cannon;
import it.polimi.ingsw.galaxytruckers.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.shipBuilding.Shield;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;
import javafx.scene.image.Image;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SmallFireTest {
    ShipBoard shipBoard;
    SmallFire smallFire;
    Component firstFoundComponent;
    Map<Point, Shield> fakeShields;

    @Test
    void getActivatablePoints() {
        fakeShields = new HashMap<>();
        shipBoard = new ShipBoard(null) {
            @Override
            protected boolean containsPoint(Point point) {
                return false;
            }

            @Override
            public Image getImage() {
                return null;
            }

            @Override
            public Map<Point, Shield> getShields() {
                return fakeShields;
            }
        };
        smallFire = new SmallFire(()->0, 0);
        assertEquals(fakeShields.keySet(), smallFire.getActivatablePoints(shipBoard));
    }

    @Test
    void getComponentToRemoveReturnsEmptyIfProjectileComesFromShieldDirectionElseFirstFound() {
        firstFoundComponent = new Component(null, null);
        for (int i = 0; i < 4; i++) {
            int finalI = i;
            shipBoard = new ShipBoard(null) {
                @Override
                protected boolean containsPoint(Point point) {
                    return false;
                }

                @Override
                public Image getImage() {
                    return null;
                }

                @Override
                public boolean[] getShieldDirections() {
                    boolean[] directions = new boolean[4]; // Default values are false
                    directions[finalI] = true;
                    return directions;
                }
            };
            for (int j=0; j<4; j++) {
                smallFire = new SmallFire(j) {
                    @Override
                    protected Optional<Component> getFirstFoundComponent(ShipBoard shipBoard) {
                        return Optional.of(firstFoundComponent);
                    }
                };
                if (i==j) {
                    assertTrue(smallFire.getComponentToRemove(shipBoard).isEmpty());
                } else {
                    assertEquals(firstFoundComponent, smallFire.getComponentToRemove(shipBoard).get());
                }
            }
        }
    }
}