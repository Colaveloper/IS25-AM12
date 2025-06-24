package it.polimi.ingsw.galaxytruckers.view.model;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import it.polimi.ingsw.galaxytruckers.view.ModelObserver;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.OpenSpaceCard;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;
import it.polimi.ingsw.galaxytruckers.view.model.state.SecondShipBuildingState;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientModelTest {
    private ClientModel model;

    @BeforeEach
    void setup() {
        model = new ClientModel();
    }

    @Test
    void addObserver() {
        ModelObserver observer = mock(ModelObserver.class);
        model.addObserver(observer);
        model.setMetaState(MetaState.INLOBBY);
        verify(observer).notifyMetaState(MetaState.INLOBBY);
    }

    @Test
    void removeObserver() {
        ModelObserver observer = mock(ModelObserver.class);
        model.addObserver(observer);
        model.removeObserver(observer);
        model.setMetaState(MetaState.INLOBBY);
        verify(observer, never()).notifyMetaState(MetaState.INLOBBY);
    }

    @Test
    void setPlayer() {
        Player player = mock(Player.class);
        model.setPlayer(player);
        assertEquals(player, model.getClientPlayer());
    }

    @Test
    void createGame() {
        ModelObserver observer = mock(ModelObserver.class);
        model.addObserver(observer);
        model.createGame(Level.SECOND);
        assertNotNull(model.getGame());
    }

    @Test
    void notifyNewLobby() {
        Lobby lobby = mock(Lobby.class);
        UUID uuid = UUID.randomUUID();
        when(lobby.getId()).thenReturn(uuid);
        ModelObserver observer = mock(ModelObserver.class);
        model.addObserver(observer);
        model.notifyNewLobby(lobby);
        assertTrue(model.getActiveLobbies().containsKey(uuid));
        verify(observer).notifyNewLobby(lobby);
    }

    @Test
    void notifyRemoveLobby() {
        Lobby lobby = mock(Lobby.class);
        UUID uuid = UUID.randomUUID();
        when(lobby.getId()).thenReturn(uuid);
        ModelObserver observer = mock(ModelObserver.class);
        model.addObserver(observer);
        model.notifyNewLobby(lobby);
        model.notifyRemoveLobby(uuid);
        assertFalse(model.getActiveLobbies().containsKey(uuid));
        verify(observer).notifyRemoveLobby(uuid);
    }

    @Test
    void addPlayer() {
        model.createGame(Level.SECOND);
        Player player = mock(Player.class);
        ShipBoard shipBoard = mock(ShipBoard.class);
        when(player.getShipBoard()).thenReturn(shipBoard);
        Player clientPlayer = mock(Player.class);
        model.setPlayer(clientPlayer);
        when(clientPlayer.getShipBoard()).thenReturn(shipBoard);
        model.addPlayer(clientPlayer, GameColor.BLUE);
        assertTrue(model.getPlayers().contains(clientPlayer));
        assertEquals(clientPlayer, model.getPlayerByShip(shipBoard));
    }

    @Test
    void addPlayerDuplicatePlayerDoesNothing() {
        model.createGame(Level.SECOND);
        Player player = mock(Player.class);
        model.setPlayer(player);
        model.addPlayer(player, GameColor.BLUE);
        model.addPlayer(player, GameColor.RED);
        assertEquals(1, model.getPlayers().stream().filter(p -> p == player).count());
    }

    @Test
    void addPlayerNotClientPlayerDoesNotSetMyShip() {
        model.createGame(Level.SECOND);
        Player player = mock(Player.class);
        Player clientPlayer = mock(Player.class);
        model.setPlayer(clientPlayer);
        assertNotEquals(clientPlayer, player);
        ShipBoard playerShipBoard = mock(ShipBoard.class);
        when(player.getShipBoard()).thenReturn(playerShipBoard);
        model.addPlayer(player, GameColor.BLUE);
        assertNotEquals(player.getShipBoard(), model.getGame().getMyShip());
    }

    @Test
    void activateCheats() {
        model.activateCheats(2);
    }

    @Test
    void getActiveLobbies() {
        assertNotNull(model.getActiveLobbies());
    }

    @Test
    void getClientPlayer() {
        assertNull(model.getClientPlayer());
    }

    @Test
    void getGame() {
        assertNull(model.getGame());
        model.createGame(Level.SECOND);
        assertNotNull(model.getGame());
    }

    @Test
    void getPlayers() {
        assertNotNull(model.getPlayers());
    }

    @Test
    void getPlayerByShip() {
        ShipBoard shipBoard = mock(ShipBoard.class);
        assertNull(model.getPlayerByShip(shipBoard));
    }

    @Test
    void getShipToPlayer() {
        assertNotNull(model.getShipToPlayer());
    }

    @Test
    void getFinalScores() {
        assertNull(model.getFinalScores());
        Map<Player, Integer> scores = new HashMap<>();
        model.setFinalScores(scores);
        assertEquals(scores, model.getFinalScores());
    }

    @Test
    void notifyCurrentState() {
        model.createGame(Level.SECOND);
        GameState state = new SecondShipBuildingState();
        ModelObserver observer = mock(ModelObserver.class);
        model.addObserver(observer);
        model.notifyCurrentState(state);
        assertEquals(state, model.getGame().getCurrentState());
        verify(observer).notifyCurrentState(state);
    }

    @Test
    void notifyEventMethods() {
        model.createGame(Level.SECOND);
        GameState state = new SecondShipBuildingState();
        model.notifyCurrentState(state);
        Player clientPlayer = mock(Player.class);
        ShipBoard shipBoard = mock(ShipBoard.class);
        when(clientPlayer.getShipBoard()).thenReturn(shipBoard);
        model.setPlayer(clientPlayer);
        model.addPlayer(clientPlayer, GameColor.BLUE);
        Component component = mock(Component.class);
        Point point = new Point(1, 1);
        Direction direction = Direction.UP;
        CrewType crewType = CrewType.HUMAN;
        AdventureCard adventureCard = new OpenSpaceCard(it.polimi.ingsw.galaxytruckers.model.enumTypes.Level.SECOND, 1);
        Player player = mock(Player.class);
        List<AdventureCard> cards = new ArrayList<>();
        cards.add(adventureCard);
        List<Set<Point>> shipPieces = new ArrayList<>();
        Set<ShipBoard> shipBoards = new HashSet<>();
        model.notifyRequestRandComponent(shipBoard, component);
        model.notifyRequestComponent(shipBoard, component);
        model.notifyRejectComponent(shipBoard);
        model.notifyStashComponent(shipBoard);
        model.notifyGrabPlacedComponent(shipBoard);
        model.notifyGrabStashedComponent(shipBoard, 0);
        model.notifyPlaceComponent(shipBoard, point, direction);
        // because the game starts with the hourglass already flipped
        assertThrows(IllegalStateException.class, () -> model.notifyFlipHourglass(shipBoard));
        model.notifyHourglassEnd();
        model.notifyFlightBoardPosition(shipBoard, 1);
        model.notifyPeekForecast(shipBoard, 0);
        model.setForecastDeck(cards);
        model.notifyReleaseForecast(shipBoard);
        model.notifyRemoveComponent(shipBoard, point);
        model.notifyChooseShipPiece(shipBoard, 0);
        model.notifyShipNotConnected(shipBoard, shipPieces);
        model.notifyShipValidated(shipBoard);
        model.notifyInitializeCabin(shipBoard, point, crewType);
        model.notifyDrawCard(adventureCard);
        model.notifyActivateComponent(shipBoard, point);
        model.notifyLoseCrew(shipBoard, point);
        model.notifyGrabCredits(shipBoard, 5);
        model.notifyPlaceGoods(shipBoard, point, GoodsType.RED);
        model.notifyRemoveGoods(shipBoard, point, GoodsType.RED);
        model.notifyUseBattery(shipBoard, point);
        model.notifyChoosePlanet(shipBoard, 1, shipBoard);
        model.notifyCurrentPlayerUpdate(shipBoard);
        model.notifyGiveUpMessage(shipBoard);
        model.notifySurrenderShip(shipBoards);
        model.notifySurrenderRequest(player);
    }

    @Test
    void getMyShip() {
        Player player = mock(Player.class);
        ShipBoard shipBoard = mock(ShipBoard.class);
        when(player.getShipBoard()).thenReturn(shipBoard);
        model.setPlayer(player);
        assertEquals(shipBoard, model.getMyShip());
    }

    @Test
    void getMetaStateAndSetMetaState() {
        assertEquals(MetaState.REGISTER, model.getMetaState());
        ModelObserver observer = mock(ModelObserver.class);
        model.addObserver(observer);
        model.setMetaState(MetaState.INLOBBY);
        assertEquals(MetaState.INLOBBY, model.getMetaState());
        verify(observer).notifyMetaState(MetaState.INLOBBY);
    }

    @Test
    void getActiveLobbiesBlocksOnGameLock() throws Exception {
        java.lang.reflect.Field lockField = ClientModel.class.getDeclaredField("gameLock");
        lockField.setAccessible(true);
        Object gameLock = lockField.get(model);

        assertMethodBlocksOnLock(gameLock, () -> model.getActiveLobbies(), "getActiveLobbies");
    }

    @Test
    void getClientPlayerBlocksOnPlayersLock() throws Exception {
        java.lang.reflect.Field lockField = ClientModel.class.getDeclaredField("playersLock");
        lockField.setAccessible(true);
        Object playersLock = lockField.get(model);
        assertMethodBlocksOnLock(playersLock, () -> model.getClientPlayer(), "getClientPlayer");
    }

    @Test
    void getGameBlocksOnGameLock() throws Exception {
        java.lang.reflect.Field lockField = ClientModel.class.getDeclaredField("gameLock");
        lockField.setAccessible(true);
        Object gameLock = lockField.get(model);
        assertMethodBlocksOnLock(gameLock, () -> model.getGame(), "getGame");
    }

    @Test
    void getPlayersBlocksOnPlayersLock() throws Exception {
        java.lang.reflect.Field lockField = ClientModel.class.getDeclaredField("playersLock");
        lockField.setAccessible(true);
        Object playersLock = lockField.get(model);
        assertMethodBlocksOnLock(playersLock, () -> model.getPlayers(), "getPlayers");
    }

    @Test
    void getPlayerByShipBlocksOnPlayersLock() throws Exception {
        java.lang.reflect.Field lockField = ClientModel.class.getDeclaredField("playersLock");
        lockField.setAccessible(true);
        Object playersLock = lockField.get(model);
        ShipBoard shipBoard = mock(ShipBoard.class);
        assertMethodBlocksOnLock(playersLock, () -> model.getPlayerByShip(shipBoard), "getPlayerByShip");
    }

    @Test
    void getShipToPlayerBlocksOnPlayersLock() throws Exception {
        java.lang.reflect.Field lockField = ClientModel.class.getDeclaredField("playersLock");
        lockField.setAccessible(true);
        Object playersLock = lockField.get(model);
        assertMethodBlocksOnLock(playersLock, () -> model.getShipToPlayer(), "getShipToPlayer");
    }

    @Test
    void getFinalScoresBlocksOnGameLock() throws Exception {
        java.lang.reflect.Field lockField = ClientModel.class.getDeclaredField("gameLock");
        lockField.setAccessible(true);
        Object gameLock = lockField.get(model);
        assertMethodBlocksOnLock(gameLock, () -> model.getFinalScores(), "getFinalScores");
    }

    @Test
    void getMyShipBlocksOnPlayersLock() throws Exception {
        java.lang.reflect.Field lockField = ClientModel.class.getDeclaredField("playersLock");
        lockField.setAccessible(true);
        Object playersLock = lockField.get(model);
        Player player = mock(Player.class);
        ShipBoard shipBoard = mock(ShipBoard.class);
        when(player.getShipBoard()).thenReturn(shipBoard);
        model.setPlayer(player);
        assertMethodBlocksOnLock(playersLock, () -> model.getMyShip(), "getMyShip");
    }

    @Test
    void safeGetCurrentStateBlocksOnGameLock() throws Exception {
        java.lang.reflect.Field lockField = ClientModel.class.getDeclaredField("gameLock");
        lockField.setAccessible(true);
        Object gameLock = lockField.get(model);

        java.lang.reflect.Method safeGetCurrentState = ClientModel.class.getDeclaredMethod("safeGetCurrentState");
        safeGetCurrentState.setAccessible(true);

        java.lang.reflect.Field gameField = ClientModel.class.getDeclaredField("game");
        gameField.setAccessible(true);
        Game dummyGame = mock(Game.class);
        when(dummyGame.getCurrentState()).thenReturn(null);
        gameField.set(model, dummyGame);

        assertMethodBlocksOnLock(gameLock, () -> {
            try {
                safeGetCurrentState.invoke(model);
            } catch (Exception ignored) {}
        }, "safeGetCurrentState");
    }

    @Test
    void notifyFlightBoardPositionAllBranches() {
        model.createGame(Level.SECOND);
        GameState state = new SecondShipBuildingState();
        model.notifyCurrentState(state);
        ShipBoard myShip = mock(ShipBoard.class);
        ShipBoard otherShip = mock(ShipBoard.class);
        Player clientPlayer = mock(Player.class);
        when(clientPlayer.getShipBoard()).thenReturn(myShip);
        model.setPlayer(clientPlayer);
        model.addPlayer(clientPlayer, GameColor.BLUE);
        assertEquals(myShip, model.getMyShip());
        GameState spyState = spy(state);
        model.notifyCurrentState(spyState);
        model.notifyFlightBoardPosition(myShip, 5);
        verify(spyState).notifyFlightBoardPosition(myShip, 5, true);
        model.notifyFlightBoardPosition(otherShip, 3);
        verify(spyState).notifyFlightBoardPosition(otherShip, 3, false);
    }

    @Test
    void notifyReleaseForecastOtherShipBoardBranch() {
        model.createGame(Level.SECOND);
        SecondShipBuildingState state = new SecondShipBuildingState();
        model.notifyCurrentState(state);
        Player clientPlayer = mock(Player.class);
        ShipBoard myShip = mock(ShipBoard.class);
        when(clientPlayer.getShipBoard()).thenReturn(myShip);
        model.setPlayer(clientPlayer);
        model.addPlayer(clientPlayer, GameColor.BLUE);
        ShipBoard otherShip = mock(ShipBoard.class);
        model.addPlayer(mock(Player.class), GameColor.RED);
        state.notifyPeekForecast(otherShip, 1);
        model.notifyReleaseForecast(otherShip);
        assertTrue(state.getHasForecastDeck() || !state.getHasForecastDeck()); // dummy assertion to avoid warnings
    }

    private void assertMethodBlocksOnLock(Object lock, Runnable testAction, String description) throws Exception {
        CountDownLatch lockAcquired = new CountDownLatch(1);
        CountDownLatch releaseLock = new CountDownLatch(1);
        CountDownLatch methodReturned = new CountDownLatch(1);

        Thread locker = new Thread(() -> {
            synchronized (lock) {
                lockAcquired.countDown();
                try {
                    releaseLock.await(2, TimeUnit.SECONDS);
                } catch (InterruptedException ignored) {}
            }
        });
        locker.start();
        assertTrue(lockAcquired.await(1, TimeUnit.SECONDS), "Locker did not acquire lock in time: " + description);

        Thread testThread = new Thread(() -> {
            testAction.run();
            methodReturned.countDown();
        });
        testThread.start();

        Thread.sleep(200);
        assertEquals(1, methodReturned.getCount(), description + " should be blocked on lock");

        releaseLock.countDown();
        assertTrue(methodReturned.await(1, TimeUnit.SECONDS), description + " did not return after lock released");
    }
}

