package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.serverController.dto.GameSnapshot;
import it.polimi.ingsw.galaxytruckers.serverController.dto.LobbyDetailsDTO;

import java.util.Optional;

/**
 * Event that provides a complete snapshot of the current game state for a specific player.
 * This event is dispatched when a player needs to receive a comprehensive update of the game,
 * typically after joining a game in progress or reconnecting after a disconnection.
 *
 * @param playerName The name of the player receiving the game snapshot
 * @param lobbyDetails Details about the lobby the player is in, including other players and settings
 * @param gameSnapshot A complete representation of the current game state, such as the flightboard,
 * ships, and current card id
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
