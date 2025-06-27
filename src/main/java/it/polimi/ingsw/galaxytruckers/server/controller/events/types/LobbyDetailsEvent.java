package it.polimi.ingsw.galaxytruckers.server.controller.events.types;

import it.polimi.ingsw.galaxytruckers.server.controller.dto.LobbyDetailsDTO;

import java.util.Optional;

/**
 * Event containing details about the lobby for a specific player.
 * Should be sent when a player joins a lobby
 *
 * @param playerName the name of the player receiving the lobby details
 * @param details the details of the lobby
 */
public record LobbyDetailsEvent(
        String playerName,
        LobbyDetailsDTO details
) implements LobbyEvent {

    @Override
    public Optional<String> getReceiverName() {
        return Optional.of(playerName);
    }
}
