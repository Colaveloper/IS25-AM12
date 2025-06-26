package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.serverController.lobby.Lobby;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class EventTest {

    @Test
    void shouldResume() {
        GameSnapshotEvent gameSnapshotEvent = new GameSnapshotEvent(
                null,
                null,
                null
        );
        assertTrue(gameSnapshotEvent.shouldResume());
        SetActiveLobbiesEvent setActiveLobbiesEvent = new SetActiveLobbiesEvent(
                null,
                null,
                false
        );
        assertTrue(setActiveLobbiesEvent.shouldResume());
    }

    @Test
    void shouldNotResume() {
        GameEndEvent gameEndEvent = new GameEndEvent(null);
        assertFalse(gameEndEvent.shouldResume());
        PlayerExitEvent playerExitEvent = new PlayerExitEvent(null);
        assertFalse(playerExitEvent.shouldResume());
        ForecastDetailsEvent forecastDetailsEvent = new ForecastDetailsEvent(null, null);
        assertFalse(forecastDetailsEvent.shouldResume());
        LobbyDetailsEvent lobbyDetailsEvent = new LobbyDetailsEvent(null, null);
        assertFalse(lobbyDetailsEvent.shouldResume());
    }

    @Test
    void getReceiverName() {
        String name = "name";
        GameSnapshotEvent gameSnapshotEvent = new GameSnapshotEvent(
                name,
                null,
                null
        );
        assertEquals(name, gameSnapshotEvent.getReceiverName().orElseThrow());
        SetActiveLobbiesEvent setActiveLobbiesEvent = new SetActiveLobbiesEvent(
                name,
                null,
                false
        );
        assertEquals(name, setActiveLobbiesEvent.getReceiverName().orElseThrow());
        ForecastDetailsEvent forecastDetailsEvent = new ForecastDetailsEvent(
                name,
                null
        );
        assertEquals(name, forecastDetailsEvent.getReceiverName().orElseThrow());
        LobbyDetailsEvent lobbyDetailsEvent = new LobbyDetailsEvent(name, null);
        assertEquals(name, lobbyDetailsEvent.getReceiverName().orElseThrow());
    }

    @Nested
    class LobbyEventTest {
        Lobby lobby = Mockito.mock(Lobby.class);
        LobbyEvent lobbyEvent;

        @Test
        void runLobbyActionRemovesTheLobby() {
            lobbyEvent = new GameEndEvent(null);
            lobbyEvent.runLobbyAction(lobby);
            Mockito.verify(lobby).remove();
            Mockito.clearInvocations(lobby);

            lobbyEvent = new PlayerExitEvent(null);
            lobbyEvent.runLobbyAction(lobby);
            Mockito.verify(lobby).remove();
        }
    }
}