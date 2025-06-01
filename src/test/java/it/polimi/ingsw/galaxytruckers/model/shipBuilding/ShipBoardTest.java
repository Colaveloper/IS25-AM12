package it.polimi.ingsw.galaxytruckers.model.shipBuilding;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import org.junit.jupiter.api.*;

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
            shipBoard = new SecondShipBoard(GameColor.BLUE);
            shipBoard.removeComponent(new Point(7,7));
            componentToAdd = new Component(Arrays.asList(Connector.UNIVERSAL, Connector.SINGLE, Connector.DOUBLE, Connector.NONE));
        }

        @Test
        void placeWithoutWeldDoesNotUpdateMap() {
            Map<Point, Component> prevMap = new HashMap<>(shipBoard.getComponentMap());
            shipBoard.offerComponent(componentToAdd);
            shipBoard.placeComponent(new Point(7,7),0);
            assertEquals(prevMap, shipBoard.getComponentMap());
        }

        @Test
        void placeWithWeldDoesUpdateMap() {
            Map<Point, Component> expectedMap = new HashMap<>(shipBoard.getComponentMap());
            Point point = new Point(7,7);
            expectedMap.put(point, componentToAdd);
            shipBoard.offerComponent(componentToAdd);
            shipBoard.placeComponent(point,0);
            shipBoard.weldLastComponent();
            assertEquals(expectedMap, shipBoard.getComponentMap());
        }

        @Test
        void placeWithoutComponentThrowsException() {
            assertThrows(IllegalStateException.class, () -> shipBoard.placeComponent(new Point(7,7),0));
        }

        @Test
        void placeWithIllegalPositionThrowsException() {
            shipBoard.offerComponent(componentToAdd);
            assertThrows(IllegalArgumentException.class, () -> shipBoard.placeComponent(new Point(-1,-1),0));
        }

        @Test
        void placeWithTakenPositionThrowsException() {
            shipBoard.offerComponent(componentToAdd);
            shipBoard.placeComponent(new Point(7,7),0);
            shipBoard.weldLastComponent();
            shipBoard.offerComponent(componentToAdd);
            assertThrows(IllegalStateException.class, () -> shipBoard.placeComponent(new Point(7,7),0));
        }

        @Test
        void weldWithoutPositionThrowsException() {
            shipBoard.offerComponent(componentToAdd);
            assertThrows(IllegalStateException.class, () -> shipBoard.weldLastComponent());
        }

        @Test
        void componentIsRotated() {
            List<Connector> expectedConnectors = new ArrayList<>(componentToAdd.getConnectors());
            Point point = new Point(7,7);
            Collections.rotate(expectedConnectors, 1);
            shipBoard.offerComponent(componentToAdd);
            shipBoard.placeComponent(point,1);
            shipBoard.weldLastComponent();
            assertEquals(expectedConnectors, shipBoard.getComponentMap().get(point).getConnectors());
        }

        @Test
        void componentIsStashed() {
            shipBoard.offerComponent(componentToAdd);
            shipBoard.stashComponent();
            assertTrue(shipBoard.getStashedComponents().contains(componentToAdd));
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
        void stashedComponentIsGrabbed() {
            shipBoard.offerComponent(componentToAdd);
            shipBoard.stashComponent();
            assertFalse(shipBoard.getLastComponent().isPresent());
            shipBoard.grabStashedComponent(0);
            assertEquals(componentToAdd, shipBoard.getLastComponent().orElse(null));
        }

        @Test
        void grabStashedComponentOutOfBoundsThrowsException() {
            shipBoard.offerComponent(componentToAdd);
            shipBoard.stashComponent();
            assertThrows(IllegalArgumentException.class, () -> shipBoard.grabStashedComponent(1));
        }
    }

    @Nested
    @DisplayName("Specific components tests")
    class SpecificComponentTests {
        Component component;

        void addComponent(Point point) {
            shipBoard.offerComponent(component);
            shipBoard.placeComponent(point,0);
            shipBoard.weldLastComponent();
        }

        static boolean restIsUnchanged(ShipBoard shipBoard) {
            return shipBoard.getCredits() == 0 &&
                    shipBoard.getStashedComponents().isEmpty();
        }

        @BeforeEach
        void setup() {
            shipBoard = new SecondShipBoard(GameColor.BLUE);
            shipBoard.removeComponent(new Point(7,7));
        }

        @Nested
        @DisplayName("Cannon tests")
        class CannonTests {
            Cannon cannon;

            @BeforeEach
            void setup() {
                cannon = new Cannon(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL));
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
                engine = new Engine(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL));
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
                doubleCannon = new DoubleCannon(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL));
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
                doubleEngine = new DoubleEngine(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL));
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
                shield = new Shield(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL));
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
                assertArrayEquals(new boolean[]{false, false, false, false}, shipBoard.getShieldDirections());
            }

            @Test
            void removeUpdatesMap() {
                addComponent(new Point(7,7));
                shipBoard.discardComponent(new Point(7,7));
                assertTrue(restIsUnchanged());
                assertEquals(0, shipBoard.getShields().size());
                assertArrayEquals(new boolean[]{false, false, false, false}, shipBoard.getShieldDirections());
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
                cabin = new Cabin(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL));
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

                // ensure that if the cabin has been initialized with a purple alien, an exception is thrown
                // since the cabin is not connected to a life support component
                assertThrows(IllegalStateException.class, ()-> shipBoard.initializeCabin(new Point(7,7), CrewType.PURPLE));

                // ensure that if the cabin has been initialized with a brown alien, an exception is thrown
                // since the cabin is not connected to a life support component
                assertThrows(IllegalStateException.class, ()-> shipBoard.initializeCabin(new Point(7,7), CrewType.BROWN));
            }

            @Test
            void loseCrewThrowsException(){
                //throw an exception when there is no cabin
                assertThrows(IllegalStateException.class, () -> shipBoard.loseCrew(new Point(7,7), 1));
            }

            @Test
            void loseCrewHumans(){
                addComponent(new Point(7,7));
                assertTrue(restIsUnchanged());
                shipBoard.initializeCabin(new Point(7,7),CrewType.HUMAN);
                shipBoard.loseCrew(new Point(7,7),2);
                assertEquals(0,shipBoard.getCrewSize());
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
                battery = new Battery(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL), 2);
                component = battery;
                addComponent(new Point(7,7));
                assertTrue(restIsUnchanged());
                assertEquals(1,shipBoard.getBatteries().size());
                assertEquals(battery, shipBoard.getBatteries().get(new Point(7,7)));
            }

            @Test
            void removeTwoBatteriesUpdatesMap(){
                battery = new Battery(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL), 2);
                component = battery;
                addComponent(new Point(7,7));
                assertTrue(restIsUnchanged());
                shipBoard.discardComponent(new Point(7,7));
                assertEquals(0, shipBoard.getBatteries().size());
            }

            @Test
            void addThreeBatteriesUpdatesMaps(){
                battery = new Battery(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL), 3);
                component = battery;
                addComponent(new Point(7,7));
                assertTrue(restIsUnchanged());
                assertEquals(1,shipBoard.getBatteries().size());
                assertEquals(battery, shipBoard.getBatteries().get(new Point(7,7)));
            }

            @Test
            void removeThreeBatteriesUpdatesMap(){
                battery = new Battery(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL), 3);
                component = battery;
                addComponent(new Point(7,7));
                assertTrue(restIsUnchanged());
                shipBoard.discardComponent(new Point(7,7));
                assertEquals(0, shipBoard.getBatteries().size());
            }

            @Test
            void useTwoBatteries(){
                battery = new Battery(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL), 2);
                component = battery;
                addComponent(new Point(7,7));
                assertTrue(restIsUnchanged());
                shipBoard.useBatteries(new Point(7,7),2);
                assertEquals(0, shipBoard.getBatteries().get(new Point(7,7)).getNumBatteries());
            }

            @Test
            void useThreeBatteries(){
                battery = new Battery(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL), 3);
                component = battery;
                addComponent(new Point(7,7));
                assertTrue(restIsUnchanged());
                shipBoard.useBatteries(new Point(7,7),3);
                assertEquals(0, shipBoard.getBatteries().get(new Point(7,7)).getNumBatteries());
            }

            @Test void noTwoBatteryThrowsException(){
                battery = new Battery(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL), 2);
                component = battery;
                assertThrows(IllegalStateException.class, () -> shipBoard.useBatteries(new Point(7,7), 2));
                assertTrue(restIsUnchanged());
            }

            @Test void noThreeBatteryThrowsException(){
                battery = new Battery(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL), 3);
                component = battery;
                assertThrows(IllegalStateException.class, () -> shipBoard.useBatteries(new Point(7,7), 2));
                assertTrue(restIsUnchanged());
            }
        }

        @Nested
        @DisplayName("Cargo Hold Tests")
        class CargoHoldTests{
            CargoHold cargo;

            @BeforeEach
            void setup(){
                cargo = new CargoHold(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL), 3, false);
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
            void noCargoHoldThrowsException(){
                assertThrows(IllegalStateException.class, () -> shipBoard.placeGoods(new Point(7,7), GoodsType.GREEN, 3));
                assertTrue(restIsUnchanged());
            }

            @Test
            void removeGoods(){
                addComponent(new Point(7,7));
                assertTrue(restIsUnchanged());
                shipBoard.placeGoods(new Point(7,7), GoodsType.GREEN, 3);
                shipBoard.removeGoods(new Point(7,7),GoodsType.GREEN,1);
                assertEquals(4, shipBoard.getGoodsValue());
            }

            @Test
            void removeGoodsThrowsException(){
                assertThrows(IllegalStateException.class, () -> shipBoard.removeGoods(new Point(7,7), GoodsType.GREEN, 2));
                assertTrue(restIsUnchanged());
            }
        }

        @Nested
        @DisplayName("Life Support Tests")
        class LifeSupportTests{
            LifeSupport lifeSupport;

            @BeforeEach
            void setup(){
                lifeSupport = new LifeSupport(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL),CrewType.PURPLE);
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
        }

        @Nested
        @DisplayName("Cabin With Connected Life Support Test")
        class CabinLifeSupportTest{
            Cabin cabin;
            LifeSupport lifeSupport;

            @BeforeEach
            void setup(){
                cabin = new Cabin(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL));
                lifeSupport = new LifeSupport(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL),CrewType.PURPLE);
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
                shipBoard.loseCrew(new Point(8,7), 1);
                assertEquals(0, shipBoard.getCrewSize());
            }
        }
    }

    @Nested
    @DisplayName("getConnectedSets() tests")
    class GetConnectedSetsTest {
        Component component;
        @BeforeEach
        void setUp() {
            component = new Component(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL));
            shipBoard = new SecondShipBoard(GameColor.BLUE);
            shipBoard.removeComponent(new Point(7,7));
            for (int i = 5; i <= 9; i++) {
                shipBoard.offerComponent(component);
                shipBoard.placeComponent(new Point(i,7),0);
            }
            shipBoard.offerComponent(component);
            shipBoard.placeComponent(new Point(7,6),0);
            shipBoard.offerComponent(component);
            shipBoard.placeComponent(new Point(7,8),0);
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




}