package it.polimi.ingsw.galaxytruckers.serverController.events.types;

/**
 * Event representing the removal of a ship piece by a player.
 *
 * @param playerName the name of the player removing the ship piece
 * @param index the index of the ship piece being removed
 */
public record ShipPieceRemoveEvent(String playerName, int index) implements LobbyEvent {
}
