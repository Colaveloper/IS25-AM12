package it.polimi.ingsw.galaxytruckers.client.controller;

import it.polimi.ingsw.galaxytruckers.server.controller.events.types.*;
import it.polimi.ingsw.galaxytruckers.shared.enums.GameColor;
import it.polimi.ingsw.galaxytruckers.shared.enums.GoodsType;
import it.polimi.ingsw.galaxytruckers.shared.enums.Level;
import it.polimi.ingsw.galaxytruckers.shared.enums.SurrenderCause;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.server.controller.dto.ActiveLobbyDTO;
import it.polimi.ingsw.galaxytruckers.server.controller.dto.LobbyDetailsDTO;
import it.polimi.ingsw.galaxytruckers.shared.enums.Direction;
import it.polimi.ingsw.galaxytruckers.client.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.client.model.MetaState;
import it.polimi.ingsw.galaxytruckers.client.model.Player;
import it.polimi.ingsw.galaxytruckers.client.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.SecondShipBoard;
import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.ShipBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.mockito.Mockito.*;

class ClientEventHandlerTest {
    private ClientController clientController;
    private ClientModel clientModel;
    private PlayerRegistry playerRegistry;
    private ClientEventHandler handler;
    private final String testPlayerName = "testPlayer";
    private Player testPlayer;
    private ShipBoard testShipBoard;

    @BeforeEach
    void setUp() {
        clientController = mock(ClientController.class);
        clientModel = mock(ClientModel.class);
        playerRegistry = new PlayerRegistry();
        ConversionUtils conversionUtils = new ConversionUtils(playerRegistry);
        handler = new ClientEventHandler(clientController, clientModel, playerRegistry);

        // Set up a test player
        testPlayer = playerRegistry.addPlayer(testPlayerName);
        testPlayer.setShipBoard(new SecondShipBoard(GameColor.GREEN));
        testShipBoard = testPlayer.getShipBoard();
        ;
    }

    @Test
    void handleActivateComponentEvent() {
        Point point = new Point(1, 1);
        ActivateComponentEvent event = new ActivateComponentEvent(testPlayerName, point, true);

        handler.handleEvent(event);

        verify(clientModel).notifyActivateComponent(testShipBoard, point);
    }

    @Test
    void handleFlightBoardUpdateEvent() {
        int position = 5;
        FlightBoardUpdateEvent event = new FlightBoardUpdateEvent(testPlayerName, position);

        handler.handleEvent(event);

        verify(clientModel).notifyFlightBoardPosition(testShipBoard, position);
    }

    @Test
    void handleFlipHourglassEvent() {
        FlipHourglassEvent event = new FlipHourglassEvent(testPlayerName);

        handler.handleEvent(event);

        verify(clientModel).notifyFlipHourglass(testShipBoard);
    }

    @Test
    void handleForecastDetailsEvent() {
        List<Integer> forecastDeckIds = List.of(1, 2, 3);
        ForecastDetailsEvent event = new ForecastDetailsEvent("", forecastDeckIds);

        handler.handleEvent(event);

        verify(clientModel).setForecastDeck(anyList());
    }

    @Test
    void handleGameEndEvent() {
        Map<String, Integer> scores = Map.of(testPlayerName, 100);
        GameEndEvent event = new GameEndEvent(scores);

        handler.handleEvent(event);

        verify(clientModel).setFinalScores(argThat(map ->
            map.containsKey(testPlayer) && map.get(testPlayer) == 100
        ));
        verify(clientModel).setMetaState(MetaState.ENDGAME);
    }

    @Test
    void handleGoodsUpdateEvent_Add() {
        Point point = new Point(1, 1);
        GoodsType goodsType = GoodsType.RED;
        GoodsUpdateEvent event = new GoodsUpdateEvent(testPlayerName, point, goodsType, true);

        handler.handleEvent(event);

        verify(clientModel).notifyPlaceGoods(testShipBoard, point, goodsType);
    }

    @Test
    void handleGoodsUpdateEvent_Remove() {
        Point point = new Point(1, 1);
        GoodsType goodsType = GoodsType.RED;
        GoodsUpdateEvent event = new GoodsUpdateEvent(testPlayerName, point, goodsType, false);

        handler.handleEvent(event);

        verify(clientModel).notifyRemoveGoods(testShipBoard, point, goodsType);
    }

    @Test
    void handleGrabStashedComponentEvent() {
        int index = 2;
        GrabStashedComponentEvent event = new GrabStashedComponentEvent(testPlayerName, index);

        handler.handleEvent(event);

        verify(clientModel).notifyGrabStashedComponent(testShipBoard, index);
    }

    @Test
    void handleHourglassEndEvent() {
        HourglassEndEvent event = new HourglassEndEvent();

        handler.handleEvent(event);

        verify(clientModel).notifyHourglassEnd();
    }

    @Test
    void handleInitializeCabinEvent() {
        Point point = new Point(1, 1);
        CrewType crewType = CrewType.HUMAN;
        InitializeCabinEvent event = new InitializeCabinEvent(testPlayerName, point, crewType);

        handler.handleEvent(event);

        verify(clientModel).notifyInitializeCabin(testShipBoard, point, crewType);
    }

    @Test
    void handleJoinLobbyEvent() {
        GameColor color = GameColor.GREEN;
        JoinLobbyEvent event = new JoinLobbyEvent("newPlayer", color);

        handler.handleEvent(event);

        verify(clientModel).addPlayer(
            argThat(player -> player.getNickname().equals("newPlayer")),
            eq(color)
        );
    }

    @Test
    void handleLobbyDetailsEvent() {
        UUID lobbyId = UUID.randomUUID();
        String host = "host";
        Level level = Level.TEST;
        Map<String, GameColor> playerColors = Map.of(testPlayerName, GameColor.GREEN);
        LobbyDetailsDTO details = new LobbyDetailsDTO(lobbyId, playerColors, level, 1);
        LobbyDetailsEvent event = new LobbyDetailsEvent(host, details);

        handler.handleEvent(event);

        verify(clientModel).createGame(level);
        verify(clientModel).addPlayer(
            argThat(player -> player.getNickname().equals(testPlayerName)),
            eq(GameColor.GREEN)
        );
        verify(clientModel).setMetaState(MetaState.INLOBBY);
    }

    @Test
    void handleNewCardEvent() {
        int cardId = 1;
        NewCardEvent event = new NewCardEvent(cardId);

        handler.handleEvent(event);

        verify(clientModel).notifyDrawCard(any(AdventureCard.class));
    }

    @Test
    void handlePeekForecastEvent() {
        int forecastIndex = 2;
        PeekForecastEvent event = new PeekForecastEvent(testPlayerName, forecastIndex);

        handler.handleEvent(event);

        verify(clientModel).notifyPeekForecast(testShipBoard, forecastIndex);
    }

    @Test
    void handlePlaceComponentEvent() {
        Point position = new Point(1, 1);
        Direction rotation = Direction.UP;
        PlaceComponentEvent event = new PlaceComponentEvent(testPlayerName, position, rotation);

        handler.handleEvent(event);

        verify(clientModel).notifyPlaceComponent(testShipBoard, position, rotation);
    }

    @Test
    void handlePlanetChoiceEvent() {
        int planetIndex = 2;
        String nextPlayerName = "nextPlayer";
        PlanetChoiceEvent event = new PlanetChoiceEvent(testPlayerName, planetIndex, nextPlayerName);

        // Add next player to registry
        Player nextPlayer = playerRegistry.addPlayer(nextPlayerName);
        nextPlayer.setShipBoard(new SecondShipBoard(GameColor.GREEN));

        handler.handleEvent(event);

        verify(clientModel).notifyChoosePlanet(testShipBoard, planetIndex, nextPlayer.getShipBoard());
    }

    @Test
    void handleRejectComponentEvent() {
        RejectComponentEvent event = new RejectComponentEvent(testPlayerName);

        handler.handleEvent(event);

        verify(clientModel).notifyRejectComponent(testShipBoard);
    }

    @Test
    void handleReleaseForecastEvent() {
        ReleaseForecastEvent event = new ReleaseForecastEvent(testPlayerName, 0);

        handler.handleEvent(event);

        verify(clientModel).notifyReleaseForecast(testShipBoard);
    }

    @Test
    void handleRemoveComponentEvent() {
        Point point = new Point(1, 1);
        RemoveComponentEvent event = new RemoveComponentEvent(testPlayerName, point);

        handler.handleEvent(event);

        verify(clientModel).notifyRemoveComponent(testShipBoard, point);
    }

    @Test
    void handleRequestFaceDownComponentEvent() {
        int componentId = 1;
        RequestFaceDownComponentEvent event = new RequestFaceDownComponentEvent(testPlayerName, componentId);

        handler.handleEvent(event);

        verify(clientModel).notifyRequestRandComponent(eq(testShipBoard), any(Component.class));
    }

    @Test
    void handleRequestFaceUpComponentEvent() {
        int componentId = 1;
        RequestFaceUpComponentEvent event = new RequestFaceUpComponentEvent(testPlayerName, componentId);

        handler.handleEvent(event);

        verify(clientModel).notifyRequestComponent(eq(testShipBoard), any(Component.class));
    }

    @Test
    void handleShipNotConnectedEvent() {
        List<Set<Point>> shipPieces = List.of(Set.of(new Point(1, 1)));
        ShipNotConnectedEvent event = new ShipNotConnectedEvent(testPlayerName, shipPieces);

        handler.handleEvent(event);

        verify(clientModel).notifyShipNotConnected(testShipBoard, shipPieces);
    }

    @Test
    void handleShipPieceRemoveEvent() {
        int index = 1;
        ShipPieceRemoveEvent event = new ShipPieceRemoveEvent(testPlayerName, index);

        handler.handleEvent(event);

        verify(clientModel).notifyChooseShipPiece(testShipBoard, index);
    }

    @Test
    void handleStashComponentEvent() {
        StashComponentEvent event = new StashComponentEvent(testPlayerName);

        handler.handleEvent(event);

        verify(clientModel).notifyStashComponent(testShipBoard);
    }

    @Test
    void handleSurrenderEvent() {
        List<String> playerNames = List.of(testPlayerName);
        SurrenderEvent event = new SurrenderEvent(playerNames);

        handler.handleEvent(event);

        verify(clientModel).notifySurrenderShip(
            argThat(shipSet -> shipSet.size() == 1 && shipSet.contains(testShipBoard))
        );
    }

    @Test
    void handleUseBatteryEvent() {
        Point point = new Point(1, 1);
        UseBatteryEvent event = new UseBatteryEvent(testPlayerName, point);

        handler.handleEvent(event);

        verify(clientModel).notifyUseBattery(testShipBoard, point);
    }

    @Test
    void handleValidateShipEvent() {
        ValidateShipEvent event = new ValidateShipEvent(testPlayerName);

        handler.handleEvent(event);

        verify(clientModel).notifyShipValidated(testShipBoard);
    }

    @Test
    void handleLoseCrewEvent() {
        Point point = new Point(1, 1);
        LoseCrewEvent event = new LoseCrewEvent(testPlayerName, point);

        handler.handleEvent(event);

        verify(clientModel).notifyLoseCrew(testShipBoard, point);
    }

    @Test
    void handleAddActiveLobbyEvent() {
        UUID lobbyId = UUID.randomUUID();
        String host = "host";
        Level level = Level.TEST;
        int numPlayers = 1;

        ActiveLobbyDTO dto = new ActiveLobbyDTO(lobbyId, level, 1, List.of(host),  host);
        AddActiveLobbyEvent event = new AddActiveLobbyEvent(dto);

        handler.handleEvent(event);

        verify(clientModel).notifyNewLobby(argThat(lobby ->
            lobby.getId().equals(lobbyId) &&
            lobby.getPlayersN() == numPlayers &&
            lobby.getLevel() == level &&
            lobby.getHost().equals(host)
        ));
    }

    @Test
    void handleRemoveActiveLobbyEvent() {
        UUID lobbyId = UUID.randomUUID();
        RemoveActiveLobbyEvent event = new RemoveActiveLobbyEvent(lobbyId);

        handler.handleEvent(event);

        verify(clientModel).notifyRemoveLobby(lobbyId);
    }

    @Test
    void handleSetActiveLobbiesEvent() {
        List<ActiveLobbyDTO> lobbies = List.of(
            new ActiveLobbyDTO(UUID.randomUUID(), Level.TEST, 1, List.of("host"), "host")
        );
        SetActiveLobbiesEvent event = new SetActiveLobbiesEvent(testPlayerName, lobbies, false);

        handler.handleEvent(event);

        verify(clientModel).setPlayer(testPlayer);
        verify(clientModel).notifyNewLobby(any());
        verify(clientModel).setMetaState(MetaState.JOINORCREATE);
    }

    @Test
    void handleCurrentPlayerUpdateEvent() {
        CurrentPlayerUpdateEvent event = new CurrentPlayerUpdateEvent(testPlayerName);

        handler.handleEvent(event);

        verify(clientModel).notifyCurrentPlayerUpdate(testShipBoard);
    }

    @Test
    void handleSurrenderRequestEvent() {
        SurrenderRequestEvent event = new SurrenderRequestEvent(testPlayerName, SurrenderCause.REQUEST);

        handler.handleEvent(event);

        verify(clientModel).notifySurrenderRequest(testPlayer);
    }

    @Test
    void handleGrabPlacedComponentEvent() {
        GrabPlacedComponentEvent event = new GrabPlacedComponentEvent(testPlayerName);

        handler.handleEvent(event);

        verify(clientModel).notifyGrabPlacedComponent(testShipBoard);
    }
}
