package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.serverController.dto.GameSnapshot;
import it.polimi.ingsw.galaxytruckers.serverController.dto.LobbyDetailsDTO;

import java.util.Optional;

/**
 * Event containing a snapshot of the current game state for a specific player.
 *
 * @param playerName the name of the player receiving the snapshot
 * @param lobbyDetails the details of the lobby at the time of the snapshot
 * @param gameSnapshot the snapshot of the game state
 */
public record GameSnapshotEvent(
        String playerName,
        LobbyDetailsDTO lobbyDetails,
        GameSnapshot gameSnapshot
) implements LobbyEvent {

    @Override
    public boolean shouldResume() {
        return true;
    }

    @Override
    public Optional<String> getReceiverName() {
        return Optional.of(playerName);
    }
}
