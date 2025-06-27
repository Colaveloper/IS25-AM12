package it.polimi.ingsw.galaxytruckers.server.controller.events.types;

/**
 * Event representing a player flipping the hourglass.
 *
 * @param playerName the name of the player who flipped the hourglass
 */
public record FlipHourglassEvent(String playerName) implements LobbyEvent {
}
