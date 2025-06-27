package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.serverController.lobby.Lobby;

import java.util.Optional;

/**
 * Event triggered when all but one player disconnect from the game and the
 * last remaining player should win by default.
 *
 * @param winnerName the name of the player who remains in the game, or null
 *                   if no players are left
 */
public record EndByDisconnectionEvent(String winnerName) implements LobbyEvent {

    @Override
    public Optional<String> getReceiverName() {
        return Optional.ofNullable(winnerName);
    }
}
