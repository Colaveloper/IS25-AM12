package it.polimi.ingsw.galaxytruckers.client.view.cli;

import it.polimi.ingsw.galaxytruckers.client.controller.ClientControllerInterface;
import it.polimi.ingsw.galaxytruckers.client.model.*;
import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.ComponentBank;
import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.client.model.state.*;
import it.polimi.ingsw.galaxytruckers.client.view.cli.cliScreens.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CliScreenFactoryTest {
    private CliScreenFactory factory;
    private ClientModel model;
    private ClientControllerInterface controller;

    @BeforeEach
    void setUp() {
        factory = new CliScreenFactory();
        model = mock(ClientModel.class);
        controller = mock(ClientControllerInterface.class);
    }

    @Test
    void createScreen_register() {
        CliScreen screen = factory.createScreen(MetaState.REGISTER, model, controller);
        assertInstanceOf(CliNicknameChoiceScreen.class, screen);
    }

    @Test
    void createScreen_joinOrCreate() {
        CliScreen screen = factory.createScreen(MetaState.JOINORCREATE, model, controller);
        assertInstanceOf(CliJoinOrCreateScreen.class, screen);
    }

    @Test
    void createScreen_creation() {
        CliScreen screen = factory.createScreen(MetaState.CREATION, model, controller);
        assertInstanceOf(CliGameCreationScreen.class, screen);
    }

    @Test
    void createScreen_inLobby() {
        CliScreen screen = factory.createScreen(MetaState.INLOBBY, model, controller);
        assertInstanceOf(CliLobbyScreen.class, screen);
    }

    @Test
    void createScreen_endGame() {
        CliScreen screen = factory.createScreen(MetaState.ENDGAME, model, controller);
        assertInstanceOf(CliEndGameScreen.class, screen);
    }

    @Test
    void createScreen_inGame_delegatesToGameState() {
        Game game = mock(Game.class);
        SecondShipBuildingState gameState = mock(SecondShipBuildingState.class);
        ShipBoard mockShipBoard = mock(ShipBoard.class);
        Player mockPlayer = mock(Player.class);
        FlightBoard mockFlightBoard = mock(FlightBoard.class);
        ComponentBank mockComponentBank = mock(ComponentBank.class);
        when(model.getGame()).thenReturn(game);
        when(model.getMyShip()).thenReturn(mockShipBoard);
        when(model.getPlayers()).thenReturn(Set.of(mockPlayer));
        when(mockPlayer.getShipBoard()).thenReturn(mockShipBoard);
        when(mockPlayer.getNickname()).thenReturn("TestPlayer");
        when(game.getCurrentState()).thenReturn(gameState);
        when(game.getFlightBoard()).thenReturn(mockFlightBoard);
        when(mockFlightBoard.getShipToPlace()).thenReturn(Map.of());
        when(gameState.getComponentBank()).thenReturn(mockComponentBank);
        when(mockComponentBank.getCoveredComponentsN()).thenReturn(0);
        when(gameState.getBlockedForecasts()).thenReturn(new ShipBoard[0]);
        CliScreen screen = factory.createScreen(MetaState.INGAME, model, controller);
        assertInstanceOf(CliSecondShipBuildingScreen.class, screen);
    }

    @Test
    void createScreen_shipBuilding_second() {
        SecondShipBuildingState state = mock(SecondShipBuildingState.class);
        Game mockGame = mock(Game.class);
        ShipBoard mockShipBoard = mock(ShipBoard.class);
        Player mockPlayer = mock(Player.class);
        FlightBoard mockFlightBoard = mock(FlightBoard.class);
        ComponentBank mockComponentBank = mock(ComponentBank.class);
        when(model.getGame()).thenReturn(mockGame);
        when(model.getMyShip()).thenReturn(mockShipBoard);
        when(model.getPlayers()).thenReturn(Set.of(mockPlayer));
        when(mockPlayer.getShipBoard()).thenReturn(mockShipBoard);
        when(mockPlayer.getNickname()).thenReturn("TestPlayer");
        when(mockGame.getFlightBoard()).thenReturn(mockFlightBoard);
        when(mockFlightBoard.getShipToPlace()).thenReturn(Map.of());
        when(state.getComponentBank()).thenReturn(mockComponentBank);
        when(mockComponentBank.getCoveredComponentsN()).thenReturn(0);
        when(state.getBlockedForecasts()).thenReturn(new ShipBoard[0]);
        CliScreen screen = factory.createScreen(state, model, controller);
        assertInstanceOf(CliSecondShipBuildingScreen.class, screen);
    }

    @Test
    void createScreen_shipBuilding_test() {
        TestShipBuildingState state = mock(TestShipBuildingState.class);
        Game mockGame = mock(Game.class);
        ShipBoard mockShipBoard = mock(ShipBoard.class);
        Player mockPlayer = mock(Player.class);
        FlightBoard mockFlightBoard = mock(FlightBoard.class);
        ComponentBank mockComponentBank = mock(ComponentBank.class);
        when(model.getGame()).thenReturn(mockGame);
        when(model.getMyShip()).thenReturn(mockShipBoard);
        when(model.getPlayers()).thenReturn(Set.of(mockPlayer));
        when(mockPlayer.getShipBoard()).thenReturn(mockShipBoard);
        when(mockPlayer.getNickname()).thenReturn("TestPlayer");
        when(mockGame.getFlightBoard()).thenReturn(mockFlightBoard);
        when(mockFlightBoard.getShipToPlace()).thenReturn(Map.of());
        when(state.getComponentBank()).thenReturn(mockComponentBank);
        when(mockComponentBank.getCoveredComponentsN()).thenReturn(0);
        CliScreen screen = factory.createScreen(state, model, controller);
        assertInstanceOf(CliTestShipBuildingScreen.class, screen);
    }

    @Test
    void createScreen_shipCorrection() {
        ShipCorrectionState state = mock(ShipCorrectionState.class);
        Game mockGame = mock(Game.class);
        ShipBoard mockShipBoard = mock(ShipBoard.class);
        Player mockPlayer = mock(Player.class);
        FlightBoard mockFlightBoard = mock(FlightBoard.class);
        when(model.getGame()).thenReturn(mockGame);
        when(model.getMyShip()).thenReturn(mockShipBoard);
        when(model.getPlayers()).thenReturn(Set.of(mockPlayer));
        when(mockPlayer.getShipBoard()).thenReturn(mockShipBoard);
        when(mockPlayer.getNickname()).thenReturn("TestPlayer");
        when(mockGame.getFlightBoard()).thenReturn(mockFlightBoard); // Correct type
        when(state.getAvailableActions()).thenReturn(List.of());
        when(state.getValidShipBoards()).thenReturn(Set.of(mockShipBoard));
        when(state.getShipPieces()).thenReturn(Map.of());
        CliScreen screen = factory.createScreen(state, model, controller);
        assertInstanceOf(CliValidationScreen.class, screen);
    }


    @Test
    void createScreen_shipInitialization() {
        ShipInitializationState state = mock(ShipInitializationState.class);
        Game mockGame = mock(Game.class);
        ShipBoard mockShipBoard = mock(ShipBoard.class);
        Player mockPlayer = mock(Player.class);
        FlightBoard mockFlightBoard = mock(FlightBoard.class);
        when(model.getGame()).thenReturn(mockGame);
        when(model.getMyShip()).thenReturn(mockShipBoard);
        when(model.getPlayers()).thenReturn(Set.of(mockPlayer));
        when(mockPlayer.getShipBoard()).thenReturn(mockShipBoard);
        when(mockPlayer.getNickname()).thenReturn("TestPlayer");
        when(mockGame.getFlightBoard()).thenReturn(mockFlightBoard);
        when(mockFlightBoard.getShipToPlace()).thenReturn(Map.of());
        CliScreen screen = factory.createScreen(state, model, controller);
        assertInstanceOf(CliCrewInitializationScreen.class, screen);
    }

    @Nested
    class adventureStateTests {
        private void setupAdventureStateMocks(AdventureState state) {
            Game mockGame = mock(Game.class);
            ShipBoard mockShipBoard = mock(ShipBoard.class);
            Player mockPlayer = mock(Player.class);
            FlightBoard mockFlightBoard = mock(FlightBoard.class);
            when(state.getShipBoard()).thenReturn(mockShipBoard);
            when(model.getGame()).thenReturn(mockGame);
            when(model.getMyShip()).thenReturn(mockShipBoard);
            when(model.getPlayers()).thenReturn(Set.of(mockPlayer));
            when(mockPlayer.getShipBoard()).thenReturn(mockShipBoard);
            when(mockPlayer.getNickname()).thenReturn("TestPlayer");
            when(mockGame.getFlightBoard()).thenReturn(mockFlightBoard);
            when(mockFlightBoard.getShipToPlace()).thenReturn(Map.of());
        }

        @Test
        void createScreen_adventure_activate_declareEnginePower() {
            DeclareEnginePowerState state = mock(DeclareEnginePowerState.class);
            setupAdventureStateMocks(state);
            CliScreen screen = factory.createScreen(state, model, controller);
            assertInstanceOf(CliDeclareEnginePowerScreen.class, screen);
        }

        @Test
        void createScreen_adventure_activate_declareFirePower() {
            DeclareFirePowerState state = mock(DeclareFirePowerState.class);
            setupAdventureStateMocks(state);
            CliScreen screen = factory.createScreen(state, model, controller);
            assertInstanceOf(CliDeclareFirePowerScreen.class, screen);
        }

        @Test
        void createScreen_adventure_activate_handleProjectile() {
            HandleProjectileState state = mock(HandleProjectileState.class);
            setupAdventureStateMocks(state);
            CliScreen screen = factory.createScreen(state, model, controller);
            assertInstanceOf(CliProjectilesScreen.class, screen);
        }

        @Test
        void createScreen_adventure_addGoods() {
            AddGoodsState state = mock(AddGoodsState.class);
            setupAdventureStateMocks(state);
            CliScreen screen = factory.createScreen(state, model, controller);
            assertInstanceOf(CliGoodsScreen.class, screen);
        }

        @Test
        void createScreen_adventure_choosePlanet() {
            ChoosePlanetState state = mock(ChoosePlanetState.class);
            setupAdventureStateMocks(state);
            CliScreen screen = factory.createScreen(state, model, controller);
            assertInstanceOf(CliPlanetScreen.class, screen);
        }

        @Test
        void createScreen_adventure_chooseShipPiece() {
            ChooseShipPieceState state = mock(ChooseShipPieceState.class);
            setupAdventureStateMocks(state);
            CliScreen screen = factory.createScreen(state, model, controller);
            assertInstanceOf(CliShipPieceChoiceScreen.class, screen);
        }

        @Test
        void createScreen_adventure_drawCard() {
            DrawCardState state = mock(DrawCardState.class);
            setupAdventureStateMocks(state);
            CliScreen screen = factory.createScreen(state, model, controller);
            assertInstanceOf(CliNewCardScreen.class, screen);
        }

        @Test
        void createScreen_adventure_grabReward() {
            GrabRewardState state = mock(GrabRewardState.class);
            setupAdventureStateMocks(state);
            CliScreen screen = factory.createScreen(state, model, controller);
            assertInstanceOf(CliRewardScreen.class, screen);
        }

        @Test
        void createScreen_adventure_removeCrew() {
            RemoveCrewState state = mock(RemoveCrewState.class);
            setupAdventureStateMocks(state);
            CliScreen screen = factory.createScreen(state, model, controller);
            assertInstanceOf(CliRemoveCrewScreen.class, screen);
        }

        @Test
        void createScreen_adventure_removeGoods() {
            RemoveGoodsState state = mock(RemoveGoodsState.class);
            setupAdventureStateMocks(state);
            CliScreen screen = factory.createScreen(state, model, controller);
            assertInstanceOf(CliLoseGoodsScreen.class, screen);
        }
    }
}