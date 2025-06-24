package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.awt.*;

/**
 * Event that represents a player using a battery component on their ship.
 * This event is dispatched when a player activates a battery to power systems
 * or components at a specific location on their ship board.
 *
 * @param playerName The name of the player using the battery
 * @param point The coordinates on the ship board where the battery is being used
 */
public record UseBatteryEvent(String playerName, Point point) implements LobbyEvent {
    /**
     * Creates a UseBatteryEvent from a ship board and battery location.
     *
     * @param shipBoard The ship board on which the battery is being used
     * @param point The coordinates on the ship board where the battery is being used
     * @return A new UseBatteryEvent with the player's name extracted from the ship board
     */
    public static UseBatteryEvent from(ShipBoard shipBoard, Point point) {
        return new UseBatteryEvent(
                Player.getPlayer(shipBoard).getNickname(),
                point
        );
    }

}
