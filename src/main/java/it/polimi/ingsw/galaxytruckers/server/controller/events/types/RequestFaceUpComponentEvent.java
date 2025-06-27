package it.polimi.ingsw.galaxytruckers.server.controller.events.types;

/**
 * Event signaling that a player has requested a face-up component from the bank.
 *
 * @param playerName the name of the player who performed the request
 * @param componentId the ID of the requested component
 */
public record RequestFaceUpComponentEvent(String playerName, int componentId) implements LobbyEvent {
}
