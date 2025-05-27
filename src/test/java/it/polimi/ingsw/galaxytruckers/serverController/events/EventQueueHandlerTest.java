package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.network.server.SessionManager;
import it.polimi.ingsw.galaxytruckers.network.server.VirtualClient;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class EventQueueHandlerTest {
    EventQueueHandler eventQueueHandler;
    EventQueue eventQueue;
    VirtualClientStub client1;
    VirtualClientStub client2;
    Player p1;
    Player p2;

    Object queueLock;

    @BeforeEach
    void setup() {
        client1 = new VirtualClientStub();
        client2 = new VirtualClientStub();
        p1 = Player.addPlayer("p1");
        p2 = Player.addPlayer("p2");
        SessionManager.getInstance().registerClient(p1,client1);
        SessionManager.getInstance().registerClient(p2,client2);
        eventQueue = new EventQueue();
        eventQueueHandler = new EventQueueHandler(List.of(p1,p2), eventQueue);
        eventQueueHandler.start();
    }

    @Test
    void broadCastUpdateTest() throws InterruptedException {
        long time = 0;
        CountDownLatch countDownLatch = new CountDownLatch(1);
        eventQueueHandler.setAfterEach(countDownLatch::countDown);
        eventQueue.notifyEvent(new JoinLobbyEvent("test", GameColor.BLUE));
        if (countDownLatch.await(1, TimeUnit.SECONDS)) {
            eventQueueHandler.stop();
            assertInstanceOf(JoinLobbyEvent.class, client1.receivedEvents.getFirst());
            assertInstanceOf(JoinLobbyEvent.class, client2.receivedEvents.getFirst());
        } else {
            throw new RuntimeException("Timeout waiting for latch");
        }
    }

    @Test
    void directUpdateTest() throws InterruptedException {
        long time = 0;
        CountDownLatch countDownLatch = new CountDownLatch(1);
        eventQueueHandler.setAfterEach(countDownLatch::countDown);
        eventQueue.notifyEvent(new ForecastDetailsEvent("p1", List.of()));
        if (countDownLatch.await(1, TimeUnit.SECONDS)) {
            eventQueueHandler.stop();
            assertInstanceOf(ForecastDetailsEvent.class, client1.receivedEvents.getFirst());
            assertTrue(client2.receivedEvents.isEmpty());
        } else {
            throw new RuntimeException("Timeout waiting for latch");
        }
    }

    @AfterEach
    void tearDown() {
        SessionManager.getInstance().unregisterClient(p1);
        SessionManager.getInstance().unregisterClient(p2);
        Player.removePlayer("p1");
        Player.removePlayer("p2");
    }
}

class VirtualClientStub implements VirtualClient {
    List<Event> receivedEvents = new ArrayList<>();

    @Override
    public void notifyEvent(Event event) {
        receivedEvents.add(event);
    }
}