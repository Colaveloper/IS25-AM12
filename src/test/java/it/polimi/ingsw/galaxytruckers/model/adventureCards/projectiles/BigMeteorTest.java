package it.polimi.ingsw.galaxytruckers.model.adventureCards.projectiles;

import it.polimi.ingsw.galaxytruckers.model.SecondShipBoardForTesting;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.*;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import it.polimi.ingsw.galaxytruckers.view.enums.ProjectileType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class BigMeteorTest {
    BigMeteor bigMeteor;
    ShipBoard shipBoard;
    Cannon singleCannon;
    DoubleCannon inactiveDoubleCannon;
    DoubleCannon ineffectiveActiveDoubleCannon;
    DoubleCannon effectiveActiveDoubleCannon;

    Point firstFoundComponentPosition;
    Cannon directedCannon;
    Cannon protectingCannon;

    // doublecannon
    // cannon-activable= singleCannon
    // cannon-activable-getFirepower>0 = inactive cannon
    // cannon-activable-getFirepower-isEffective = not effective active cannon

    @BeforeEach
    void setUp() {

    }

    @Test
    void getProjectileType() {
        assertEquals(ProjectileType.BIGMETEOR, new BigMeteor(Direction.UP).getProjectileType());
    }

    @Test
    void getActivatablePointsReturnsEffectiveActiveDoubleCannons() {
        singleCannon = new Cannon(new EnumMap<>(Direction.class));
        inactiveDoubleCannon = new DoubleCannon( new EnumMap<>(Direction.class));
        ineffectiveActiveDoubleCannon = new DoubleCannon( new EnumMap<>(Direction.class)) {
            @Override
            public int getFirePower() {
                return 1;
            }
        };
        effectiveActiveDoubleCannon = new DoubleCannon( new EnumMap<>(Direction.class)) {
            @Override
            public int getFirePower() {
                return 1;
            }
        };
        shipBoard = new SecondShipBoardForTesting(GameColor.BLUE) {
            @Override
            protected boolean containsPoint(Point point) {
                return true;
            }

            @Override
            public Map<Point, Cannon> getCannons() {
                return Map.of(
                        new Point(1, 0), singleCannon,
                        new Point(2, 0), inactiveDoubleCannon,
                        new Point(3, 0), ineffectiveActiveDoubleCannon,
                        new Point(4, 0), effectiveActiveDoubleCannon
                );
            }

            @Override
            public Map<Point, Activatable> getActivatables() {
                return Map.of(
                        new Point(2, 0), inactiveDoubleCannon,
                        new Point(3, 0), ineffectiveActiveDoubleCannon,
                        new Point(4, 0), effectiveActiveDoubleCannon
                );
            }
        };
        bigMeteor = new BigMeteor(Direction.UP) {
            @Override
            protected boolean cannonPositionIsEffective(Map.Entry<Point, Cannon> e) {
                if (e.getKey().equals(new Point(4, 0))) return true;
                return false;
            }
        };
        assertEquals(Set.of(new Point(4, 0)), bigMeteor.getActivatablePoints(shipBoard));
    }

    @Test
    void getComponentToRemoveReturnsFirstFoundComponentPositionIfThereIsEffectiveCannonWithNoFirePower() {
        firstFoundComponentPosition = new Point();
        protectingCannon = new DoubleCannon( new EnumMap<>(Direction.class)) {
            @Override
            public int getFirePower() {
                return 0;
            }
        };
        shipBoard = new SecondShipBoardForTesting(GameColor.BLUE) {
            @Override
            protected boolean containsPoint(Point point) {
                return true;
            }

            @Override
            public Map<Point, Cannon> getCannons() {
                return Map.of(new Point(1, 0), protectingCannon);
            }
        };
        bigMeteor = new BigMeteor(Direction.UP) {
            @Override
            protected boolean cannonPositionIsEffective(Map.Entry<Point, Cannon> e) {
                return true;
            }

            @Override
            protected Optional<Point> getFirstFoundComponentPosition(ShipBoard shipBoard) {
                return Optional.of(firstFoundComponentPosition);
            }
        };
        assertEquals(Optional.of(firstFoundComponentPosition), bigMeteor.getComponentPositionToRemove(shipBoard));
    }

    @Test
    void getComponentPositionToRemoveReturnsEmptyIfThereIsEffectiveCannonWithFirePower() {
        firstFoundComponentPosition = new Point();
        protectingCannon = new DoubleCannon( new EnumMap<>(Direction.class)) {
            @Override
            public int getFirePower() {
                return 1;
            }
        };
        shipBoard = new SecondShipBoardForTesting(GameColor.BLUE) {
            @Override
            protected boolean containsPoint(Point point) {
                return true;
            }

            @Override
            public Map<Point, Cannon> getCannons() {
                return Map.of(new Point(1, 0), protectingCannon);
            }
        };
        bigMeteor = new BigMeteor(Direction.UP) {
            @Override
            protected boolean cannonPositionIsEffective(Map.Entry<Point, Cannon> e) {
                return true;
            }

            @Override
            protected Optional<Point> getFirstFoundComponentPosition(ShipBoard shipBoard) {
                return Optional.of(firstFoundComponentPosition);
            }
        };
        assertTrue(bigMeteor.getComponentPositionToRemove(shipBoard).isEmpty());
    }


    @Test
    void cannonPositionIsEffectiveForMatchingOrientationAndDirection() {
        directedCannon = new Cannon( new EnumMap<>(Direction.class));
        for (Direction direction : Direction.values()) {
            directedCannon = new Cannon( new EnumMap<>(Direction.class)) {
                @Override
                public Direction getOrientation() {
                    return direction;
                }
            };
            bigMeteor = new BigMeteor(() -> 0, direction);
            assertTrue(bigMeteor.cannonPositionIsEffective(new AbstractMap.SimpleEntry<>(
                    new Point(0, 0), directedCannon
            )));
        }
    }

    @Test
    void cannonPositionIsNotEffectiveWhenDirectionNotMatchingOrientation() {
        directedCannon = new Cannon( new EnumMap<>(Direction.class)) {
            @Override
            public Direction getOrientation() {
                return Direction.UP;
            }
        };
        bigMeteor = new BigMeteor(() -> 0, Direction.RIGHT);
        assertFalse(bigMeteor.cannonPositionIsEffective(new AbstractMap.SimpleEntry<>(
                new Point(0, 0), directedCannon
        )));
    }

    @Test
    void cannonPositionIsNotEffectiveWhenTooMuchOnOneSide() {
        // FROM ABOVE
        directedCannon = new Cannon( new EnumMap<>(Direction.class)) {
            @Override
            public Direction getOrientation() {
                return Direction.UP;
            }
        };
        bigMeteor = new BigMeteor(() -> 0, Direction.UP);
        assertFalse(bigMeteor.cannonPositionIsEffective(new AbstractMap.SimpleEntry<>(
                new Point(1, 0), directedCannon
        )));

        // FROM SIDE
        directedCannon = new Cannon( new EnumMap<>(Direction.class)) {
            @Override
            public Direction getOrientation() {
                return Direction.RIGHT;
            }
        };
        bigMeteor = new BigMeteor(() -> 0, Direction.RIGHT);
        assertTrue(bigMeteor.cannonPositionIsEffective(new AbstractMap.SimpleEntry<>(
                new Point(0, 1), directedCannon
        )));
        assertFalse(bigMeteor.cannonPositionIsEffective(new AbstractMap.SimpleEntry<>(
                new Point(0, 2), directedCannon
        )));

        // FROM BEHIND
        directedCannon = new Cannon( new EnumMap<>(Direction.class)) {
            @Override
            public Direction getOrientation() {
                return Direction.DOWN;
            }
        };
        bigMeteor = new BigMeteor(() -> 0, Direction.DOWN);
        assertTrue(bigMeteor.cannonPositionIsEffective(new AbstractMap.SimpleEntry<>(
                new Point(1, 0), directedCannon
        )));
        assertFalse(bigMeteor.cannonPositionIsEffective(new AbstractMap.SimpleEntry<>(
                new Point(2, 0), directedCannon
        )));
    }
}