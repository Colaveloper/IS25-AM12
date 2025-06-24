package it.polimi.ingsw.galaxytruckers.model.adventureCards.projectiles;

import it.polimi.ingsw.galaxytruckers.model.SecondShipBoardForTesting;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Shield;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import it.polimi.ingsw.galaxytruckers.view.enums.ProjectileType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class SmallFireTest {
    ShipBoard shipBoard;
    SmallFire smallFire;
    Point firstFoundComponentPosition;
    Map<Point, Shield> fakeShields;

    @BeforeEach
    void setUp() {
        smallFire = new SmallFire(() -> 0, Direction.UP);
    }

    @Test
    void getActivatablePoints() {
        fakeShields = new HashMap<>();
        shipBoard = new SecondShipBoardForTesting(GameColor.BLUE) {
            @Override
            protected boolean containsPoint(Point point) {
                return true;
            }

            @Override
            public Map<Point, Shield> getShields() {
                return fakeShields;
            }
        };
        smallFire = new SmallFire(()->0, Direction.UP);
        assertEquals(fakeShields.keySet(), smallFire.getActivatablePoints(shipBoard));
    }

    @Test
    void getProjectileType() {
        assertEquals(ProjectileType.SMALLFIRE, smallFire.getProjectileType());
    }

    @Test
    void getComponentPositionToRemoveReturnsEmptyIfProjectileComesFromShieldDirectionElseFirstFound() {
        firstFoundComponentPosition = new Point();
        for (Direction defendedDirection : Direction.values()) {
            shipBoard = new SecondShipBoardForTesting(GameColor.BLUE) {
                @Override
                protected boolean containsPoint(Point point) {
                    return true;
                }

                @Override
                public Set<Direction> getShieldDirections() {
                    return new HashSet<>(Set.of(defendedDirection));
                }
            };
            for (Direction originDirection : Direction.values()) {
                smallFire = new SmallFire(originDirection) {
                    @Override
                    protected Optional<Point> getFirstFoundComponentPosition(ShipBoard shipBoard) {
                        return Optional.of(firstFoundComponentPosition);
                    }
                };
                if (originDirection == defendedDirection) {
                    assertTrue(smallFire.getComponentPositionToRemove(shipBoard).isEmpty());
                } else {
                    assertEquals(firstFoundComponentPosition, smallFire.getComponentPositionToRemove(shipBoard).get());
                }
            }
        }
    }
}