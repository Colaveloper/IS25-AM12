package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.serverController.dto.ActiveLobbyDTO;

import java.util.List;
import java.util.Optional;

/**
 * Event that provides a player with information about all active lobbies in the game.
 * This event is dispatched when a player needs to be updated about available lobbies,
 * typically after connecting or reconnecting to the game server.
 *
 * @param playerName The name of the player receiving the lobby information
 * @param activeLobbies A list of DTOs containing information about all active lobbies
 * @param reconnect Whether this is a reconnection event (true) or a new connection (false)
 */
public record SetActiveLobbiesEvent(String playerName, List<ActiveLobbyDTO> activeLobbies, boolean reconnect) implements ControllerEvent {
    @Override
    public boolean shouldResume() {
        return true;
    }

    @Override
    public Optional<String> getReceiverName() {
        return Optional.of(playerName);
    }
}
