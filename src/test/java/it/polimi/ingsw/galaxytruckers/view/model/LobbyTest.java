package it.polimi.ingsw.galaxytruckers.view.model;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class LobbyTest {

    @Test
    void getPlayersN() {
        Lobby lobby = new Lobby(UUID.randomUUID(), 4, Level.SECOND, "hostUser");
        assertEquals(4, lobby.getPlayersN());
    }

    @Test
    void getLevel() {
        Lobby lobby = new Lobby(UUID.randomUUID(), 3, Level.SECOND, "hostUser");
        assertEquals(Level.SECOND, lobby.getLevel());
    }

    @Test
    void getId() {
        UUID id = UUID.randomUUID();
        Lobby lobby = new Lobby(id, 2, Level.SECOND, "hostUser");
        assertEquals(id, lobby.getId());
    }

    @Test
    void getPlayers_emptyList() {
        Lobby lobby = new Lobby(UUID.randomUUID(), 5, Level.SECOND, "hostUser");
        assertNotNull(lobby.getPlayers());
        assertTrue(lobby.getPlayers().isEmpty());
    }

    @Test
    void getPlayers_existingList() {
        List<String> players = new ArrayList<>(Arrays.asList("Alice", "Bob"));
        Lobby lobby = new Lobby(UUID.randomUUID(), 5, Level.SECOND, players, "hostUser");
        assertEquals(players, lobby.getPlayers());
    }

    @Test
    void getHost() {
        Lobby lobby = new Lobby(UUID.randomUUID(), 4, Level.SECOND, "theHost");
        assertEquals("theHost", lobby.getHost());
    }

    @Test
    void lobbyConstructorWithExistingPlayersList() {
        UUID id = UUID.randomUUID();
        List<String> players = Arrays.asList("A", "B", "C");
        Lobby lobby = new Lobby(id, 4, Level.SECOND, players, "hostUser");
        assertEquals(id, lobby.getId());
        assertEquals(4, lobby.getPlayersN());
        assertEquals(Level.SECOND, lobby.getLevel());
        assertEquals(players, lobby.getPlayers());
        assertEquals("hostUser", lobby.getHost());
    }

    @Test
    void lobbyConstructorWithEmptyPlayersList() {
        UUID id = UUID.randomUUID();
        Lobby lobby = new Lobby(id, 3, Level.SECOND, "hostUser");
        assertEquals(id, lobby.getId());
        assertEquals(3, lobby.getPlayersN());
        assertEquals(Level.SECOND, lobby.getLevel());
        assertNotNull(lobby.getPlayers());
        assertTrue(lobby.getPlayers().isEmpty());
        assertEquals("hostUser", lobby.getHost());
    }
}