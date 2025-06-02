package it.polimi.ingsw.galaxytruckers.model.adventureCards.projectiles;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Shield;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
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
    Point firstFoundComponentPosition;
    Map<Point, Shield> fakeShields;

    @Test
    void getActivatablePoints() {
        fakeShields = new HashMap<>();
        shipBoard = new ShipBoard(GameColor.BLUE) {
            @Override
            protected boolean containsPoint(Point point) {
                return true;
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
    void getComponentPositionToRemoveReturnsEmptyIfProjectileComesFromShieldDirectionElseFirstFound() {
        firstFoundComponentPosition = new Point();
        for (int i = 0; i < 4; i++) {
            int finalI = i;
            shipBoard = new ShipBoard(GameColor.BLUE) {
                @Override
                protected boolean containsPoint(Point point) {
                    return true;
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
                    protected Optional<Point> getFirstFoundComponentPosition(ShipBoard shipBoard) {
                        return Optional.of(firstFoundComponentPosition);
                    }
                };
                if (i==j) {
                    assertTrue(smallFire.getComponentPositionToRemove(shipBoard).isEmpty());
                } else {
                    assertEquals(firstFoundComponentPosition, smallFire.getComponentPositionToRemove(shipBoard).get());
                }
            }
        }
    }
}