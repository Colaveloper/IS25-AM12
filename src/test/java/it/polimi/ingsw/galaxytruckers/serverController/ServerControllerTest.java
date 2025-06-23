package it.polimi.ingsw.galaxytruckers.serverController;

import it.polimi.ingsw.galaxytruckers.model.GameModel;
import it.polimi.ingsw.galaxytruckers.model.GameModelInterface;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.network.server.ClientHandler;
import it.polimi.ingsw.galaxytruckers.network.server.SessionManager;
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
    ServerController testController;
    GameModelInterface model;

    @BeforeEach
    void setup(){
        model = new GameModel();
        testController = new ServerController(model);
    }

    @AfterEach
    void cleanup(){
        testController.getIdToLobby().clear();
        Player.clear();
    }

    @Test
    void newGameInitializesCorrectly(){
        testController.newGame(Player.addPlayer("testPlayer"), Level.SECOND, 2);
        assertNotNull(testController.getIdToLobby());
        assertEquals(1, testController.getIdToLobby().size());
    }

    @Test
    void joinLobbyWithInCorrectKey(){
        Player p1 = Player.addPlayer("player1");
        testController.newGame(p1, Level.SECOND, 2);
        assertThrows(IllegalArgumentException.class, () -> testController.joinLobby(Player.addPlayer("player2"),UUID.randomUUID()));
    }

    @Test
    void joinLobbyWithCorrectKey(){
        Player p1 = Player.addPlayer("player1");
        testController.newGame(p1, Level.SECOND, 2);

        // grabbing the key
        Iterator<UUID> iterator = testController.getIdToLobby().keySet().iterator();
        assertTrue(iterator.hasNext());
        UUID firstKey = iterator.next();
        System.out.println("First key: " + firstKey);

        Player p2 = Player.addPlayer("player2");
        testController.joinLobby(p2, firstKey);
        assertEquals(List.of(p1, p2), testController.getIdToLobby().get(firstKey).getPlayers());
    }

    @Test
    void handlePlayerDisconnectionWhenPlayerPresent(){
        // player 2 joins with the key
        Player p2 = Player.addPlayer("player2");
        SessionManager.getInstance().registerClient(p2, mock(ClientHandler.class));

        // player 2 disconnects
        testController.handlePlayerDisconnection(p2);
        assertNull(Player.getPlayer("player2"));
    }

    @Test
    void handlePlayerDisconnectionWhenHandlerNotPresent(){
        // player 2 doesn't join, but tries to disconnect
        Player p2 = Player.addPlayer("player2");
        Player.removePlayer("player2");
        testController.handlePlayerDisconnection(p2);
        assertFalse(SessionManager.getInstance().isPlayerActive(Player.getPlayer("player2")));
    }

    @Test
    void leaveLobbyIfPresent(){
        // player 1 creates game
        Player p1 = Player.addPlayer("player1");
        testController.newGame(p1, Level.SECOND, 2);

        testController.leaveLobby(p1);
        assertTrue(testController.getIdToLobby().isEmpty());
    }

    @Test
    void leaveLobbyIfNotPresentChangesNothing(){
        // player 1 creates game
        Player p1 = Player.addPlayer("player1");
        testController.newGame(p1, Level.SECOND, 2);
        Map<UUID, Lobby> lobby1 = testController.getIdToLobby();

        Player p2 = Player.addPlayer("player2");
        testController.leaveLobby(p2);
        Map<UUID, Lobby> lobby2 = testController.getIdToLobby();

        assertFalse(testController.getIdToLobby().isEmpty());
        assertEquals(lobby1, lobby2);
    }
}