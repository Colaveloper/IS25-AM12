package it.polimi.ingsw.galaxytruckers.view.controller;

import it.polimi.ingsw.galaxytruckers.view.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlayerRegistryTest {
    private PlayerRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new PlayerRegistry();
    }

    @Test
    void shouldAddNewPlayer() {
        Player player = registry.addPlayer("testPlayer");

        assertNotNull(player);
        assertEquals("testPlayer", player.getNickname());
    }

    @Test
    void shouldReturnExistingPlayer() {
        Player firstPlayer = registry.addPlayer("testPlayer");
        Player secondPlayer = registry.addPlayer("testPlayer");

        assertSame(firstPlayer, secondPlayer);
    }

    @Test
    void shouldGetPlayerByNickname() {
        Player addedPlayer = registry.addPlayer("testPlayer");
        Player retrievedPlayer = registry.getByNickname("testPlayer");

        assertNotNull(retrievedPlayer);
        assertSame(addedPlayer, retrievedPlayer);
    }

    @Test
    void shouldReturnNullForNonExistentPlayer() {
        Player player = registry.getByNickname("nonExistentPlayer");

        assertNull(player);
    }

    @Test
    void shouldRemovePlayer() {
        Player player = registry.addPlayer("testPlayer");
        registry.removePlayer(player);

        Player retrievedPlayer = registry.getByNickname("testPlayer");
        assertNull(retrievedPlayer);
    }

    @Test
    void shouldClearAllPlayers() {
        registry.addPlayer("player1");
        registry.addPlayer("player2");
        registry.clear();

        assertNull(registry.getByNickname("player1"));
        assertNull(registry.getByNickname("player2"));
    }
}
