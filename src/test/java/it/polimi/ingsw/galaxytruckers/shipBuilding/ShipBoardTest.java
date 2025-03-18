package it.polimi.ingsw.galaxytruckers.shipBuilding;

import it.polimi.ingsw.galaxytruckers.enumTypes.Colors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShipBoardTest {

    ShipBoard shipBoard;

    @Nested
    @DisplayName("Ship-building tests")
    class ShipBuildingTest {
        Component componentToAdd;

        @BeforeEach
        void setup() {
            ComponentBank componentBank = new ComponentBank() {
                @Override
                public Component getRanComponent() {
                    return componentToAdd;
                }
            };
            shipBoard = new SecondShipBoard(componentBank, Colors.BLUE);
            componentToAdd = new Component(Arrays.asList(Connector.UNIVERSAL, Connector.SINGLE, Connector.DOUBLE, Connector.NONE));
        }

        @Test
        void placeWithoutWeldDoesNotUpdateMap() {
            Map<Point, Component> prevMap = new HashMap<>(shipBoard.getComponentMap());
            shipBoard.requestRandComponent();
            shipBoard.placeComponent(new Point(7,7));
            assertEquals(prevMap, shipBoard.getComponentMap());
        }

        @Test
        void placeWithWeldDoesUpdateMap() {
            Map<Point, Component> expectedMap = new HashMap<>(shipBoard.getComponentMap());
            Point point = new Point(7,7);
            expectedMap.put(point, componentToAdd);
            shipBoard.requestRandComponent();
            shipBoard.placeComponent(point);
            shipBoard.weldLastComponent();
            assertEquals(expectedMap, shipBoard.getComponentMap());
        }

        @Test
        void placeWithoutComponentThrowsException() {
            assertThrows(IllegalStateException.class, () -> shipBoard.placeComponent(new Point(7,7)));
        }

        @Test
        void placeWithIllegalPositionThrowsException() {
            assertThrows(IllegalStateException.class, () -> shipBoard.placeComponent(new Point(-1,-1)));
        }

        @Test
        void placeWithTakenPositionThrowsException() {
            shipBoard.requestRandComponent();
            shipBoard.placeComponent(new Point(7,7));
            shipBoard.weldLastComponent();
            assertThrows(IllegalStateException.class, () -> shipBoard.placeComponent(new Point(7,7)));
        }

        @Test
        void weldWithoutPositionThrowsException() {
            shipBoard.requestRandComponent();
            assertThrows(IllegalStateException.class, () -> shipBoard.weldLastComponent());
        }

        @Test
        void componentIsRotated() {
            List<Connector> expectedConnectors = componentToAdd.getConnectors();
            Point point = new Point(7,7);
            Collections.rotate(expectedConnectors, 1);
            shipBoard.requestRandComponent();
            shipBoard.placeComponent(point);
            shipBoard.rotateComponent();
            shipBoard.weldLastComponent();
            assertEquals(expectedConnectors, shipBoard.getComponentMap().get(point).getConnectors());
        }

        @Test
        void rotateWithoutComponentThrowsException() {
            assertThrows(IllegalStateException.class, () -> shipBoard.rotateComponent());
        }

        @Test
        void componentIsStashed() {
            shipBoard.requestRandComponent();
            shipBoard.stashComponent();
            assertTrue(shipBoard.getStashedComponents().contains(componentToAdd));
        }

        @Test
        void stashWhenLimitIsReachedThrowsException() {
            shipBoard.requestRandComponent();
            shipBoard.stashComponent();
            shipBoard.requestRandComponent();
            shipBoard.stashComponent();
            shipBoard.requestRandComponent();
            assertThrows(IllegalStateException.class, () -> shipBoard.stashComponent());
        }

        @Test
        void stashedComponentIsGrabbed() {
            shipBoard.requestRandComponent();
            shipBoard.stashComponent();
            assertFalse(shipBoard.getLastComponent().isPresent());
            shipBoard.grabStashedComponent(0);
            assertEquals(componentToAdd, shipBoard.getLastComponent().orElse(null));
        }

        @Test
        void grabStashedComponentOutOfBoundsThrowsException() {
            shipBoard.requestRandComponent();
            shipBoard.stashComponent();
            assertThrows(IndexOutOfBoundsException.class, () -> shipBoard.grabStashedComponent(1));
        }
    }

    @Nested
    @DisplayName("Cannon tests")
    class CannonTests {
        Cannon componentToAdd;

        @BeforeEach
        void setup() {
            componentToAdd = new Cannon(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL));
            ComponentBank componentBank = new ComponentBank() {
                @Override
                public Component getRanComponent() {
                    return componentToAdd;
                }
            };
            shipBoard = new SecondShipBoard(componentBank, Colors.BLUE);
        }

        void addCannon(Point point) {
            shipBoard.requestRandComponent();
            shipBoard.placeComponent(point);
            shipBoard.weldLastComponent();
        }

        void removeCannon(Point point) {
            shipBoard.removeComponent(point);
        }

        boolean restIsUnchanged() {
            return shipBoard.getActivatables().isEmpty() &&
                    shipBoard.getCargoHolds().isEmpty() &&
                    shipBoard.getCabins().isEmpty() &&
                    shipBoard.getBatteries().isEmpty() &&
                    shipBoard.getEngines().isEmpty() &&
                    shipBoard.getLifeSupports().isEmpty() &&
                    shipBoard.getStashedComponents().isEmpty() &&
                    shipBoard.getCredits() == 0;
        }

        @Test
        void addCannonUpdatesMap() {
            addCannon(new Point(7,7));
            assertEquals(shipBoard.getCannons().get(new Point(7,7)), componentToAdd);
            assertEquals(1, shipBoard.getCannons().size());
            assertTrue(restIsUnchanged());
        }

        @Test
        void removeCannonUpdatesMap() {
            addCannon(new Point(7,7));
            removeCannon(new Point(7,7));
            assertTrue(shipBoard.getCannons().isEmpty());
            assertTrue(restIsUnchanged());
        }

        @Test
        void getFirePowerEqualsCannonFirePowerSum() {
            assertEquals(0, shipBoard.getFirePower());
            for (int i = 6; i <= 8; i++) {
                addCannon(new Point(i,7));
            }
            assertEquals(3*componentToAdd.getFirePower(), shipBoard.getFirePower());
            assertTrue(restIsUnchanged());
        }
    }

    @Nested
    @DisplayName("getConnectedSets() tests")
    class GetConnectedSetsTest {
        @BeforeEach
        void setUp() {
            ComponentBank bank = new ComponentBank() {
                @Override
                public Component getRanComponent() {
                    return new Component(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL));
                }
            };
            shipBoard = new SecondShipBoard(bank, Colors.BLUE);
            for (int i = 5; i <= 9; i++) {
                shipBoard.requestRandComponent();
                shipBoard.placeComponent(new Point(i,7));
            }
            shipBoard.requestRandComponent();
            shipBoard.placeComponent(new Point(7,6));
            shipBoard.requestRandComponent();
            shipBoard.placeComponent(new Point(7,8));
            shipBoard.requestRandComponent();
        }

        @Test
        void connectedSetsForConnectedShipsAre1() {
            assertEquals(1, shipBoard.getConnectedSets().size());
        }

        @Test
        void shipHas4differentConnectedSets() {
            shipBoard.removeComponent(new Point(7,7));

            assertEquals(4, shipBoard.getConnectedSets().size());
        }

        @Test
        void connectedSetsHaveTheRightMembers() {
            shipBoard.removeComponent(new Point(7,7));
            List<Set<Point>> expectedSets = new ArrayList<>();
            for (int i = 0; i < 4; i++) {expectedSets.add(new HashSet<>());}
            expectedSets.get(0).add(new Point(5,7));
            expectedSets.get(0).add(new Point(6,7));
            expectedSets.get(1).add(new Point(8,7));
            expectedSets.get(1).add(new Point(9,7));
            expectedSets.get(2).add(new Point(7,8));
            expectedSets.get(3).add(new Point(7,6));
            for (Set<Point> set : expectedSets) {
                assertTrue(shipBoard.getConnectedSets().contains(set));
            }
        }
    }




}