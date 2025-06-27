package it.polimi.ingsw.galaxytruckers.server.controller.events.types;

/**
 * Event representing the disconnection of a player from the lobby.
 *
 * @param playerName the name of the player who disconnected
 */
public record PlayerDisconnectionEvent(String playerName) implements LobbyEvent {
}
