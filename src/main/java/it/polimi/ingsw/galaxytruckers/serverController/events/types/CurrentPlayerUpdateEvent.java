package it.polimi.ingsw.galaxytruckers.serverController.events.types;

/**
 * Event that represents an update to the current active player in a game lobby.
 *
 * @param playerName The name of the player who is now the current active player
 */
public record CurrentPlayerUpdateEvent(String playerName) implements LobbyEvent {
}
