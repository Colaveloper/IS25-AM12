package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;
import it.polimi.ingsw.galaxytruckers.view.Direction;

import java.awt.*;

/**
 * Event that represents a player placing a component on their ship board.
 * This event is dispatched when a player positions a component at a specific location
 * with a particular rotation during the ship building phase.
 *
 * @param playerName The name of the player placing the component
 * @param position The coordinates on the ship board where the component is being placed
 * @param rotation The orientation/direction of the component being placed
 */
public record PlaceComponentEvent(String playerName, Point position, Direction rotation) implements LobbyEvent {
    /**
     * Creates a PlaceComponentEvent from a ship board, component, and position information.
     *
     * @param shipBoard The ship board on which the component is being placed
     * @param component The component being placed on the ship board
     * @param position The coordinates on the ship board where the component is being placed
     * @return A new PlaceComponentEvent with the player's name and component orientation extracted from the provided parameters
     */
    public static PlaceComponentEvent from(ShipBoard shipBoard, Component component, Point position) {
        return new PlaceComponentEvent(
                Player.getPlayer(shipBoard).getNickname(),
                position,
                component.getOrientation()
        );
    }

}
