package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

/**
 * Event that represents a player collecting or receiving credits.
 * This event is dispatched when a player gains credits during gameplay.
 *
 * @param playerName The name of the player receiving the credits
 * @param value The amount of credits being received
 */
public record GrabCreditsEvent(String playerName, int value) implements LobbyEvent {
    /**
     * Creates a GrabCreditsEvent from a ship board and credit value.
     *
     * @param shipBoard The ship board associated with the player receiving credits
     * @param value The amount of credits being received
     * @return A new GrabCreditsEvent with the player's name extracted from the ship board
     */
    public static GrabCreditsEvent from(ShipBoard shipBoard, int value) {
        return new GrabCreditsEvent(
                Player.getPlayer(shipBoard).getNickname(),
                value
        );
    }
}
