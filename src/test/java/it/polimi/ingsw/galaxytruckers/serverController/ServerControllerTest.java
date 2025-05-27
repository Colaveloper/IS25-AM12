package it.polimi.ingsw.galaxytruckers.serverController;

import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.GameModel;
import it.polimi.ingsw.galaxytruckers.model.GameModelInterface;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Lobby;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

import static org.junit.jupiter.api.Assertions.*;

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
    }

    @Test
    void registerNickname(){
        assertEquals("test", testController.registerNickname("test").getNickname());
    }

    @Test
    void newGameInitializesCorrectly(){
        Player p = new Player("testPlayer");
        testController.newGame(p, Level.SECOND, 2);
        assertNotNull(testController.getIdToLobby());
        assertEquals(1, testController.getIdToLobby().size());
    }

    @Test
    void joinLobbyWithInCorrectKey(){
        Player p = new Player("player1");
        testController.newGame(p, Level.SECOND, 2);
        assertThrows(IllegalArgumentException.class, () -> testController.joinLobby(new Player("player2"),new Lobby(model, p, Level.SECOND, 2).getId()));
    }

    @Test
    void joinLobbyWithCorrectKey(){
        Player p1 = new Player("player1");
        testController.newGame(p1, Level.SECOND, 2);

        // grabbing the key
        Iterator<UUID> iterator = testController.getIdToLobby().keySet().iterator();
        assertTrue(iterator.hasNext());
        UUID firstKey = iterator.next();
        System.out.println("First key: " + firstKey);

        Player p2 = new Player("player2");
        testController.joinLobby(p2, firstKey);
        assertEquals(List.of(p1, p2), testController.getIdToLobby().get(firstKey).getPlayers());
    }

    @Test
    void handlePlayerDisconnectionWhenPlayerPresent(){
        // player 1 creates game
        Player p1 = new Player("player1");
        testController.newGame(p1, Level.SECOND, 2);

        // grabbing the key
        Iterator<UUID> iterator = testController.getIdToLobby().keySet().iterator();
        assertTrue(iterator.hasNext());
        UUID firstKey = iterator.next();
        System.out.println("First key: " + firstKey);

        // player 2 joins with the key
        Player p2 = new Player("player2");
        testController.joinLobby(p2, firstKey);

        // player 2 disconnects
        testController.handlePlayerDisconnection(p2);
        assertTrue(testController.getIdToLobby().isEmpty());
    }

    @Test
    void handlePlayerDisconnectionWhenPlayerNotPresent(){
        // player 1 creates game
        Player p1 = new Player("player1");
        testController.newGame(p1, Level.SECOND, 2);

        // player 2 doesn't join, but tries to disconnect
        Player p2 = new Player("player2");
        testController.handlePlayerDisconnection(p2);
        assertThrows(IllegalArgumentException.class, () -> Player.getPlayer("player2"));
    }

    @Test
    void leaveLobbyIfPresent(){
        // player 1 creates game
        Player p1 = new Player("player1");
        testController.newGame(p1, Level.SECOND, 2);

        testController.leaveLobby(p1);
        assertTrue(testController.getIdToLobby().isEmpty());
    }

    @Test
    void leaveLobbyIfNotPresentChangesNothing(){
        // player 1 creates game
        Player p1 = new Player("player1");
        testController.newGame(p1, Level.SECOND, 2);
        ConcurrentMap<UUID, Lobby> lobby1 = testController.getIdToLobby();

        Player p2 = new Player("player2");
        testController.leaveLobby(p2);
        ConcurrentMap<UUID, Lobby> lobby2 = testController.getIdToLobby();

        assertFalse(testController.getIdToLobby().isEmpty());
        assertEquals(lobby1, lobby2);
    }
}