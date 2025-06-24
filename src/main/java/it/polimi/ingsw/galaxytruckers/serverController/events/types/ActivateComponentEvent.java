package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.awt.*;

/**
 * An event that represents the activation or deactivation of a component on a player's ship board.
 * This event is used to notify the system about component state changes in the game.
 *
 * @param playerName The name of the player who owns the ship board
 * @param point The coordinates of the component on the ship board
 * @param active Whether the component is being activated (true) or deactivated (false)
 */
public record ActivateComponentEvent(String playerName, Point point, boolean active) implements LobbyEvent {
    /**
     * Creates an ActivateComponentEvent from a ship board and component information.
     *
     * @param shipBoard The ship board containing the component
     * @param point The coordinates of the component on the ship board
     * @param active Whether the component is being activated (true) or deactivated (false)
     * @return A new ActivateComponentEvent with the player's name extracted from the ship board
     */
    public static ActivateComponentEvent from(ShipBoard shipBoard, Point point, boolean active) {
        return new ActivateComponentEvent(
                Player.getPlayer(shipBoard).getNickname(),
                point,
                active
        );
    }
}
