package it.polimi.ingsw.galaxytruckers.serverController.events.types;

/**
 * Event representing the disconnection of a player from the lobby.
 *
 * @param playerName the name of the player who disconnected
 */
public record PlayerDisconnectionEvent(String playerName) implements LobbyEvent {
}
