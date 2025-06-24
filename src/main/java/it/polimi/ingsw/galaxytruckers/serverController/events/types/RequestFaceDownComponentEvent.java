package it.polimi.ingsw.galaxytruckers.serverController.events.types;

/**
 * Event signaling that a player has requested a face-down component from the bank.
 *
 * @param playerName  the name of the player who performed the request
 * @param componentId the ID of the requested component
 */
public record RequestFaceDownComponentEvent(String playerName, int componentId) implements LobbyEvent {
}
