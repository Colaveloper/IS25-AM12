package it.polimi.ingsw.galaxytruckers.serverController.events.types;

/**
 * Event indicating that the current player has changed.
 * This event is used to notify listeners about the player whose turn it is now.
 *
 * @param playerName the name of the player who is now the current player
 */
public record CurrentPlayerUpdateEvent(String playerName) implements LobbyEvent {
}
