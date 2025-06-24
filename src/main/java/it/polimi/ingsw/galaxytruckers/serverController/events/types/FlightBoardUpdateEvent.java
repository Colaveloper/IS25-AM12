package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

/**
 * Event that represents an update to a player's position on the flight board.
 * This event is dispatched when a player's ship moves to a new position during gameplay.
 *
 * @param playerName The name of the player whose position is being updated
 * @param position The new position of the player's ship on the flight board
 */
public record FlightBoardUpdateEvent(String playerName, int position) implements LobbyEvent {

    /**
     * Creates a FlightBoardUpdateEvent from a ship board and position information.
     *
     * @param shipBoard The ship board associated with the player
     * @param position The new position of the player's ship on the flight board
     * @return A new FlightBoardUpdateEvent with the player's name extracted from the ship board
     */
    public static FlightBoardUpdateEvent from(ShipBoard shipBoard, int position) {
        return new FlightBoardUpdateEvent(
                Player.getPlayer(shipBoard).getNickname(),
                position
        );
    }
}
