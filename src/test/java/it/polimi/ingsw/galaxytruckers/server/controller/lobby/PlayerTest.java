package it.polimi.ingsw.galaxytruckers.server.controller.lobby;

import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.ShipBoard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    Player p1;
    ShipBoard shipBoard = Mockito.mock(ShipBoard.class);
    Lobby lobby = Mockito.mock(Lobby.class);

    @BeforeEach
    void setUp() {
        p1 = Player.addPlayer("p1");
    }

    @AfterEach
    void tearDown() {
        Player.clear();
    }

    @Test
    void addPlayer() {
        assertEquals(p1, Player.getPlayer("p1"));
    }

    @Test
    void addPlayerThrowsIfNicknameNotUnique() {
        assertThrows(IllegalArgumentException.class, () -> Player.addPlayer("p1"));
    }

    @Test
    void removePlayer() {
        Player.removePlayer("p1");
        assertNull(Player.getPlayer("p1"));
    }

    @Test
    void getAllPlayers() {
        Player p2 = Player.addPlayer("p2");
        assertEquals(Set.of(p1,p2), Player.getAllPlayers());
    }

    @Test
    void clear() {
        Player.addPlayer("p2");
        Player.clear();
        assertEquals(Set.of(), Player.getAllPlayers());
    }

    @Test
    void getNickname() {
        assertEquals("p1", p1.getNickname());
    }

    @Test
    void setLobby() {

        p1.setLobby(lobby);
        assertEquals(lobby, p1.getLobby().orElseThrow());
    }

    @Test
    void setShipBoard() {
        p1.setShipBoard(shipBoard);
        assertEquals(shipBoard, p1.getShipBoard().orElseThrow());
        assertEquals(p1,Player.getPlayer(shipBoard));
    }

    @Test
    void leaveLobby() {
        p1.setLobby(lobby);
        p1.setShipBoard(shipBoard);
        p1.leaveLobby();
        assertTrue(p1.getLobby().isEmpty());
        assertTrue(p1.getShipBoard().isEmpty());
        assertThrows(IllegalArgumentException.class, () -> Player.getPlayer(shipBoard));
    }
}