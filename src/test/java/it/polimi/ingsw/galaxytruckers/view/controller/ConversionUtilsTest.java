package it.polimi.ingsw.galaxytruckers.view.controller;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.serverController.dto.BuildingDataDTO;
import it.polimi.ingsw.galaxytruckers.serverController.dto.ComponentDTO;
import it.polimi.ingsw.galaxytruckers.serverController.dto.components.ActivatablePayload;
import it.polimi.ingsw.galaxytruckers.serverController.dto.components.BatteryPayload;
import it.polimi.ingsw.galaxytruckers.serverController.dto.components.CabinPayload;
import it.polimi.ingsw.galaxytruckers.serverController.dto.components.CargoPayload;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import it.polimi.ingsw.galaxytruckers.view.model.Player;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.*;
import it.polimi.ingsw.galaxytruckers.view.model.state.SecondShipBuildingState;
import it.polimi.ingsw.galaxytruckers.view.model.state.ShipBuildingState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ConversionUtilsTest {
    private PlayerRegistry playerRegistry;
    private Player player;
    private ConversionUtils utils;

    @BeforeEach
    void setUp() {
        playerRegistry = new PlayerRegistry();
        player = playerRegistry.addPlayer("testPlayer");
        utils = new ConversionUtils(playerRegistry);
    }

    @Test
    void convertPlayerMap() {
        Map<String, Integer> scores = Map.of("testPlayer", 10);
        Map<Player, Integer> convertedMap = utils.convertPlayerMap(scores);

        assertNotNull(convertedMap);
        assertTrue(convertedMap.containsKey(player));
        assertEquals(10, convertedMap.get(player));
    }

    @Test
    void convertMap_convertsStringToShipBoardMap() {
        Map<String, Integer> input = Map.of("testPlayer", 99);
        Map<ShipBoard, Integer> result = utils.convertMap(input);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.containsKey(player.getShipBoard()));
        assertEquals(99, result.get(player.getShipBoard()));
    }

    @Test
    void convertCollection_convertsNicknamesToShipBoards() {
        java.util.List<String> names = java.util.List.of("testPlayer");
        java.util.List<ShipBoard> boards = utils.convertCollection(names, java.util.ArrayList::new);
        assertNotNull(boards);
        assertEquals(1, boards.size());
        assertEquals(player.getShipBoard(), boards.get(0));
    }

    @Test
    void convertArray_convertsNicknamesArrayToShipBoardArray() {
        String[] names = {"testPlayer"};
        ShipBoard[] boards = utils.convertArray(names);
        assertNotNull(boards);
        assertEquals(1, boards.length);
        assertEquals(player.getShipBoard(), boards[0]);
    }

    @Test
    void convertBuildingData_setsShipBuildingStateCorrectly() {
        // Prepare test data
        Set<String> completedNames = new HashSet<>(Set.of("testPlayer"));
        List<Integer> uncoveredIds = List.of(1); // Assumes id 1 exists in registry
        int numCovered = 5;
        BuildingDataDTO dto = new BuildingDataDTO(numCovered, uncoveredIds, completedNames);
        ShipBuildingState state = new SecondShipBuildingState();
        // Call method
        utils.convertBuildingData(state, dto);
        // Assert component bank
        ComponentBank bank = state.getComponentBank();
        assertEquals(numCovered, bank.getCoveredComponentsN());
        assertEquals(uncoveredIds.size(), bank.getUncoveredComponents().size());
        assertEquals(1, bank.getUncoveredComponents().get(0).getId());
    }

    @Nested
    class ComponentConversionTests {
        private ConversionUtils utils;
        private int batteryId;
        private int cabinId;
        private int cargoHoldId;
        private int shieldId;
        private int engineId;
        private int doubleCannonId;
        private int lifeSupportId;

        @BeforeEach
        void setUp() {
            playerRegistry = new PlayerRegistry();
            player = playerRegistry.addPlayer("testPlayer");
            utils = new ConversionUtils(playerRegistry);

            // Get valid component IDs from registry
            batteryId = ComponentTestUtils.findFirstComponentIdOfType(Battery.class);
            cabinId = ComponentTestUtils.findFirstComponentIdOfType(Cabin.class);
            cargoHoldId = ComponentTestUtils.findFirstComponentIdOfType(CargoHold.class);
            shieldId = ComponentTestUtils.findFirstComponentIdOfType(Shield.class);
            engineId = ComponentTestUtils.findFirstComponentIdOfType(Engine.class);
            doubleCannonId = ComponentTestUtils.findFirstComponentIdOfType(DoubleCannon.class);
            lifeSupportId = ComponentTestUtils.findFirstComponentIdOfType(LifeSupport.class);
        }

        @Test
        void convertComponent_Battery_SetsCorrectProperties() {
            ComponentDTO batteryDto = new ComponentDTO(batteryId, Direction.RIGHT,
                new BatteryPayload(3));
            Component batteryComponent = utils.convertComponent(batteryDto);

            assertNotNull(batteryComponent);
            assertInstanceOf(Battery.class, batteryComponent);
            assertEquals(3, ((Battery) batteryComponent).getNumBatteries());
            assertEquals(Direction.RIGHT, batteryComponent.getOrientation());
        }

        @Test
        void convertComponent_Cabin_SetsCorrectProperties() {
            ComponentDTO cabinDto = new ComponentDTO(cabinId, Direction.LEFT,
                new CabinPayload(CrewType.HUMAN, 2));
            Component cabinComponent = utils.convertComponent(cabinDto);

            assertNotNull(cabinComponent);
            assertInstanceOf(Cabin.class, cabinComponent);
            var cabin = (Cabin) cabinComponent;
            assertEquals(CrewType.HUMAN, cabin.getCrewType());
            assertEquals(2, cabin.getNumResidents());
            assertEquals(Direction.LEFT, cabinComponent.getOrientation());
        }

        @Test
        void convertComponent_CargoHold_SetsCorrectProperties() {
            Map<GoodsType, Integer> goodsMap = Map.of(GoodsType.RED, 5);
            ComponentDTO cargoDto = new ComponentDTO(cargoHoldId, Direction.UP,
                new CargoPayload(goodsMap));
            Component cargoComponent = utils.convertComponent(cargoDto);

            assertNotNull(cargoComponent);
            assertInstanceOf(CargoHold.class, cargoComponent);
            assertEquals(goodsMap, ((CargoHold) cargoComponent).getGoods());
            assertEquals(Direction.UP, cargoComponent.getOrientation());
        }

        @Test
        void convertComponent_Shield_SetsCorrectProperties() {
            ComponentDTO shieldDto = new ComponentDTO(shieldId, Direction.DOWN,
                new ActivatablePayload(true));
            Component shieldComponent = utils.convertComponent(shieldDto);

            assertNotNull(shieldComponent);
            assertInstanceOf(Shield.class, shieldComponent);
            assertInstanceOf(Activatable.class, shieldComponent);
            assertTrue(((Activatable) shieldComponent).isActive());
            assertEquals(Direction.DOWN, shieldComponent.getOrientation());
        }

        @Test
        void convertComponent_Engine_SetsCorrectProperties() {
            ComponentDTO engineDto = new ComponentDTO(engineId, Direction.RIGHT,
                new ActivatablePayload(false));
            Component engineComponent = utils.convertComponent(engineDto);

            assertNotNull(engineComponent);
            assertInstanceOf(Engine.class, engineComponent);
            assertEquals(Direction.RIGHT, engineComponent.getOrientation());
        }

        @Test
        void convertComponent_DoubleCannon_SetsCorrectProperties() {
            ComponentDTO cannonDto = new ComponentDTO(doubleCannonId, Direction.LEFT,
                new ActivatablePayload(false));
            Component cannonComponent = utils.convertComponent(cannonDto);

            assertNotNull(cannonComponent);
            assertInstanceOf(DoubleCannon.class, cannonComponent);
            assertInstanceOf(Activatable.class, cannonComponent);
            assertEquals(Direction.LEFT, cannonComponent.getOrientation());
            assertFalse(((Activatable) cannonComponent).isActive());
        }

        @Test
        void convertComponent_LifeSupport_SetsCorrectProperties() {
            ComponentDTO lifeSupportDto = new ComponentDTO(lifeSupportId, Direction.UP,
                new CabinPayload(CrewType.PURPLE, 0));
            Component lifeSupportComponent = utils.convertComponent(lifeSupportDto);

            assertNotNull(lifeSupportComponent);
            assertInstanceOf(LifeSupport.class, lifeSupportComponent);
            assertEquals(Direction.UP, lifeSupportComponent.getOrientation());
        }
    }
}
