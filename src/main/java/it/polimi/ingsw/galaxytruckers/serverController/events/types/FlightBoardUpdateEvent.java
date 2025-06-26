package it.polimi.ingsw.galaxytruckers.serverController.events.types;

/**
 * Event representing an update to the flight board position of a player
 * @param playerName the name of the player whose position is being updated
 * @param position the new position of the player on the flight board
 */
public record FlightBoardUpdateEvent(String playerName, int position) implements LobbyEvent {
}
