package it.polimi.ingsw.galaxytruckers.serverController.events.types;

/**
 * Event signaling that a player's ship has become valid.
 *
 * @param playerName the name of the player whose ship is being validated
 */
public record ValidateShipEvent(String playerName) implements LobbyEvent {
}
