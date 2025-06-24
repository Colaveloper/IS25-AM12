package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.network.server.ClientHandler;
import it.polimi.ingsw.galaxytruckers.network.server.SessionManager;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.AddActiveLobbyEvent;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.ControllerEvent;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.SetActiveLobbiesEvent;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ControllerEventHandlerTest {
    ControllerEventHandler eventHandler;
    Player p1;
    Player p2;

    @BeforeEach
    void setup() {
        p1 = Player.addPlayer("p1");
        p2 = Player.addPlayer("p2");
        SessionManager.getInstance().registerClient(p1, Mockito.mock(ClientHandler.class));
        SessionManager.getInstance().registerClient(p2, Mockito.mock(ClientHandler.class));
        SessionManager.getInstance().shutDown();
        EventQueue<ControllerEvent> eventQueue = new EventQueue<>();
        eventHandler = new ControllerEventHandler(eventQueue);
    }

    @AfterEach
    void tearDown() {
        Player.clear();
        SessionManager.getInstance().clear();
    }

    @Test
    void handleBroadcastEvent() {
        ControllerEvent event = new AddActiveLobbyEvent(
                null
        );
        eventHandler.handleEvent(event);
        Mockito.verify(SessionManager.getInstance().getClient(p1)).notifyEvent(new AddActiveLobbyEvent(null));
        Mockito.verify(SessionManager.getInstance().getClient(p2)).notifyEvent(new AddActiveLobbyEvent(null));
    }

    @Test
    void handleDirectEvent() {
        ControllerEvent event = new SetActiveLobbiesEvent(
                p1.getNickname(),
                List.of(),
                false
        );
        eventHandler.handleEvent(event);
        Mockito.verify(SessionManager.getInstance().getClient(p1)).notifyEvent(event);
        Mockito.verify(SessionManager.getInstance().getClient(p2), Mockito.never()).notifyEvent(event);
    }

    @Test
    void sendToDisconnectedPlayer() {
        SessionManager.getInstance().unregisterClient(p1);
        assertDoesNotThrow(() -> eventHandler.handleEvent(new SetActiveLobbiesEvent(p1.getNickname(), List.of(), false)));
        assertDoesNotThrow(() -> eventHandler.handleEvent(new AddActiveLobbyEvent(null)));
    }
}