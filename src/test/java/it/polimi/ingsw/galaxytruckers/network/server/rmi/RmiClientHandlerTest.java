package it.polimi.ingsw.galaxytruckers.network.server.rmi;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.network.client.rmi.RemoteClient;
import it.polimi.ingsw.galaxytruckers.network.server.SessionManager;
import it.polimi.ingsw.galaxytruckers.serverController.ServerControllerInterface;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.Event;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.PlayerDisconnectionEvent;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.LobbyInterface;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.rmi.RemoteException;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RmiClientHandlerTest {
    RmiClientHandler rmiClientHandler;
    MethodChecker checker;
    ServerControllerInterface controller;
    LobbyInterface lobby;
    SessionManager sessionManager;
    Player player;

    @BeforeEach
    void setUp() throws RemoteException{
        checker = mock(MethodChecker.class);
        controller = mock(ServerControllerInterface.class);
        lobby = mock(LobbyInterface.class);
        RemoteClient remoteClient = new RemoteClientStub(checker);
        sessionManager = mock(SessionManager.class);
        player = new Player("x");
        rmiClientHandler = new RmiClientHandler(remoteClient,player, controller);
        rmiClientHandler.setLobby(lobby);
        rmiClientHandler.setSessionManager(sessionManager);
    }

    @Test
    void connect() {
        rmiClientHandler.start();
        assertTrue(rmiClientHandler.isRunning());
    }

    @Test
    void stop() {
        rmiClientHandler.start();
        rmiClientHandler.stop();
        assertFalse(rmiClientHandler.isRunning());
    }

    @Test
    void ping() throws RemoteException {
        rmiClientHandler.ping();
        verify(sessionManager).ping(player);
    }

    @Test
    void notifyEvent() throws InterruptedException {
        rmiClientHandler.start();
        Event event = new PlayerDisconnectionEvent("x");
        CountDownLatch  countDownLatch = new CountDownLatch(1);
        rmiClientHandler.setAfterEach(countDownLatch::countDown);
        rmiClientHandler.notifyEvent(event);
        if (countDownLatch.await(1, TimeUnit.SECONDS)) {
            verify(checker).notifyEvent(event);
        } else {
            throw new RuntimeException("Timed out waiting for event");
        }
    }

    @Test
    void newGame() throws RemoteException {
        rmiClientHandler.newGame(Level.SECOND, 2);
        verify(controller).newGame(player, Level.SECOND, 2);
    }

    @Test
    void joinLobby() throws RemoteException {
        UUID uuid = UUID.randomUUID();
        rmiClientHandler.joinLobby(uuid);
        verify(controller).joinLobby(player, uuid);
    }

    @Test
    void leaveLobby() throws RemoteException {
        rmiClientHandler.leaveLobby();
        verify(controller).leaveLobby(player);
    }

    @Test
    void requestRandComponent() throws RemoteException {
        rmiClientHandler.requestRandComponent();
        verify(lobby).requestRandComponent(player);
    }

    @Test
    void requestComponent() throws RemoteException {
        rmiClientHandler.requestComponent(0);
        verify(lobby).requestComponent(player, 0);
    }

    @Test
    void rejectComponent() throws RemoteException {
        rmiClientHandler.rejectComponent();
        verify(lobby).rejectComponent(player);
    }

    @Test
    void stashComponent() throws RemoteException {
        rmiClientHandler.stashComponent();
        verify(lobby).stashComponent(player);
    }

    @Test
    void grabStashedComponent() throws RemoteException {
        rmiClientHandler.grabStashedComponent(0);
        verify(lobby).grabStashedComponent(player, 0);
    }

    @Test
    void placeComponent() throws RemoteException {
        rmiClientHandler.placeComponent(new Point(0,0), Direction.UP);
        verify(lobby).placeComponent(player,new Point(0,0),Direction.UP);
    }

    @Test
    void flipHourglass() throws RemoteException {
        rmiClientHandler.flipHourglass();
        verify(lobby).flipHourglass(player);
    }

    @Test
    void placeShipOnFlightBoard() throws RemoteException {
        rmiClientHandler.placeShipOnFlightBoard(0);
        verify(lobby).placeShipOnFlightBoard(player, 0);
    }

    @Test
    void acquireForecast() throws RemoteException {
        rmiClientHandler.acquireForecast(0);
        verify(lobby).acquireForecast(player, 0);
    }

    @Test
    void releaseForecast() throws RemoteException {
        rmiClientHandler.releaseForecast();
        verify(lobby).releaseForecast(player);
    }

    @Test
    void removeComponent() throws RemoteException {
        rmiClientHandler.removeComponent(new Point(0,0));
        verify(lobby).removeComponent(player,new Point(0,0));
    }

    @Test
    void chooseShipPiece() throws RemoteException {
        rmiClientHandler.chooseShipPiece(0);
        verify(lobby).chooseShipPiece(player, 0);
    }

    @Test
    void initializeCabin() throws RemoteException {
        rmiClientHandler.initializeCabin(new Point(0,0), CrewType.HUMAN);
        verify(lobby).initializeCabin(player,new Point(0,0), CrewType.HUMAN);
    }

    @Test
    void drawCard() throws RemoteException {
        rmiClientHandler.drawCard();
        verify(lobby).drawCard(player);
    }

    @Test
    void activateComponent() throws RemoteException {
        rmiClientHandler.activateComponent(new Point(0,0));
        verify(lobby).activateComponent(player,new Point(0,0));
    }

    @Test
    void loseCrew() throws RemoteException {
        rmiClientHandler.loseCrew(new Point(0,0));
        verify(lobby).loseCrew(player,new Point(0,0));
    }

    @Test
    void grabReward() throws RemoteException {
        rmiClientHandler.grabReward(false);
        verify(lobby).grabReward(player,false);
    }

    @Test
    void placeGoods() throws RemoteException {
        rmiClientHandler.placeGoods(new Point(0,0), GoodsType.RED);
        verify(lobby).placeGoods(player,new Point(0,0), GoodsType.RED);
    }

    @Test
    void removeGoods() throws RemoteException {
        rmiClientHandler.removeGoods(new Point(0,0),GoodsType.RED);
        verify(lobby).removeGoods(player,new Point(0,0), GoodsType.RED);
    }

    @Test
    void loseGoods() throws RemoteException {
        rmiClientHandler.loseGoods(new Point(0,0));
        verify(lobby).loseGoods(player,new Point(0,0));
    }

    @Test
    void useBattery() throws RemoteException {
        rmiClientHandler.useBattery(new Point(0,0));
        verify(lobby).useBattery(player,new Point(0,0));
    }

    @Test
    void choosePlanet() throws RemoteException {
        rmiClientHandler.choosePlanet(0);
        verify(lobby).choosePlanet(player, 0);
    }

    @Test
    void goNext() throws RemoteException {
        rmiClientHandler.goNext();
        verify(lobby).goNext(player);
    }

    @Test
    void giveUp() throws RemoteException {
        rmiClientHandler.giveUp();
        verify(lobby).giveUp(player);
    }

    @Test
    void noLobbyException() {
        rmiClientHandler.setLobby(null);
        assertThrows(IllegalStateException.class, () -> rmiClientHandler.goNext());
    }

    @Nested
    class FailedConnection {
        @BeforeEach
        void setUp() throws RemoteException {
            rmiClientHandler = new RmiClientHandler(new DisconnectedClientStub(),player,controller);
        }

        @Test
        void handleNetworkError() throws InterruptedException {
            rmiClientHandler.notifyEvent(new PlayerDisconnectionEvent("x"));
            CountDownLatch latch = new CountDownLatch(1);
            rmiClientHandler.setAfterEach(latch::countDown);
            rmiClientHandler.start();
            if (latch.await(1, TimeUnit.SECONDS)) {
                assertFalse(rmiClientHandler.isRunning());
            } else {
                throw new RuntimeException("Timeout waiting for latch");
            }
        }
    }
}

class RemoteClientStub implements RemoteClient {
    MethodChecker checker;

    public RemoteClientStub(MethodChecker checker) {
        this.checker = checker;
    }

    @Override
    public void notifyEvent(Event event) throws RemoteException {
        checker.notifyEvent(event);
    }
}

class DisconnectedClientStub implements RemoteClient {
    @Override
    public void notifyEvent(Event event) throws RemoteException {
        throw new RemoteException();
    }
}