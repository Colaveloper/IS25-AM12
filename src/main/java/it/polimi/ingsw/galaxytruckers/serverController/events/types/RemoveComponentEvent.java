package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.awt.*;

/**
 * Event that represents the removal of a component from a player's ship board.
 * This event is dispatched when a component is removed during ship building or modification.
 *
 * @param playerName The name of the player who owns the ship board
 * @param point The coordinates of the component being removed from the ship board
 */
public record RemoveComponentEvent(String playerName, Point point) implements LobbyEvent {
    /**
     * Creates a RemoveComponentEvent from a ship board and component location.
     *
     * @param shipBoard The ship board from which the component is being removed
     * @param point The coordinates of the component being removed
     * @return A new RemoveComponentEvent with the player's name extracted from the ship board
     */
    public static RemoveComponentEvent from(ShipBoard shipBoard, Point point) {
        return new RemoveComponentEvent(
                Player.getPlayer(shipBoard).getNickname(),
                point
        );
    }

}
