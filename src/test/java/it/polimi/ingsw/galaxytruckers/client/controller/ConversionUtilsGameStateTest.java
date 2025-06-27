package it.polimi.ingsw.galaxytruckers.client.controller;

import it.polimi.ingsw.galaxytruckers.server.controller.dto.states.*;
import it.polimi.ingsw.galaxytruckers.shared.enums.GameColor;
import it.polimi.ingsw.galaxytruckers.shared.enums.GoodsType;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.server.controller.dto.BuildingDataDTO;
import it.polimi.ingsw.galaxytruckers.server.controller.dto.HourglassDTO;
import it.polimi.ingsw.galaxytruckers.shared.enums.Direction;
import it.polimi.ingsw.galaxytruckers.shared.enums.ProjectileType;
import it.polimi.ingsw.galaxytruckers.client.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.client.model.Game;
import it.polimi.ingsw.galaxytruckers.client.model.factory.GameFactory;
import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.SecondShipBoard;
import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.client.model.state.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ConversionUtilsGameStateTest {
    private ConversionUtils utils;
    private ClientModel clientModel;
    private ShipBoard myShip;
    private final String testPlayerName = "testPlayer";

    @BeforeEach
    void setUp() {
        PlayerRegistry playerRegistry = new PlayerRegistry();
        utils = new ConversionUtils(playerRegistry);
        clientModel = mock(ClientModel.class);

        // Create player and shipboard properly
        var player = playerRegistry.addPlayer(testPlayerName);
        myShip = new SecondShipBoard(GameColor.GREEN);
        player.setShipBoard(myShip);

        when(clientModel.getMyShip()).thenReturn(myShip);

        Game game = mock(Game.class);
        GameFactory gameFactory = mock(GameFactory.class);
        when(clientModel.getGame()).thenReturn(game);
        when(game.getGameFactory()).thenReturn(gameFactory);
        when(gameFactory.createShipBuildingState()).thenReturn(new TestShipBuildingState());
    }

    @Nested
    class SimpleStateDTOTests {
        @Test
        void declareEnginePowerState_SetsCorrectProperties() {
            SimpleStateDTO dto = new SimpleStateDTO(testPlayerName, StateDTOType.DECLARE_ENGINE_POWER);
            var state = (DeclareEnginePowerState) utils.getGameState((StateDTO) dto, clientModel);

            assertNotNull(state);
            assertEquals(myShip, state.getShipBoard());
        }

        @Test
        void declareFirePowerState_SetsCorrectProperties() {
            SimpleStateDTO dto = new SimpleStateDTO(testPlayerName, StateDTOType.DECLARE_FIRE_POWER);
            var state = (DeclareFirePowerState) utils.getGameState((StateDTO) dto, clientModel);

            assertNotNull(state);
            assertEquals(myShip, state.getShipBoard());
        }

        @Test
        void drawCardState_SetsCorrectProperties() {
            SimpleStateDTO dto = new SimpleStateDTO(testPlayerName, StateDTOType.DRAW_CARD);
            var state = (DrawCardState) utils.getGameState((StateDTO) dto, clientModel);

            assertNotNull(state);
            assertEquals(myShip, state.getShipBoard());
        }

        @Test
        void grabRewardState_SetsCorrectProperties() {
            SimpleStateDTO dto = new SimpleStateDTO(testPlayerName, StateDTOType.GRAB_REWARD);
            var state = (GrabRewardState) utils.getGameState((StateDTO) dto, clientModel);

            assertNotNull(state);
            assertEquals(myShip, state.getShipBoard());
        }
    }

    @Nested
    class ComplexStateDTOTests {
        @Test
        void complexChoosePlanetState_SetsCorrectProperties() {
            String[] choices = {testPlayerName};
            ComplexChoosePlanetDTO dto = new ComplexChoosePlanetDTO(
                new ChoosePlanetDTO(testPlayerName, 1),
                choices
            );

            var state = (ChoosePlanetState) utils.getGameState(dto, clientModel);

            assertNotNull(state);
            assertEquals(myShip, state.getShipBoard());
            assertArrayEquals(new ShipBoard[]{myShip}, state.getOptions());
        }

        @Test
        void complexDrawCardState_SetsCorrectProperties() {
            ComplexDrawCardDTO dto = new ComplexDrawCardDTO(testPlayerName, true);
            var state = (DrawCardState) utils.getGameState(dto, clientModel);

            assertNotNull(state);
            assertEquals(myShip, state.getShipBoard());
            assertTrue(state.hasDrawn());
        }

        @Test
        void secondShipBuildingState_SetsCorrectProperties() {
            Map<String, Integer> blockedForecasts = Map.of(testPlayerName, 1);
            HourglassDTO hourglass = new HourglassDTO(3, 60000, true);
            BuildingDataDTO buildingData = new BuildingDataDTO(5, List.of(1), Set.of(testPlayerName));

            SecondShipBuildingDTO dto = new SecondShipBuildingDTO(buildingData, hourglass, blockedForecasts);
            var state = (SecondShipBuildingState) utils.getGameState(dto, clientModel);

            assertNotNull(state);
            assertEquals(myShip, state.getBlockedForecasts()[1]);
        }

        @Test
        void testShipBuildingState_SetsCorrectProperties() {
            BuildingDataDTO buildingData = new BuildingDataDTO(5, List.of(1), Set.of(testPlayerName));
            TestShipBuildingDTO dto = new TestShipBuildingDTO(buildingData);
            var state = (TestShipBuildingState) utils.getGameState(dto, clientModel);

            assertNotNull(state);
            assertEquals(5, state.getComponentBank().getCoveredComponentsN());
            assertEquals(1, state.getComponentBank().getUncoveredComponents().size());
        }
    }

    @Nested
    class ActionStateDTOTests {
        @Test
        void addGoodsState_SetsCorrectProperties() {
            Map<GoodsType, Integer> goodsBuffer = Map.of(GoodsType.RED, 2);
            AddGoodsDTO dto = new AddGoodsDTO(testPlayerName, goodsBuffer);
            var state = (AddGoodsState) utils.getGameState((ComplexStateDTO) dto, clientModel);

            assertNotNull(state);
            assertEquals(myShip, state.getShipBoard());
        }

        @Test
        void chooseShipPieceState_SetsCorrectProperties() {
            List<Set<Point>> shipPieces = List.of();
            ChooseShipPieceDTO dto = new ChooseShipPieceDTO(testPlayerName, shipPieces);
            var state = (ChooseShipPieceState) utils.getGameState((ComplexStateDTO) dto, clientModel);

            assertNotNull(state);
            assertEquals(myShip, state.getShipBoard());
            assertEquals(shipPieces, state.getShipPieces());
        }

        @Test
        void removeCrewState_SetsCorrectProperties() {
            RemoveCrewDTO dto = new RemoveCrewDTO(testPlayerName, 1);
            var state = (RemoveCrewState) utils.getGameState((ComplexStateDTO) dto, clientModel);

            assertNotNull(state);
            assertEquals(myShip, state.getShipBoard());
        }

        @Test
        void removeGoodsState_SetsCorrectProperties() {
            RemoveGoodsDTO dto = new RemoveGoodsDTO(testPlayerName, 1);
            var state = (RemoveGoodsState) utils.getGameState((ComplexStateDTO) dto, clientModel);

            assertNotNull(state);
            assertEquals(myShip, state.getShipBoard());
        }

        @Test
        void handleProjectileState_SetsCorrectProperties() {
            var diceRoll = 3;
            var direction = Direction.UP;
            var projectileType = ProjectileType.SMALLMETEOR;
            var availablePoints = Set.of(new Point(1, 1));

            HandleProjectileDTO dto = new HandleProjectileDTO(testPlayerName, projectileType, diceRoll, direction, availablePoints);
            var state = (HandleProjectileState) utils.getGameState((ComplexStateDTO) dto, clientModel);

            assertNotNull(state);
            assertEquals(myShip, state.getShipBoard());
        }
    }

    @Nested
    class ShipStateDTOTests {
        @Test
        void shipInitializationState_SetsCorrectProperties() {
            Map<String, Map<CrewType, Set<Point>>> crewToCabins =
                Map.of(testPlayerName, Map.of(CrewType.HUMAN, Set.of()));
            ShipInitializationDTO dto = new ShipInitializationDTO(crewToCabins);
            var state = (ShipInitializationState) utils.getGameState((ComplexStateDTO) dto, clientModel);

            assertNotNull(state);
        }

        @Test
        void shipCorrectionState_SetsCorrectProperties() {
            Set<String> validShips = Set.of(testPlayerName);
            Map<String, List<Set<Point>>> shipPieces = Map.of(testPlayerName, List.of());
            ShipCorrectionDTO dto = new ShipCorrectionDTO(validShips, shipPieces, true);
            var state = (ShipCorrectionState) utils.getGameState((ComplexStateDTO) dto, clientModel);

            assertNotNull(state);
            assertTrue(state.getShipPieces().containsKey(myShip));
        }

        @Test
        void shipBuildingState_SetsCorrectProperties() {
            ShipBuildingDTO dto = new ShipBuildingDTO();
            var state = (ShipBuildingState) utils.getGameState(dto, clientModel);

            assertNotNull(state);
            // Since we mocked gameFactory to return TestShipBuildingState
            assertInstanceOf(TestShipBuildingState.class, state);
        }
    }
}
