package it.polimi.ingsw.galaxytruckers.model.adventureCards.projectiles;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.*;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Component;
import javafx.scene.image.Image;
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

    Cannon positionTestCannon;
    Point firstFoundComponentPosition;
    DoubleCannon activeDoubleCannon;

    Cannon directedCannon;
    Map.Entry<Point, Cannon> cannon00;
    Map.Entry<Point, Cannon> cannon02;
    Map.Entry<Point, Cannon> cannon20;
    Map<Point, Cannon> cannonPositions;

    Cannon impossibleCannon;
    BigMeteor impossibleBigMeteor;

    Cannon protectingCannon;

    // doublecannon
    // cannon-activable= singleCannon
    // cannon-activable-getFirepower>0 = inactive cannon
    // cannon-activable-getFirepower-isEffective = not effective active cannon

    @BeforeEach
    void setUp() {

    }

    @Test
    void getActivatablePointsReturnsEffectiveActiveDoubleCannons() {
        singleCannon = new Cannon(null);
        inactiveDoubleCannon = new DoubleCannon( null);
        ineffectiveActiveDoubleCannon = new DoubleCannon( null) {
            @Override
            public int getFirePower() {
                return 1;
            }
        };
        effectiveActiveDoubleCannon = new DoubleCannon( null) {
            @Override
            public int getFirePower() {
                return 1;
            }
        };
        shipBoard = new ShipBoard(null) {
            @Override
            protected boolean containsPoint(Point point) {
                return false;
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
        bigMeteor = new BigMeteor(0) {
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
        protectingCannon = new DoubleCannon( null) {
            @Override
            public int getFirePower() {
                return 0;
            }
        };
        shipBoard = new ShipBoard(null) {
            @Override
            protected boolean containsPoint(Point point) {
                return false;
            }

            @Override
            public Map<Point, Cannon> getCannons() {
                return Map.of(new Point(1, 0), protectingCannon);
            }
        };
        bigMeteor = new BigMeteor(0) {
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
        protectingCannon = new DoubleCannon( null) {
            @Override
            public int getFirePower() {
                return 1;
            }
        };
        shipBoard = new ShipBoard(null) {
            @Override
            protected boolean containsPoint(Point point) {
                return false;
            }

            @Override
            public Map<Point, Cannon> getCannons() {
                return Map.of(new Point(1, 0), protectingCannon);
            }
        };
        bigMeteor = new BigMeteor(0) {
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
    void cannonPositionIsEffectiveThrowsExceptionForUnknownDirection() {
        impossibleBigMeteor = new BigMeteor(4);
        impossibleCannon = new Cannon( null) {
            @Override
            public int getOrientation() {
                return 4;
            }
        };
        assertThrows(IllegalArgumentException.class, () -> {
            impossibleBigMeteor.cannonPositionIsEffective(
                    new AbstractMap.SimpleEntry<>(
                            new Point(0, 0), impossibleCannon
                    )
            );
        });
    }

    @Test
    void cannonPositionIsEffectiveForMatchingOrientationAndDirection() {
        directedCannon = new Cannon( null);
        for (int i = 0; i < 4; i++) {
            int finalI = i;
            directedCannon = new Cannon( null) {
                @Override
                public int getOrientation() {
                    return finalI;
                }
            };
            bigMeteor = new BigMeteor(() -> 0, finalI);
            assertTrue(bigMeteor.cannonPositionIsEffective(new AbstractMap.SimpleEntry<>(
                    new Point(0, 0), directedCannon
            )));
        }
    }

    @Test
    void cannonPositionIsNotEffectiveWhenDirectionNotMatchingOrientation() {
        directedCannon = new Cannon( null) {
            @Override
            public int getOrientation() {
                return 0;
            }
        };
        bigMeteor = new BigMeteor(() -> 0, 1);
        assertFalse(bigMeteor.cannonPositionIsEffective(new AbstractMap.SimpleEntry<>(
                new Point(0, 0), directedCannon
        )));
    }

    @Test
    void cannonPositionIsNotEffectiveWhenTooMuchOnOneSide() {
        // FROM ABOVE
        directedCannon = new Cannon( null) {
            @Override
            public int getOrientation() {
                return 0;
            }
        };
        bigMeteor = new BigMeteor(() -> 0, 0);
        assertFalse(bigMeteor.cannonPositionIsEffective(new AbstractMap.SimpleEntry<>(
                new Point(1, 0), directedCannon
        )));

        // FROM SIDE
        directedCannon = new Cannon( null) {
            @Override
            public int getOrientation() {
                return 1;
            }
        };
        bigMeteor = new BigMeteor(() -> 0, 1);
        assertTrue(bigMeteor.cannonPositionIsEffective(new AbstractMap.SimpleEntry<>(
                new Point(0, 1), directedCannon
        )));
        assertFalse(bigMeteor.cannonPositionIsEffective(new AbstractMap.SimpleEntry<>(
                new Point(0, 2), directedCannon
        )));

        // FROM BEHIND
        directedCannon = new Cannon( null) {
            @Override
            public int getOrientation() {
                return 2;
            }
        };
        bigMeteor = new BigMeteor(() -> 0, 2);
        assertTrue(bigMeteor.cannonPositionIsEffective(new AbstractMap.SimpleEntry<>(
                new Point(1, 0), directedCannon
        )));
        assertFalse(bigMeteor.cannonPositionIsEffective(new AbstractMap.SimpleEntry<>(
                new Point(2, 0), directedCannon
        )));
    }


//    @Test
//    void cannonPositionIsEffective() {
//        directedCannon = new Cannon( null);
//        cannon00 = new AbstractMap.SimpleEntry<>(new Point(0, 0), directedCannon);
//        cannon02 = new AbstractMap.SimpleEntry<>(new Point(0, 2), directedCannon);
//        cannon20 = new AbstractMap.SimpleEntry<>(new Point(2, 0), directedCannon);
//        cannonPositions = new HashMap<Point, Cannon>(Map.ofEntries(cannon00, cannon02, cannon20));
//
//        directedCannon = new Cannon( null) {
//            @Override
//            public int getOrientationProperty() {
//                return 0;
//            }
//        };
//        bigMeteor = new BigMeteor(()->0,0);
//        assertTrue(bigMeteor.cannonPositionIsEffective(cannon00));
//        assertTrue(bigMeteor.cannonPositionIsEffective(cannon02));
//        assertFalse(bigMeteor.cannonPositionIsEffective(cannon20));
//
//        directedCannon = new Cannon( null) {
//            @Override
//            public int getOrientationProperty() {
//                return 1;
//            }
//        };
//        bigMeteor = new BigMeteor(()->0,1);
//        assertTrue(bigMeteor.cannonPositionIsEffective(cannon00));
//        assertTrue(bigMeteor.cannonPositionIsEffective(cannon02));
//        assertFalse(bigMeteor.cannonPositionIsEffective(cannon20));
//
//        bigMeteor = new BigMeteor(()->0,1);
//        assertFalse(bigMeteor.cannonPositionIsEffective(cannon00));
//
//        bigMeteor = new BigMeteor(()->0,2);
//        assertFalse(bigMeteor.cannonPositionIsEffective(cannon00));
//
//
//
//
//
//
//        for(Map.Entry<Point, Cannon> e : cannonPositions.entrySet()) {
//
//        }
//
//
//    }
}