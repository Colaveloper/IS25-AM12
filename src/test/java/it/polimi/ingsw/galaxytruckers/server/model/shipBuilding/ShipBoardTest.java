package it.polimi.ingsw.galaxytruckers.server.model.shipBuilding;

import it.polimi.ingsw.galaxytruckers.server.model.SecondShipBoardForTesting;
import it.polimi.ingsw.galaxytruckers.server.model.TestShipBoardForTesting;
import it.polimi.ingsw.galaxytruckers.shared.enums.GameColor;
import it.polimi.ingsw.galaxytruckers.shared.enums.GoodsType;
import it.polimi.ingsw.galaxytruckers.shared.enums.Direction;
import org.junit.jupiter.api.*;

import java.awt.*;
import java.util.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShipBoardTest {
    SecondShipBoardForTesting shipBoard;

    @Nested
    @DisplayName("Ship-building tests")
    class SecondShipBoardTests {
        it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.Component componentToAdd;

        @BeforeEach
        void setup() {
            shipBoard = new SecondShipBoardForTesting(GameColor.BLUE);
            shipBoard.removeComponent(new Point(7,7));
            componentToAdd = new it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.Component(Map.of(
                    Direction.UP, Connector.UNIVERSAL,
                    Direction.LEFT, Connector.SINGLE,
                    Direction.DOWN, Connector.DOUBLE,
                    Direction.RIGHT, Connector.NONE
            ));
        }

        @Test
        void placeWithoutWeldDoesNotUpdateMap() {
            Map<Point, it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.Component> prevMap = new HashMap<>(shipBoard.getComponentMap());
            shipBoard.offerComponent(componentToAdd);
            shipBoard.placeComponent(new Point(7,7),Direction.UP);
            assertEquals(prevMap, shipBoard.getComponentMap());

        }

        @Test
        void placeWithWeldDoesUpdateMap() {
            Map<Point, it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.Component> expectedMap = new HashMap<>(shipBoard.getComponentMap());
            Point point = new Point(7,7);
            expectedMap.put(point, componentToAdd);
            shipBoard.offerComponent(componentToAdd);
            shipBoard.placeComponent(point,Direction.UP);
            shipBoard.weldLastComponent();
            assertEquals(expectedMap, shipBoard.getComponentMap());
        }

        @Test
        void placeWithoutComponentThrowsException() {
            assertThrows(IllegalStateException.class, () -> shipBoard.placeComponent(new Point(7,7),Direction.UP));
        }

        @Test
        void placeGeneratesEvent() {
            shipBoard.offerComponent(componentToAdd);
            shipBoard.placeComponent(new Point(7,7),Direction.UP);
            verify(shipBoard.getEventListener(), times(1))
                    .notifyPlaceComponentEvent(shipBoard, Direction.UP,  new Point(7,7));
        }

        @Test
        void placeWithIllegalPositionThrowsException() {
            shipBoard.offerComponent(componentToAdd);
            assertThrows(IllegalArgumentException.class, () -> shipBoard.placeComponent(new Point(-1,-1),Direction.UP));
        }

        @Test
        void placeWithTakenPositionThrowsException() {
            shipBoard.offerComponent(componentToAdd);
            shipBoard.placeComponent(new Point(7,7),Direction.UP);
            shipBoard.weldLastComponent();
            shipBoard.offerComponent(componentToAdd);
            assertThrows(IllegalStateException.class, () -> shipBoard.placeComponent(new Point(7,7),Direction.UP));
        }

        @Test
        void weldWithoutPositionThrowsException() {
            shipBoard.offerComponent(componentToAdd);
            assertThrows(IllegalStateException.class, () -> shipBoard.weldLastComponent());
        }

        @Test
        void componentIsRotated() {
            Point point = new Point(7,7);

            Map<Direction, Connector> expectedConnectors = Direction.rotateDirectionMap(componentToAdd.getConnectors(), Direction.UP, Direction.LEFT);

            shipBoard.offerComponent(componentToAdd);
            shipBoard.placeComponent(point,Direction.LEFT);
            shipBoard.weldLastComponent();
            assertEquals(expectedConnectors, shipBoard.getComponentMap().get(point).getConnectors());
        }

        @Test
        void grabPlacedComponentUpdatesLastPosition() {
            shipBoard.offerComponent(componentToAdd);
            shipBoard.placeComponent(new Point(7,7),Direction.UP);
            shipBoard.grabPlacedComponent();
            assertTrue(shipBoard.getLastPosition().isEmpty());
            assertEquals(componentToAdd, shipBoard.getLastComponent().orElse(null));
        }

        @Test
        void grabPlacedComponentGeneratesEvent() {
            shipBoard.offerComponent(componentToAdd);
            shipBoard.placeComponent(new Point(7,7),Direction.UP);
            shipBoard.grabPlacedComponent();
            verify(shipBoard.getEventListener(), times(1))
                    .notifyGrabPlacedComponentEvent(shipBoard);
        }

        @Test
        void grabPlacedComponentThrowsIfThereIsNoPlacedComponentToGrab() {
            assertThrows(IllegalStateException.class, () -> shipBoard.grabPlacedComponent());
            shipBoard.offerComponent(componentToAdd);
            assertThrows(IllegalStateException.class, () -> shipBoard.grabPlacedComponent());
        }

        @Test
        void componentIsStashed() {
            shipBoard.offerComponent(componentToAdd);
            shipBoard.stashComponent();
            assertTrue(shipBoard.getStashedComponents().contains(componentToAdd));
        }

        @Test
        void stashGeneratesEvent() {
            shipBoard.offerComponent(componentToAdd);
            shipBoard.stashComponent();
            verify(shipBoard.getEventListener(), times(1))
                    .notifyStashComponentEvent(shipBoard);
        }

        @Test
        void stashWhenLimitIsReachedThrowsException() {
            shipBoard.offerComponent(componentToAdd);
            shipBoard.stashComponent();
            shipBoard.offerComponent(componentToAdd);
            shipBoard.stashComponent();
            shipBoard.offerComponent(componentToAdd);
            assertThrows(IllegalStateException.class, () -> shipBoard.stashComponent());
        }

        @Test
        void stashWithNoComponentThrowsException() {
            assertThrows(IllegalStateException.class, () -> shipBoard.stashComponent());
        }

        @Test
        void stashedComponentIsGrabbed() {
            shipBoard.offerComponent(componentToAdd);
            shipBoard.stashComponent();
            assertFalse(shipBoard.getLastComponent().isPresent());
            shipBoard.grabStashedComponent(0);
            assertEquals(componentToAdd, shipBoard.getLastComponent().orElse(null));
        }

        @Test
        void grabStashedComponentGeneratesEvent() {
            shipBoard.offerComponent(componentToAdd);
            shipBoard.stashComponent();
            shipBoard.grabStashedComponent(0);
            verify(shipBoard.getEventListener(), times(1))
                    .notifyGrabStashedComponentEvent(shipBoard, 0);
        }

        @Test
        void grabStashedComponentOutOfBoundsThrowsException() {
            shipBoard.offerComponent(componentToAdd);
            shipBoard.stashComponent();
            assertThrows(IllegalArgumentException.class, () -> shipBoard.grabStashedComponent(1));
        }

        @Test
        void rejectComponentReturnsLastComponent() {
            it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.Component component = new it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.Component();
            shipBoard.offerComponent(component);
            assertEquals(component, shipBoard.rejectComponent());
            assertTrue(shipBoard.getLastComponent().isEmpty());
            assertTrue(shipBoard.getLastPosition().isEmpty());
        }

        @Test
        void rejectThrowsWhenStashed() {
            shipBoard.offerComponent(new it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.Component());
            shipBoard.stashComponent();
            shipBoard.grabStashedComponent(0);
            assertThrows(IllegalStateException.class, () -> shipBoard.rejectComponent());
        }

        @Test
        void finishBuildingWelds() {
            it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.Component component = new it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.Component();
            Point point = new Point(6,7);
            shipBoard.offerComponent(component);
            shipBoard.placeComponent(point,Direction.UP);
            shipBoard.finishBuilding();
            assertEquals(component, shipBoard.getComponentMap().get(point));
        }

        @Test
        void removeComponentGeneratesEvent() {
            Point point = new Point(6,7);
            shipBoard.offerComponent(componentToAdd);
            shipBoard.placeComponent(point,Direction.UP);
            shipBoard.weldLastComponent();
            shipBoard.removeComponent(point);
            verify(shipBoard.getEventListener(), times(1))
                    .notifyRemoveComponentEvent(shipBoard, point);
        }

        @Test
        void activateComponentGeneratesEvent() {
            Point point = new Point(6,7);
            shipBoard.addWeldedComponent(new DoubleCannon(), point, Direction.UP);
            shipBoard.activateComponent(point);
            verify(shipBoard.getEventListener(), times(1))
                    .notifyActivateComponentEvent(shipBoard, point, true);
        }

        @Test
        void finishBuildingRejects() {
            Map<Point, it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.Component> expMap = new HashMap<>(shipBoard.getComponentMap());
            it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.Component component = new it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.Component();
            Point point = new Point(6,7);
            shipBoard.offerComponent(component);
            shipBoard.finishBuilding();
            assertEquals(expMap, shipBoard.getComponentMap());
            assertTrue(shipBoard.getLastComponent().isEmpty());
            assertTrue(shipBoard.getLastPosition().isEmpty());
        }

        @Test
        void getFirePowerWithAliensIsIncreased() {
            Point p1 = new Point(8,7);
            Point p2 = new Point(9,7);
            shipBoard.addWeldedComponent(new Cabin(), p1, Direction.UP);
            shipBoard.addWeldedComponent(new Cannon(), p2, Direction.UP);
            shipBoard.initializeCabin(p1,CrewType.PURPLE);
            assertEquals(6, shipBoard.getFirePower());
        }

        @Test
        void getFirePowerWithAliensIsNotIncreased() {
            Point p1 = new Point(8,7);
            shipBoard.addWeldedComponent(new Cabin(), p1, Direction.UP);
            shipBoard.initializeCabin(p1,CrewType.PURPLE);
            assertEquals(0, shipBoard.getFirePower());
        }

        @Test
        void getEnginePowerWithAliensIsIncreased() {
            Point p1 = new Point(8,7);
            Point p2 = new Point(9,7);
            shipBoard.addWeldedComponent(new Cabin(), p1, Direction.UP);
            shipBoard.addWeldedComponent(new Engine(), p2, Direction.UP);
            shipBoard.initializeCabin(p1,CrewType.BROWN);
            assertEquals(3, shipBoard.getEnginePower());
        }

        @Test
        void getEnginePowerWithAliensIsNotIncreased() {
            Point p1 = new Point(8,7);
            shipBoard.addWeldedComponent(new Cabin(), p1, Direction.UP);
            shipBoard.initializeCabin(p1,CrewType.BROWN);
            assertEquals(0, shipBoard.getEnginePower());
        }
    }

    @Nested
    class TestShipBoardTests {
        TestShipBoardForTesting shipBoard;

        @BeforeEach
        void setup() {
            shipBoard = new TestShipBoardForTesting(GameColor.RED);
        }

        @Test
        void stashDoNothingInTestLevel() {
            shipBoard.offerComponent(new it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.Component());
            shipBoard.stashComponent();
            assertTrue(shipBoard.getLastComponent().isPresent());
            assertTrue(shipBoard.getStashedComponents().isEmpty());
        }

        @Test
        void grabStashedDoesNothingInTestLevel() {
            shipBoard.grabStashedComponent(0);
            assertTrue(shipBoard.getLastComponent().isEmpty());
        }

        @Test
        void lifeSupportsAreNotConsideredInTest() {
            shipBoard.addWeldedComponent(new LifeSupport(CrewType.PURPLE),new Point(7,8),Direction.UP);
            assertTrue(shipBoard.getLifeSupports().isEmpty());
            shipBoard.removeComponent(new Point(7,8));
            assertTrue(shipBoard.getLifeSupports().isEmpty());
        }
    }

    @Nested
    @DisplayName("Specific components tests")
    class SpecificComponentTests {
        it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.Component component;

        void addComponent(Point point) {
            shipBoard.offerComponent(component);
            shipBoard.placeComponent(point,Direction.UP);
            shipBoard.weldLastComponent();
        }

        static boolean restIsUnchanged(ShipBoard shipBoard) {
            return shipBoard.getCredits() == 0 &&
                    shipBoard.getStashedComponents().isEmpty();
        }

        @BeforeEach
        void setup() {
            shipBoard = new SecondShipBoardForTesting(GameColor.BLUE);
            shipBoard.removeComponent(new Point(7,7));
        }

        @Nested
        @DisplayName("Cannon tests")
        class CannonTests {
            Cannon cannon;

            @BeforeEach
            void setup() {
                cannon = new Cannon(Map.of(
                        Direction.UP, Connector.UNIVERSAL,
                        Direction.LEFT, Connector.UNIVERSAL,
                        Direction.DOWN, Connector.UNIVERSAL,
                        Direction.RIGHT, Connector.UNIVERSAL
                ));
                component = cannon;
            }

            boolean restIsUnchanged() {
                return shipBoard.getActivatables().isEmpty() &&
                        shipBoard.getCargoHolds().isEmpty() &&
                        shipBoard.getCabins().isEmpty() &&
                        shipBoard.getBatteries().isEmpty() &&
                        shipBoard.getEngines().isEmpty() &&
                        shipBoard.getShields().isEmpty() &&
                        shipBoard.getLifeSupports().isEmpty();
            }

            @Test
            void addCannonUpdatesMapAndFirePower() {
                addComponent(new Point(7,7));
                assertEquals(shipBoard.getCannons().get(new Point(7,7)), cannon);
                assertEquals(1, shipBoard.getCannons().size());
                assertEquals(cannon.getFirePower(), shipBoard.getFirePower());
                assertTrue(restIsUnchanged() && ShipBoardTest.SpecificComponentTests.restIsUnchanged(shipBoard));
            }

            @Test
            void removeCannonUpdatesMapAndFirePower() {
                addComponent(new Point(7,7));
                shipBoard.discardComponent(new Point(7,7));
                assertTrue(shipBoard.getCannons().isEmpty());
                assertEquals(0, shipBoard.getFirePower());
                assertTrue(restIsUnchanged() &&  ShipBoardTest.SpecificComponentTests.restIsUnchanged(shipBoard));
            }
        }

        @Nested
        @DisplayName("Engine Test")
        class EngineTests {
            Engine engine;

            @BeforeEach
            void setup() {
                engine = new Engine(Map.of(
                        Direction.UP, Connector.UNIVERSAL,
                        Direction.LEFT, Connector.UNIVERSAL,
                        Direction.DOWN, Connector.UNIVERSAL,
                        Direction.RIGHT, Connector.UNIVERSAL
                ));
                component = engine;
            }

            boolean restIsUnchanged() {
                return shipBoard.getActivatables().isEmpty() &&
                        shipBoard.getCargoHolds().isEmpty() &&
                        shipBoard.getCabins().isEmpty() &&
                        shipBoard.getBatteries().isEmpty() &&
                        shipBoard.getCannons().isEmpty() &&
                        shipBoard.getShields().isEmpty() &&
                        shipBoard.getLifeSupports().isEmpty();
            }

            @Test
            void addEngineUpdatesMapAndEnginePower() {
                addComponent(new Point(7,7));
                assertEquals(1, shipBoard.getEngines().size());
                assertEquals(engine, shipBoard.getEngines().get(new Point(7,7)));
                assertEquals(engine.getEnginePower(), shipBoard.getEnginePower());
                assertTrue(restIsUnchanged() &&  ShipBoardTest.SpecificComponentTests.restIsUnchanged(shipBoard));
            }

            @Test
            void removeEngineUpdatesMapAndEnginePower() {
                addComponent(new Point(7,7));
                shipBoard.discardComponent(new Point(7,7));
                assertEquals(0, shipBoard.getEngines().size());
                assertTrue(shipBoard.getEngines().isEmpty());
                assertTrue(restIsUnchanged() && ShipBoardTest.SpecificComponentTests.restIsUnchanged(shipBoard));
            }
        }

        @Nested
        @DisplayName("Double cannon tests")
        class DoubleCannonTests {
            DoubleCannon doubleCannon;

            @BeforeEach
            void setup() {
                doubleCannon = new DoubleCannon(Map.of(
                        Direction.UP, Connector.UNIVERSAL,
                        Direction.LEFT, Connector.UNIVERSAL,
                        Direction.DOWN, Connector.UNIVERSAL,
                        Direction.RIGHT, Connector.UNIVERSAL
                ));
                component = doubleCannon;
            }

            boolean restIsUnchanged() {
                return shipBoard.getCargoHolds().isEmpty() &&
                        shipBoard.getCabins().isEmpty() &&
                        shipBoard.getBatteries().isEmpty() &&
                        shipBoard.getEngines().isEmpty() &&
                        shipBoard.getShields().isEmpty() &&
                        shipBoard.getLifeSupports().isEmpty();
            }

            @Test
            void addUpdatesMaps() {
                addComponent(new Point(7,7));
                assertEquals(shipBoard.getCannons().get(new Point(7,7)), doubleCannon);
                assertEquals(shipBoard.getActivatables().get(new Point(7,7)), doubleCannon);
                assertEquals(1, shipBoard.getCannons().size());
                assertEquals(1, shipBoard.getActivatables().size());
                assertEquals(0, shipBoard.getFirePower());
                assertTrue(restIsUnchanged() && ShipBoardTest.SpecificComponentTests.restIsUnchanged(shipBoard));
            }

            @Test
            void removeUpdatesMaps() {
                addComponent(new Point(7,7));
                shipBoard.discardComponent(new Point(7,7));
                assertTrue(shipBoard.getCannons().isEmpty());
                assertTrue(shipBoard.getCannons().isEmpty());
                assertEquals(0, shipBoard.getFirePower());
                assertTrue(restIsUnchanged() &&  ShipBoardTest.SpecificComponentTests.restIsUnchanged(shipBoard));
            }

            @Test
            void activateIncrementsFirePower() {
                addComponent(new Point(7,7));
                shipBoard.activateComponent(new Point(7,7));
                Map<Point, Cannon> expCannons = new HashMap<>(shipBoard.getCannons());
                Map<Point, Activatable> expActivatables = new HashMap<>(shipBoard.getActivatables());
                assertEquals(4, shipBoard.getFirePower());
                assertTrue(restIsUnchanged() && ShipBoardTest.SpecificComponentTests.restIsUnchanged(shipBoard));
                assertEquals(expCannons, shipBoard.getCannons());
                assertEquals(expActivatables, shipBoard.getActivatables());
            }

            @Test
            void activateTwiceDoesNothing() {
                addComponent(new Point(7,7));
                shipBoard.activateComponent(new Point(7,7));
                shipBoard.activateComponent(new Point(7,7));
                Map<Point, Cannon> expCannons = new HashMap<>(shipBoard.getCannons());
                Map<Point, Activatable> expActivatables = new HashMap<>(shipBoard.getActivatables());
                assertEquals(4, shipBoard.getFirePower());
                assertTrue(restIsUnchanged() && ShipBoardTest.SpecificComponentTests.restIsUnchanged(shipBoard));
                assertEquals(expCannons, shipBoard.getCannons());
                assertEquals(expActivatables, shipBoard.getActivatables());
            }

            @Test
            void deactivateWhenNotActiveDoesNothing() {
                addComponent(new Point(7,7));
                Map<Point, Cannon> expCannons = new HashMap<>(shipBoard.getCannons());
                Map<Point, Activatable> expActivatables = new HashMap<>(shipBoard.getActivatables());
                shipBoard.deactivateComponent(new Point(7,7));
                assertEquals(0, shipBoard.getFirePower());
                assertEquals(expCannons, shipBoard.getCannons());
                assertEquals(expActivatables, shipBoard.getActivatables());
                assertTrue(restIsUnchanged() && ShipBoardTest.SpecificComponentTests.restIsUnchanged(shipBoard));
            }

            @Test
            void deactivateWhenActiveDecrementsFirePower() {
                addComponent(new Point(7,7));
                shipBoard.activateComponent(new Point(7,7));
                Map<Point, Cannon> expCannons = new HashMap<>(shipBoard.getCannons());
                Map<Point, Activatable> expActivatables = new HashMap<>(shipBoard.getActivatables());
                shipBoard.deactivateComponent(new Point(7,7));
                assertEquals(0, shipBoard.getFirePower());
                assertEquals(expCannons, shipBoard.getCannons());
                assertEquals(expActivatables, shipBoard.getActivatables());
                assertTrue(restIsUnchanged() && ShipBoardTest.SpecificComponentTests.restIsUnchanged(shipBoard));
            }
        }

        @Nested
        @DisplayName("Double Engine tests")
        class DoubleEngineTests {
            DoubleEngine doubleEngine;

            @BeforeEach
            void setup() {
                doubleEngine = new DoubleEngine(Map.of(
                        Direction.UP, Connector.UNIVERSAL,
                        Direction.LEFT, Connector.UNIVERSAL,
                        Direction.DOWN, Connector.UNIVERSAL,
                        Direction.RIGHT, Connector.UNIVERSAL
                ));
                component = doubleEngine;
            }

            boolean restIsUnchanged() {
                return shipBoard.getCargoHolds().isEmpty() &&
                        shipBoard.getCabins().isEmpty() &&
                        shipBoard.getBatteries().isEmpty() &&
                        shipBoard.getCannons().isEmpty() &&
                        shipBoard.getLifeSupports().isEmpty() &&
                        shipBoard.getShields().isEmpty() &&
                        SpecificComponentTests.restIsUnchanged(shipBoard);
            }

            @Test
            void addUpdatesMaps() {
                addComponent(new Point(7,7));
                assertTrue(restIsUnchanged());
                assertEquals(1, shipBoard.getEngines().size());
                assertEquals(1, shipBoard.getActivatables().size());
                assertEquals(doubleEngine, shipBoard.getActivatables().get(new Point(7,7)));
                assertEquals(doubleEngine, shipBoard.getEngines().get(new Point(7,7)));
                assertEquals(0, shipBoard.getEnginePower());
            }

            @Test
            void removeUpdatesMaps() {
                addComponent(new Point(7,7));
                shipBoard.discardComponent(new Point(7,7));
                assertTrue(restIsUnchanged());
                assertTrue(shipBoard.getEngines().isEmpty());
                assertTrue(shipBoard.getActivatables().isEmpty());
                assertEquals(0, shipBoard.getEnginePower());
            }

            @Test
            void activateUpdatesEnginePower() {
                addComponent(new Point(7,7));
                shipBoard.activateComponent(new Point(7,7));
                assertTrue(restIsUnchanged());
                assertEquals(2, shipBoard.getEnginePower());
                assertEquals(1, shipBoard.getEngines().size());
                assertEquals(1, shipBoard.getActivatables().size());
                assertEquals(doubleEngine, shipBoard.getActivatables().get(new Point(7,7)));
                assertEquals(doubleEngine, shipBoard.getEngines().get(new Point(7,7)));
            }

            @Test
            void multipleActivateDoNothing() {
                addComponent(new Point(7,7));
                shipBoard.activateComponent(new Point(7,7));
                shipBoard.activateComponent(new Point(7,7));
                assertTrue(restIsUnchanged());
                assertEquals(2, shipBoard.getEnginePower());
                assertEquals(1, shipBoard.getEngines().size());
                assertEquals(1, shipBoard.getActivatables().size());
                assertEquals(doubleEngine, shipBoard.getActivatables().get(new Point(7,7)));
                assertEquals(doubleEngine, shipBoard.getEngines().get(new Point(7,7)));
            }

            @Test
            void deactivateUpdatesEnginePower() {
                addComponent(new Point(7,7));
                shipBoard.activateComponent(new Point(7,7));
                shipBoard.deactivateComponent(new Point(7,7));
                assertTrue(restIsUnchanged());
                assertEquals(0, shipBoard.getEnginePower());
                assertEquals(1, shipBoard.getEngines().size());
                assertEquals(1, shipBoard.getActivatables().size());
                assertEquals(doubleEngine, shipBoard.getActivatables().get(new Point(7,7)));
                assertEquals(doubleEngine, shipBoard.getEngines().get(new Point(7,7)));
            }

            @Test
            void multipleDeactivateDoNothing() {
                addComponent(new Point(7,7));
                shipBoard.activateComponent(new Point(7,7));
                shipBoard.deactivateComponent(new Point(7,7));
                shipBoard.deactivateComponent(new Point(7,7));
                assertTrue(restIsUnchanged());
                assertEquals(0, shipBoard.getEnginePower());
                assertEquals(1, shipBoard.getEngines().size());
                assertEquals(1, shipBoard.getActivatables().size());
                assertEquals(doubleEngine, shipBoard.getActivatables().get(new Point(7,7)));
                assertEquals(doubleEngine, shipBoard.getEngines().get(new Point(7,7)));
            }
        }

        @Nested
        @DisplayName("Shield tests")
        class ShieldTests {
            Shield shield;

            @BeforeEach
            void setup() {
                shield = new Shield(Map.of(
                        Direction.UP, Connector.UNIVERSAL,
                        Direction.LEFT, Connector.UNIVERSAL,
                        Direction.DOWN, Connector.UNIVERSAL,
                        Direction.RIGHT, Connector.UNIVERSAL
                ));
                component = shield;
            }

            boolean restIsUnchanged() {
                return shipBoard.getCargoHolds().isEmpty() &&
                        shipBoard.getCabins().isEmpty() &&
                        shipBoard.getBatteries().isEmpty() &&
                        shipBoard.getCannons().isEmpty() &&
                        shipBoard.getLifeSupports().isEmpty() &&
                        shipBoard.getEngines().isEmpty() &&
                        SpecificComponentTests.restIsUnchanged(shipBoard);
            }

            @Test
            void addUpdatesMaps() {
                addComponent(new Point(7,7));
                assertTrue(restIsUnchanged());
                assertEquals(1, shipBoard.getShields().size());
                assertEquals(shield, shipBoard.getShields().get(new Point(7,7)));
                assertEquals(1, shipBoard.getActivatables().size());
                assertEquals(shield, shipBoard.getActivatables().get(new Point(7,7)));
                assertEquals(Set.of(), shipBoard.getShieldDirections());
            }

            @Test
            void removeUpdatesMap() {
                addComponent(new Point(7,7));
                shipBoard.discardComponent(new Point(7,7));
                assertTrue(restIsUnchanged());
                assertEquals(0, shipBoard.getShields().size());
                assertEquals(Set.of(), shipBoard.getShieldDirections());
            }

            @Test
            void activateUpdatesShieldDirections() {
                addComponent(new Point(7,7));
                shipBoard.activateComponent(new Point(7,7));
                assertTrue(restIsUnchanged());
                assertEquals(1, shipBoard.getShields().size());
                assertEquals(1, shipBoard.getActivatables().size());
                assertEquals(shield, shipBoard.getActivatables().get(new Point(7,7)));
                //assertArrayEquals(new boolean[]{false, false, false, false}, shipBoard.getShieldDirections());
            }
        }

        @Nested
        @DisplayName("Cabin tests")
        class CabinTests {
            Cabin cabin;

            @BeforeEach
            void setup() {
                cabin = new Cabin(Map.of(
                        Direction.UP, Connector.UNIVERSAL,
                        Direction.LEFT, Connector.UNIVERSAL,
                        Direction.DOWN, Connector.UNIVERSAL,
                        Direction.RIGHT, Connector.UNIVERSAL
                ));
                component = cabin;
            }

            boolean restIsUnchanged() {
                return shipBoard.getCargoHolds().isEmpty() &&
                        shipBoard.getShields().isEmpty() &&
                        shipBoard.getBatteries().isEmpty() &&
                        shipBoard.getCannons().isEmpty() &&
                        shipBoard.getLifeSupports().isEmpty() &&
                        shipBoard.getEngines().isEmpty() &&
                        SpecificComponentTests.restIsUnchanged(shipBoard);
            }

            @Test
            void addUpdatesMaps() {
                addComponent(new Point(7,7));
                assertTrue(restIsUnchanged());
                assertEquals(1, shipBoard.getCabins().size());
                assertEquals(cabin, shipBoard.getCabins().get(new Point(7,7)));
            }

            @Test
            void removeUpdatesMap() {
                addComponent(new Point(7,7));
                assertTrue(restIsUnchanged());
                shipBoard.initializeCabin(new Point(7,7),CrewType.HUMAN);
                shipBoard.discardComponent(new Point(7,7));
                assertEquals(0, shipBoard.getCabins().size());
            }

            @Test
            void startingCabinOnlyAllowsHumansInCrewTypeOptions(){
                addComponent(new Point(7,7));
                assertEquals(Set.of(CrewType.HUMAN), shipBoard.getCrewTypeOptions(new Point(7,7)));
            }

            @Test
            void noCabinThrowsExceptionInCrewTypeOptions(){
                assertThrows(IllegalStateException.class, ()-> shipBoard.getCrewTypeOptions(new Point(7,7)));
            }

            @Test
            void initializeCabin(){
                // adding cabin component to map
                addComponent(new Point(7,7));
                assertTrue(restIsUnchanged());
                assertEquals(1, shipBoard.getCabins().size());
                assertEquals(cabin, shipBoard.getCabins().get(new Point(7,7)));

                // ensure that if the cabin has been initialized with a human, numResidents is 2
                shipBoard.initializeCabin(new Point(7,7), CrewType.HUMAN);
                assertEquals(2,shipBoard.getCrewSize());
            }

            @Test
            void initializeCabinGeneratesEvent() {
                addComponent(new Point(7,7));
                shipBoard.initializeCabin(new Point(7,7), CrewType.HUMAN);
                verify(shipBoard.getEventListener())
                        .notifyCabinInitializationEvent(shipBoard, new Point(7,7), CrewType.HUMAN);
            }

            @Test
            void initializeCabinThrowsWithInvalidPosition() {
                assertThrows(IllegalStateException.class, () -> shipBoard.initializeCabin(new Point(7,7), CrewType.HUMAN));
            }

            @Test
            void loseCrewThrowsException(){
                //throw an exception when there is no cabin
                assertThrows(IllegalStateException.class, () -> shipBoard.loseCrew(new Point(7,7)));
            }

            @Test
            void loseCrewHumans(){
                addComponent(new Point(7,7));
                assertTrue(restIsUnchanged());
                shipBoard.initializeCabin(new Point(7,7),CrewType.HUMAN);
                shipBoard.loseCrew(new Point(7,7));
                assertEquals(1,shipBoard.getCrewSize());
            }

            @Test
            void loseCrewGeneratesEvent() {
                addComponent( new Point(7,7));
                assertTrue(restIsUnchanged());
                shipBoard.initializeCabin(new Point(7,7), CrewType.HUMAN);
                shipBoard.loseCrew(new Point(7,7));
                verify(shipBoard.getEventListener())
                        .notifyLoseCrewEvent(shipBoard, new Point(7,7));
            }
        }

        // potentially tests with two and three batteries can just be simplified to be just one or the other
        @Nested
        @DisplayName("Battery Tests")
        class BatteryTests{
            Battery battery;

            boolean restIsUnchanged(){
                return shipBoard.getCargoHolds().isEmpty() &&
                        shipBoard.getCabins().isEmpty() &&
                        shipBoard.getCannons().isEmpty() &&
                        shipBoard.getEngines().isEmpty() &&
                        shipBoard.getShields().isEmpty() &&
                        shipBoard.getLifeSupports().isEmpty() &&
                        SpecificComponentTests.restIsUnchanged(shipBoard);
            }

            @Test
            void addTwoBatteriesUpdatesMaps(){
                battery = new Battery(Map.of(
                        Direction.UP, Connector.UNIVERSAL,
                        Direction.LEFT, Connector.UNIVERSAL,
                        Direction.DOWN, Connector.UNIVERSAL,
                        Direction.RIGHT, Connector.UNIVERSAL
                ), 2);
                component = battery;
                addComponent(new Point(7,7));
                assertTrue(restIsUnchanged());
                assertEquals(1,shipBoard.getBatteries().size());
                assertEquals(battery, shipBoard.getBatteries().get(new Point(7,7)));
            }

            @Test
            void removeTwoBatteriesUpdatesMap(){
                battery = new Battery(Map.of(
                        Direction.UP, Connector.UNIVERSAL,
                        Direction.LEFT, Connector.UNIVERSAL,
                        Direction.DOWN, Connector.UNIVERSAL,
                        Direction.RIGHT, Connector.UNIVERSAL
                ), 2);
                component = battery;
                addComponent(new Point(7,7));
                assertTrue(restIsUnchanged());
                shipBoard.discardComponent(new Point(7,7));
                assertEquals(0, shipBoard.getBatteries().size());
            }

            @Test
            void addThreeBatteriesUpdatesMaps(){
                battery = new Battery(Map.of(
                        Direction.UP, Connector.UNIVERSAL,
                        Direction.LEFT, Connector.UNIVERSAL,
                        Direction.DOWN, Connector.UNIVERSAL,
                        Direction.RIGHT, Connector.UNIVERSAL
                ), 3);
                component = battery;
                addComponent(new Point(7,7));
                assertTrue(restIsUnchanged());
                assertEquals(1,shipBoard.getBatteries().size());
                assertEquals(battery, shipBoard.getBatteries().get(new Point(7,7)));
            }

            @Test
            void removeThreeBatteriesUpdatesMap(){
                battery = new Battery(Map.of(
                        Direction.UP, Connector.UNIVERSAL,
                        Direction.LEFT, Connector.UNIVERSAL,
                        Direction.DOWN, Connector.UNIVERSAL,
                        Direction.RIGHT, Connector.UNIVERSAL
                ), 3);
                component = battery;
                addComponent(new Point(7,7));
                assertTrue(restIsUnchanged());
                shipBoard.discardComponent(new Point(7,7));
                assertEquals(0, shipBoard.getBatteries().size());
            }

            @Test
            void useThreeBatteries(){
                battery = new Battery(Map.of(
                        Direction.UP, Connector.UNIVERSAL,
                        Direction.LEFT, Connector.UNIVERSAL,
                        Direction.DOWN, Connector.UNIVERSAL,
                        Direction.RIGHT, Connector.UNIVERSAL
                ), 3);
                component = battery;
                addComponent(new Point(7,7));
                assertTrue(restIsUnchanged());
                int numBatteries = battery.getNumBatteries();
                for (int i = 0; i < numBatteries; i++) {
                    shipBoard.useBatteries(new Point(7,7));
                }
                assertEquals(0, shipBoard.getBatteries().get(new Point(7,7)).getNumBatteries());
            }

            @Test void noTwoBatteryThrowsException(){
                battery = new Battery(Map.of(
                        Direction.UP, Connector.UNIVERSAL,
                        Direction.LEFT, Connector.UNIVERSAL,
                        Direction.DOWN, Connector.UNIVERSAL,
                        Direction.RIGHT, Connector.UNIVERSAL
                ), 2);
                component = battery;
                assertThrows(IllegalStateException.class, () -> shipBoard.useBatteries(new Point(7,7)));
                assertTrue(restIsUnchanged());
            }

            @Test void noThreeBatteryThrowsException(){
                battery = new Battery(Map.of(
                        Direction.UP, Connector.UNIVERSAL,
                        Direction.LEFT, Connector.UNIVERSAL,
                        Direction.DOWN, Connector.UNIVERSAL,
                        Direction.RIGHT, Connector.UNIVERSAL
                ), 3);
                component = battery;
                assertThrows(IllegalStateException.class, () -> shipBoard.useBatteries(new Point(7,7)));
                assertTrue(restIsUnchanged());
            }

            @Test
            void useBatteriesGeneratesEvent() {
                battery = new Battery(Map.of(
                        Direction.UP, Connector.UNIVERSAL,
                        Direction.LEFT, Connector.UNIVERSAL,
                        Direction.DOWN, Connector.UNIVERSAL,
                        Direction.RIGHT, Connector.UNIVERSAL
                ), 3);
                component = battery;
                addComponent(new Point(7,7));
                shipBoard.useBatteries(new Point(7,7));
                verify(shipBoard.getEventListener(), times(1))
                        .notifyUseBatteryEvent(shipBoard, new Point(7,7));
            }
        }

        @Nested
        @DisplayName("Cargo Hold Tests")
        class CargoHoldTests{
            CargoHold cargo;

            @BeforeEach
            void setup(){
                cargo = new CargoHold(Map.of(
                        Direction.UP, Connector.UNIVERSAL,
                        Direction.LEFT, Connector.UNIVERSAL,
                        Direction.DOWN, Connector.UNIVERSAL,
                        Direction.RIGHT, Connector.UNIVERSAL
                ), 3, false);
                component = cargo;
            }

            boolean restIsUnchanged(){
                return shipBoard.getShields().isEmpty() &&
                        shipBoard.getEngines().isEmpty() &&
                        shipBoard.getCannons().isEmpty() &&
                        shipBoard.getCabins().isEmpty() &&
                        shipBoard.getLifeSupports().isEmpty() &&
                        shipBoard.getBatteries().isEmpty() &&
                        SpecificComponentTests.restIsUnchanged(shipBoard);
            }

            @Test
            void addUpdatesMap(){
                addComponent(new Point(7,7));
                assertTrue(restIsUnchanged());
                assertEquals(1,shipBoard.getCargoHolds().size());
                assertEquals(cargo, shipBoard.getCargoHolds().get(new Point(7,7)));
            }

            @Test
            void removeUpdatesMap(){
                addComponent(new Point(7,7));
                assertTrue(restIsUnchanged());
                shipBoard.discardComponent(new Point(7,7));
                assertEquals(0, shipBoard.getCargoHolds().size());
            }

            @Test
            void placeGoods(){
                addComponent(new Point(7,7));
                assertTrue(restIsUnchanged());
                shipBoard.placeGoods(new Point(7,7), GoodsType.GREEN,3);
                assertEquals(6,shipBoard.getGoodsValue());
            }

            @Test
            void placeGoodsUpdatesGoodsMap() {
                addComponent(new Point(7,7));
                shipBoard.placeGoods(new Point(7,7), GoodsType.GREEN,3);
                verify(shipBoard.getEventListener(), times(1))
                        .notifyGoodsUpdateEvent(shipBoard, new Point(7,7), GoodsType.GREEN, true);
            }

            @Test
            void noCargoHoldThrowsException(){
                assertThrows(IllegalStateException.class, () -> shipBoard.placeGoods(new Point(7,7), GoodsType.GREEN, 3));
                assertTrue(restIsUnchanged());
            }

            @Test
            void removeGoods(){
                addComponent(new Point(7,7));
                assertTrue(restIsUnchanged());
                shipBoard.placeGoods(new Point(7,7), GoodsType.GREEN, 3);
                shipBoard.removeGoods(new Point(7,7),GoodsType.GREEN);
                assertEquals(4, shipBoard.getGoodsValue());
            }

            @Test
            void removeGoodsGeneratesEvent() {
                addComponent(new Point(7,7));
                shipBoard.placeGoods(new Point(7,7), GoodsType.GREEN,3);
                clearInvocations(shipBoard.getEventListener());
                shipBoard.removeGoods(new Point(7,7),GoodsType.GREEN);
                verify(shipBoard.getEventListener(), times(1))
                        .notifyGoodsUpdateEvent(shipBoard, new Point(7,7), GoodsType.GREEN, false);
            }

            @Test
            void removeGoodsThrowsException(){
                assertThrows(IllegalStateException.class, () -> shipBoard.removeGoods(new Point(7,7), GoodsType.GREEN));
                assertTrue(restIsUnchanged());
            }

            @Test
            void removeCargoHoldUpdatesGoodsMap() {
                Point p1 = new Point(6,7);
                Point p2 = new Point(8,7);
                shipBoard.addWeldedComponent(new CargoHold(3), p1,Direction.UP);
                shipBoard.addWeldedComponent(new CargoHold(3), p2,Direction.UP);
                shipBoard.placeGoods(p1, GoodsType.GREEN,1);
                shipBoard.placeGoods(p2, GoodsType.GREEN,1);
                shipBoard.removeComponent(p1);
                assertEquals(Map.of(GoodsType.GREEN, 1), shipBoard.getGoods());
            }
        }

        @Nested
        @DisplayName("Life Support Tests")
        class LifeSupportTests{
            LifeSupport lifeSupport;

            @BeforeEach
            void setup(){
                lifeSupport = new LifeSupport(Map.of(
                        Direction.UP, Connector.UNIVERSAL,
                        Direction.LEFT, Connector.UNIVERSAL,
                        Direction.DOWN, Connector.UNIVERSAL,
                        Direction.RIGHT, Connector.UNIVERSAL
                ),CrewType.PURPLE);
                component = lifeSupport;
            }

            boolean restIsUnchanged(){
                return shipBoard.getBatteries().isEmpty() &&
                        shipBoard.getCabins().isEmpty() &&
                        shipBoard.getCannons().isEmpty() &&
                        shipBoard.getEngines().isEmpty() &&
                        shipBoard.getShields().isEmpty() &&
                        shipBoard.getCargoHolds().isEmpty() &&
                        SpecificComponentTests.restIsUnchanged(shipBoard);
            }

            @Test
            void addUpdatesMap(){
                addComponent(new Point(7,7));
                assertTrue(restIsUnchanged());
                assertEquals(1,shipBoard.getLifeSupports().size());
                assertEquals(lifeSupport, shipBoard.getLifeSupports().get(new Point(7,7)));
            }

            @Test
            void removeUpdatesMap(){
                addComponent(new Point(7,7));
                assertTrue(restIsUnchanged());
                shipBoard.discardComponent(new Point(7,7));
                assertEquals(0, shipBoard.getLifeSupports().size());
            }

            @Test
            void removeLifeSupportUpdatesAdjacentCabins() {
                LifeSupport ls = new LifeSupport(CrewType.PURPLE);
                Cabin cabin = new Cabin();
                Point p1 = new Point(8,7);
                Point p2 = new Point(9,7);
                Point p3 = new Point(8,8);
                shipBoard.addWeldedComponent(ls,p1,Direction.UP);
                shipBoard.addWeldedComponent(cabin,p2,Direction.UP);
                shipBoard.addWeldedComponent(new Cabin(),ShipBoard.center,Direction.UP);
                shipBoard.addWeldedComponent(new Cannon(),new Point(6,7),Direction.UP);
                shipBoard.addWeldedComponent(new Cabin(),p3, Direction.UP);
                shipBoard.initializeCabin(p3, CrewType.PURPLE);
                shipBoard.loseCrew(p3);
                shipBoard.initializeCabin(p2, CrewType.PURPLE);
                shipBoard.initializeCabin(ShipBoard.center, CrewType.HUMAN);
                shipBoard.removeComponent(p1);
                assertEquals(2, shipBoard.getCrewSize());
                assertEquals(2, shipBoard.getFirePower());
            }
        }

        @Nested
        @DisplayName("Cabin With Connected Life Support Test")
        class CabinLifeSupportTest{
            Cabin cabin;
            LifeSupport lifeSupport;

            @BeforeEach
            void setup(){
                cabin = new Cabin(Map.of(
                        Direction.UP, Connector.UNIVERSAL,
                        Direction.LEFT, Connector.UNIVERSAL,
                        Direction.DOWN, Connector.UNIVERSAL,
                        Direction.RIGHT, Connector.UNIVERSAL
                ));
                lifeSupport = new LifeSupport(Map.of(
                        Direction.UP, Connector.UNIVERSAL,
                        Direction.LEFT, Connector.UNIVERSAL,
                        Direction.DOWN, Connector.UNIVERSAL,
                        Direction.RIGHT, Connector.UNIVERSAL
                ),CrewType.PURPLE);
            }

            boolean restIsUnchanged(){
                return shipBoard.getEngines().isEmpty() &&
                        shipBoard.getCargoHolds().isEmpty() &&
                        shipBoard.getShields().isEmpty() &&
                        shipBoard.getBatteries().isEmpty() &&
                        shipBoard.getCannons().isEmpty() &&
                        SpecificComponentTests.restIsUnchanged(shipBoard);
            }

            @Test
            void addUpdatesMap(){
                // add cabin adjacent to life support, NOT in starting square
                component = cabin;
                addComponent(new Point(8,7));
                component = lifeSupport;
                addComponent((new Point(9,7)));

                assertTrue(restIsUnchanged());

                assertEquals(1,shipBoard.getLifeSupports().size());
                assertEquals(1, shipBoard.getCabins().size());

                assertEquals(lifeSupport, shipBoard.getLifeSupports().get(new Point(9,7)));
                assertEquals(cabin, shipBoard.getCabins().get(new Point(8,7)));
            }

            @Test
            void removeUpdatesMap(){
                // add cabin adjacent to life support, NOT in starting square
                component = cabin;
                addComponent(new Point(8,7));
                component = lifeSupport;
                addComponent((new Point(9,7)));
                assertTrue(restIsUnchanged());

                shipBoard.initializeCabin(new Point(8,7), CrewType.PURPLE);
                shipBoard.discardComponent(new Point(8,7));
                shipBoard.discardComponent(new Point(9,7));

                assertEquals(0, shipBoard.getCabins().size());
                assertEquals(0, shipBoard.getLifeSupports().size());
            }

            @Test
            void placingAlienInCabin(){
                // add cabin adjacent to life support, NOT in starting square
                component = cabin;
                addComponent(new Point(8,7));
                component = lifeSupport;
                addComponent((new Point(9,7)));
                assertTrue(restIsUnchanged());

                // initializing and ensuring alien is present
                shipBoard.initializeCabin(new Point(8,7), CrewType.PURPLE);
                assertFalse(shipBoard.getCrewTypeOptions(new Point(8,7)).contains(CrewType.PURPLE));
                assertEquals(1,shipBoard.getCrewSize());

                // losing the alien
                shipBoard.loseCrew(new Point(8,7));
                assertEquals(0, shipBoard.getCrewSize());
            }
        }
    }

    @Nested
    @DisplayName("getConnectedSets() tests")
    class GetConnectedSetsTest {
        it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.Component component;
        @BeforeEach
        void setUp() {
            component = new it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.Component(Map.of(
                    Direction.UP, Connector.UNIVERSAL,
                    Direction.LEFT, Connector.UNIVERSAL,
                    Direction.DOWN, Connector.UNIVERSAL,
                    Direction.RIGHT, Connector.UNIVERSAL
            ));
            shipBoard = new SecondShipBoardForTesting(GameColor.BLUE);
            shipBoard.removeComponent(new Point(7,7));
            for (int i = 5; i <= 9; i++) {
                shipBoard.offerComponent(component);
                shipBoard.placeComponent(new Point(i,7),Direction.UP);
            }
            shipBoard.offerComponent(component);
            shipBoard.placeComponent(new Point(7,6),Direction.UP);
            shipBoard.offerComponent(component);
            shipBoard.placeComponent(new Point(7,8),Direction.UP);
            shipBoard.offerComponent(component);
        }

        @Test
        void connectedSetsForConnectedShipsAre1() {
            assertEquals(1, shipBoard.getConnectedSets().size());
        }

        @Test
        void shipHas4differentConnectedSets() {
            shipBoard.discardComponent(new Point(7,7));

            assertEquals(4, shipBoard.getConnectedSets().size());
        }

        @Test
        void connectedSetsHaveTheRightMembers() {
            shipBoard.discardComponent(new Point(7,7));
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

    @Nested
    class ShipValidityTests {
        @BeforeEach
        void setup() {
            shipBoard = new SecondShipBoardForTesting(GameColor.BLUE);
        }

        void placeCannon(Point point) {
            shipBoard.offerComponent(new Cannon(
                    Map.of(Direction.UP,Connector.UNIVERSAL,
                            Direction.RIGHT,Connector.UNIVERSAL,
                            Direction.LEFT,Connector.UNIVERSAL,
                            Direction.DOWN,Connector.UNIVERSAL)
            ));
            shipBoard.placeComponent(point, Direction.UP);
            shipBoard.weldLastComponent();
        }

        @Test
        void shipIsValid() {
            placeCannon(new Point(6,7));
            assertTrue(shipBoard.checkValidity());
        }

        @Test
        void shipIsNotValidWithWrongCannon() {
            placeCannon(new Point(7,8));
            assertFalse(shipBoard.checkValidity());
        }

        @Test
        void shipIsNotValidWithRotatedEngine() {
            shipBoard.addWeldedComponent(new Engine(), new Point(8,7), Direction.LEFT);
            assertFalse(shipBoard.checkValidity());
        }

        @Test
        void shipIsNotValidWithWrongEngine() {
            shipBoard.addWeldedComponent(new Engine(), new Point(8,7), Direction.UP);
            shipBoard.addWeldedComponent(new it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.Component(), new Point(8,8), Direction.UP);
            assertFalse(shipBoard.checkValidity());
        }
    }

    @Nested
    class GeneralTests {
        TestShipBoardForTesting shipBoard;

        @BeforeEach
        void setup() {
            shipBoard = new TestShipBoardForTesting(GameColor.RED);
        }

        @Test
        void gainCredits() {
            shipBoard.gainCredits(10);
            assertEquals(10, shipBoard.getCredits());
        }

        @Test
        void removeAll() {
            shipBoard.addWeldedComponent(new it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.Component(), new Point(8, 7), Direction.UP);
            shipBoard.removeAll(false);
            assertEquals(1, shipBoard.getComponentMap().size());
            assertTrue(shipBoard.getComponentMap().containsKey(ShipBoard.center));
            assertEquals(0, shipBoard.getLosses());
        }

        @Test
        void discardAll() {
            shipBoard.addWeldedComponent(new it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.Component(), new Point(8, 7), Direction.UP);
            shipBoard.removeAll(true);
            assertEquals(1, shipBoard.getComponentMap().size());
            assertTrue(shipBoard.getComponentMap().containsKey(ShipBoard.center));
            assertEquals(1, shipBoard.getLosses());
        }

        @Test
        void keepShipPieceNoDiscard() {
            Point point = new Point(9, 7);
            shipBoard.addWeldedComponent(new it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.Component(), point, Direction.UP);
            shipBoard.keepShipPiece(List.of(Set.of(ShipBoard.center), Set.of(point)), 0, false);
            assertEquals(1, shipBoard.getComponentMap().size());
            assertTrue(shipBoard.getComponentMap().containsKey(ShipBoard.center));
            assertEquals(0, shipBoard.getLosses());
        }

        @Test
        void keepShipPieceWithDiscard() {
            Point point = new Point(9, 7);
            shipBoard.addWeldedComponent(new it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.Component(), point, Direction.UP);
            shipBoard.keepShipPiece(List.of(Set.of(ShipBoard.center), Set.of(point)), 0, true);
            assertEquals(1, shipBoard.getComponentMap().size());
            assertTrue(shipBoard.getComponentMap().containsKey(ShipBoard.center));
            assertEquals(1, shipBoard.getLosses());
        }

        @Test
        void getExposedConnectors() {
            Point p1 = new Point(8, 7);
            Point p2 = new Point(9, 7);
            shipBoard.addWeldedComponent(new it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.Component(Map.of(
                    Direction.UP, Connector.NONE,
                    Direction.DOWN, Connector.NONE,
                    Direction.LEFT, Connector.UNIVERSAL,
                    Direction.RIGHT, Connector.NONE
            )), p1, Direction.UP);
            shipBoard.addWeldedComponent(new Component(Map.of(
                    Direction.UP, Connector.NONE,
                    Direction.DOWN, Connector.NONE,
                    Direction.LEFT, Connector.NONE,
                    Direction.RIGHT, Connector.NONE
            )), p2, Direction.UP);
            assertEquals(3, shipBoard.getExposedConnectorsNumber());
        }

        @Test
        void activateThrowsExceptionWithInvalidPosition() {
            assertThrows(IllegalStateException.class, () -> shipBoard.activateComponent(new Point(7, 7)));
        }

        @Test
        void deactivateThrowsExceptionWithInvalidPosition() {
            assertThrows(IllegalStateException.class, () -> shipBoard.deactivateComponent(new Point(7, 7)));
        }

        @Test
        void deactivateAll() {
            shipBoard.addWeldedComponent(new DoubleCannon(), new Point(8, 7), Direction.UP);
            shipBoard.addWeldedComponent(new DoubleCannon(), new Point(9, 7), Direction.UP);
            shipBoard.activateComponent(new Point(8, 7));
            shipBoard.deactivateAll();
            assertEquals(0, shipBoard.getFirePower());
        }
    }

}