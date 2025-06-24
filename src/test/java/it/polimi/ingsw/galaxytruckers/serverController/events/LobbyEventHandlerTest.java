package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.model.GameModel;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.network.server.ClientHandler;
import it.polimi.ingsw.galaxytruckers.network.server.SessionManager;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.*;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Lobby;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class LobbyEventHandlerTest {
    LobbyEventHandler lobbyEventHandler;
    EventQueue<LobbyEvent> eventQueue;
    VirtualClientStub client1;
    VirtualClientStub client2;
    Player p1;
    Player p2;

    @BeforeEach
    void setup() {
        client1 = new VirtualClientStub();
        client2 = new VirtualClientStub();
        p1 = Player.addPlayer("p1");
        p2 = Player.addPlayer("p2");
        SessionManager.getInstance().registerClient(p1, client1);
        SessionManager.getInstance().registerClient(p2, client2);
        eventQueue = new EventQueue<>();
        lobbyEventHandler = new LobbyEventHandler(eventQueue, new LobbyStub(List.of(p1, p2)));
        lobbyEventHandler.start();
    }

    @AfterEach
    void tearDown() {
        SessionManager.getInstance().clear();
        Player.clear();
    }

    @Test
    void broadCastUpdateTest() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        lobbyEventHandler.setAfterEach(countDownLatch::countDown);
        eventQueue.clear();
        eventQueue.notifyEvent(new JoinLobbyEvent("test", GameColor.BLUE));
        if (countDownLatch.await(1, TimeUnit.SECONDS)) {
            lobbyEventHandler.stop();
            assertTrue(client1.receivedEvents.stream().anyMatch(e -> e instanceof JoinLobbyEvent));
            assertTrue(client2.receivedEvents.stream().anyMatch(e -> e instanceof JoinLobbyEvent));
        } else {
            throw new RuntimeException("Timeout waiting for latch");
        }
    }

    @Test
    void directUpdateTest() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        lobbyEventHandler.setAfterEach(countDownLatch::countDown);
        eventQueue.clear();
        eventQueue.notifyEvent(new ForecastDetailsEvent("p1", List.of()));
        if (countDownLatch.await(1, TimeUnit.SECONDS)) {
            lobbyEventHandler.stop();
            assertTrue(client1.receivedEvents.stream().anyMatch(e -> e instanceof ForecastDetailsEvent));
            assertFalse(client2.receivedEvents.stream().anyMatch(e -> e instanceof ForecastDetailsEvent));
        } else {
            throw new RuntimeException("Timeout waiting for latch");
        }
    }
}

class LobbyStub extends Lobby {
    private final List<Player> players;

    public LobbyStub(List<Player> players) {
        super(new GameModel(), players.getFirst(), Level.SECOND, 2, (_) -> {
        });
        this.players = players;
    }

    @Override
    public List<Player> getPlayers() {
        return players;
    }
}

class VirtualClientStub implements ClientHandler {
    List<Event> receivedEvents = new ArrayList<>();

    @Override
    public void setPlayer(Player player) {
    }

    @Override
    public void notifyEvent(Event event) {
        //mock
        if (Objects.requireNonNull(event) instanceof LobbyEvent) {
            receivedEvents.add(event);
        }
    }

    @Override
    public void stop() {
    }

    @Override
    public void pauseEvents() {
    }
}