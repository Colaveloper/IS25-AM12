package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.serverController.dto.ActiveLobbyDTO;

import java.util.List;
import java.util.Optional;

/**
 * Event representing the setting of the list of active lobbies for a player.
 *
 * @param playerName the name of the player receiving the list
 * @param activeLobbies the list of active lobbies
 * @param reconnect true if this event is sent as part of a reconnection process
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
