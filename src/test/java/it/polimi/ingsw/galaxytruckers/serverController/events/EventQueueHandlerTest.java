package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.network.server.SessionManager;
import it.polimi.ingsw.galaxytruckers.network.server.VirtualClient;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EventQueueHandlerTest {
    EventQueueHandler eventQueueHandler;
    EventQueue eventQueue;
    VirtualClientStub client1;
    VirtualClientStub client2;

    Object queueLock;

    @BeforeEach
    void setup() {
        client1 = new VirtualClientStub();
        client2 = new VirtualClientStub();
        Player p1 = Player.addPlayer("p1");
        Player p2 = Player.addPlayer("p2");
        SessionManager.getInstance().registerClient(p1,client1);
        SessionManager.getInstance().registerClient(p2,client2);
        eventQueue = new EventQueue();
        eventQueueHandler = new EventQueueHandler(List.of(p1,p2), eventQueue);
        eventQueueHandler.start();
    }

    @Test
    void broadCastUpdateTest() throws InterruptedException {
        long time = 0;
        eventQueue.notifyEvent(new JoinLobbyEvent("test"));
        while(!eventQueue.isEmpty() && time < 1000) {
            Thread.sleep(100);
            time += 100;
        }
        eventQueueHandler.stop();
        assertInstanceOf(JoinLobbyEvent.class, client1.receivedEvents.getFirst());
        assertInstanceOf(JoinLobbyEvent.class, client2.receivedEvents.getFirst());
    }

    @Test
    void directUpdateTest() throws InterruptedException {
        long time = 0;
        eventQueue.notifyEvent(new ForecastDetailsEvent("p1", List.of()));
        while(!eventQueue.isEmpty() && time < 1000) {
            Thread.sleep(100);
            time += 100;
        }
        eventQueueHandler.stop();
        assertInstanceOf(ForecastDetailsEvent.class, client1.receivedEvents.getFirst());
        assertTrue(client2.receivedEvents.isEmpty());
    }

    @AfterEach
    void tearDown() {
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