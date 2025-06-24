package it.polimi.ingsw.galaxytruckers.serverController.events.types;

/**
 * Event that represents a player disconnecting from the game.
 * This event is dispatched when a player's connection to the server is lost,
 * allowing the game to handle the disconnection appropriately.
 *
 * @param playerName The name of the player who disconnected from the game
 */
public record PlayerDisconnectionEvent(String playerName) implements LobbyEvent {
}
