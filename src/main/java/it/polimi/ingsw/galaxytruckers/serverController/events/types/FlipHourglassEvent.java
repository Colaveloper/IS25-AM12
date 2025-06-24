package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

/**
 * Event that represents a player flipping an hourglass during gameplay.
 * This event is dispatched when a player performs an action that requires flipping the hourglass,
 * typically to track time-sensitive activities or turns.
 *
 * @param playerName The name of the player who flipped the hourglass
 */
public record FlipHourglassEvent(String playerName) implements LobbyEvent {

    /**
     * Creates a FlipHourglassEvent from a ship board.
     *
     * @param shipBoard The ship board associated with the player who flipped the hourglass
     * @return A new FlipHourglassEvent with the player's name extracted from the ship board
     */
    public static FlipHourglassEvent from(ShipBoard shipBoard) {
        return new FlipHourglassEvent(
                Player.getPlayer(shipBoard).getNickname()
        );
    }

}
