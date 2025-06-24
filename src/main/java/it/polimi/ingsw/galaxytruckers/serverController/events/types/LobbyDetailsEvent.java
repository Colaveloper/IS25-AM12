package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.serverController.dto.LobbyDetailsDTO;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Event that provides a player with detailed information about a specific lobby.
 * This event is dispatched when a player needs to be updated with the current state of a lobby,
 * including information about other players, game settings, and lobby status.
 *
 * @param playerName The name of the player receiving the lobby details
 * @param details A data transfer object containing all the detailed information about the lobby
 */
public record LobbyDetailsEvent(
        String playerName,
        LobbyDetailsDTO details
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
