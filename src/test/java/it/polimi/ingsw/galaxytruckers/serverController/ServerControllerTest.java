package it.polimi.ingsw.galaxytruckers.serverController;

import it.polimi.ingsw.galaxytruckers.model.GameModel;
import it.polimi.ingsw.galaxytruckers.model.GameModelInterface;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.network.server.ClientHandler;
import it.polimi.ingsw.galaxytruckers.network.server.SessionManager;
import it.polimi.ingsw.galaxytruckers.serverController.dto.DtoConverter;
import it.polimi.ingsw.galaxytruckers.serverController.events.EventQueue;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.AddActiveLobbyEvent;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.ControllerEvent;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.SetActiveLobbiesEvent;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Lobby;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServerControllerTest {
    ServerController controller;
    GameModelInterface model;
    EventQueue<ControllerEvent> eventQueue;
    ClientHandler clientHandler;

    @BeforeEach
    void setup(){
        model = new GameModel();
        eventQueue = mock(EventQueue.class);
        clientHandler = mock(ClientHandler.class);
        controller = new ServerController(model,eventQueue);
    }

    @AfterEach
    void cleanup(){
        controller.getIdToLobby().clear();
        Player.clear();
    }

    @Test
    void newGameInitializesCorrectly(){
        controller.newGame(Player.addPlayer("testPlayer"), Level.SECOND, 2);
        assertNotNull(controller.getIdToLobby());
        assertEquals(1, controller.getIdToLobby().size());
    }

    @Test
    void joinLobbyWithInCorrectKey(){
        Player p1 = Player.addPlayer("player1");
        controller.newGame(p1, Level.SECOND, 2);
        assertThrows(IllegalArgumentException.class, () -> controller.joinLobby(Player.addPlayer("player2"),UUID.randomUUID()));
    }

    @Test
    void joinLobbyWithCorrectKey(){
        Player p1 = Player.addPlayer("player1");
        controller.newGame(p1, Level.SECOND, 2);

        // grabbing the key
        Iterator<UUID> iterator = controller.getIdToLobby().keySet().iterator();
        assertTrue(iterator.hasNext());
        UUID firstKey = iterator.next();
        System.out.println("First key: " + firstKey);

        Player p2 = Player.addPlayer("player2");
        controller.joinLobby(p2, firstKey);
        assertEquals(List.of(p1, p2), controller.getIdToLobby().get(firstKey).getPlayers());
    }

    @Test
    void handlePlayerDisconnectionWhenPlayerPresent(){
        // player 2 joins with the key
        Player p2 = Player.addPlayer("player2");
        SessionManager.getInstance().registerClient(p2, mock(ClientHandler.class));

        // player 2 disconnects
        controller.handlePlayerDisconnection(p2);
        assertNull(Player.getPlayer("player2"));
    }

    @Test
    void handlePlayerDisconnectionWhenHandlerNotPresent(){
        // player 2 doesn't join, but tries to disconnect
        Player p2 = Player.addPlayer("player2");
        Player.removePlayer("player2");
        controller.handlePlayerDisconnection(p2);
        assertFalse(SessionManager.getInstance().isPlayerActive(Player.getPlayer("player2")));
    }

    @Test
    void leaveLobbyIfPresent(){
        // player 1 creates game
        Player p1 = Player.addPlayer("player1");
        controller.newGame(p1, Level.SECOND, 2);

        controller.leaveLobby(p1);
        assertTrue(controller.getIdToLobby().isEmpty());
    }

    @Test
    void leaveLobbyIfNotPresentChangesNothing(){
        // player 1 creates game
        Player p1 = Player.addPlayer("player1");
        controller.newGame(p1, Level.SECOND, 2);
        Map<UUID, Lobby> lobby1 = controller.getIdToLobby();

        Player p2 = Player.addPlayer("player2");
        controller.leaveLobby(p2);
        Map<UUID, Lobby> lobby2 = controller.getIdToLobby();

        assertFalse(controller.getIdToLobby().isEmpty());
        assertEquals(lobby1, lobby2);
    }

    //TODO: fix old tests

    @Test
    void registerNicknameWithNewPlayerAddsPlayer() {
        controller.registerNickname("p1",clientHandler);
        verify(clientHandler).setPlayer(Player.getPlayer("p1"));
        verify(eventQueue).notifyEvent(new SetActiveLobbiesEvent("p1", List.of(),  false));
    }

    @Test
    void registerNicknameWithDisconnectedPlayerReconnects() {
        Player p1 = Player.addPlayer("p1");
        controller.newGame(p1, Level.SECOND, 2);
        controller.handlePlayerDisconnection(p1);
        clearInvocations(eventQueue,clientHandler);
        controller.registerNickname("p1",clientHandler);
        verify(clientHandler).setPlayer(Player.getPlayer("p1"));
        verify(clientHandler).pauseEvents();
        SetActiveLobbiesEvent setActiveLobbiesEvent = new SetActiveLobbiesEvent(
                "p1",
                controller.getIdToLobby().values().stream()
                        .map(DtoConverter::getActiveLobby)
                        .toList(),
                true);
        verify(eventQueue).notifyEvent(setActiveLobbiesEvent);
    }

    @Test
    void registerNicknameWithInvalidNicknameThrowsException() {
        controller.registerNickname("p1", clientHandler);
        assertThrows(IllegalArgumentException.class, () -> controller.registerNickname("p1", clientHandler));
    }

    @Test
    void newGame() {
        Player p1 = Player.addPlayer("p1");
        Lobby lobby = (Lobby) controller.newGame(p1, Level.SECOND, 2);
        assertEquals(lobby, controller.getIdToLobby().get(lobby.getId()));
        assertEquals(p1, lobby.getHost());
        verify(eventQueue).notifyEvent(new AddActiveLobbyEvent(DtoConverter.getActiveLobby(lobby)));
    }

    @Test
    void newGameThrowsExceptionIfAlreadyInALobby() {
        Player p1 = Player.addPlayer("p1");
        controller.newGame(p1, Level.SECOND, 2);
        assertThrows(IllegalStateException.class, () -> controller.newGame(p1, Level.SECOND, 2));
    }

    @Test
    void joinLobby() {
        Player p1 = Player.addPlayer("p1");
        Lobby lobby = (Lobby) controller.newGame(p1, Level.SECOND, 3);
        assertNotNull(lobby);
        Player p2 = Player.addPlayer("p2");
        controller.joinLobby(p2, lobby.getId());
        assertTrue(lobby.getPlayers().contains(p2));
    }

    @Test
    void joinLobbyThrowsWhenThePlayerIsInAnotherGame() {
        Player p1 = Player.addPlayer("p1");
        Lobby lobby = (Lobby) controller.newGame(p1, Level.SECOND, 3);
        assertNotNull(lobby);
        assertThrows(IllegalStateException.class, () -> controller.joinLobby(p1, lobby.getId()));
    }

    @Test
    void joinLobbyThrowsIfNoLobbiesWithGivenId() {
        Player p1 = Player.addPlayer("p1");
        assertThrows(IllegalArgumentException.class, () -> controller.joinLobby(p1, UUID.randomUUID()));
    }

    @Test
    void testProperConstructor() {
        assertDoesNotThrow(() -> controller = new ServerController(model));
    }
}