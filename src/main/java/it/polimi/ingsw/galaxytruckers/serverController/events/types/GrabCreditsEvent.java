package it.polimi.ingsw.galaxytruckers.serverController.events.types;

/**
 * Event representing a player grabbing credits.
 *
 * @param playerName the name of the player grabbing credits
 * @param value the amount of credits grabbed
 */
public record GrabCreditsEvent(String playerName, int value) implements LobbyEvent {
}
