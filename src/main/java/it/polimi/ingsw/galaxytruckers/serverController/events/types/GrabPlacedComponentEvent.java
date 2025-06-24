package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

/**
 * Event that represents a player grabbing a previously placed component from their ship.
 * This event is dispatched when a player picks up a component that was already placed on their
 * ship board, typically to reposition it during the ship building phase.
 *
 * @param playerName The name of the player grabbing the component
 */
public record GrabPlacedComponentEvent(String playerName) implements LobbyEvent{
    /**
     * Creates a GrabPlacedComponentEvent from a ship board.
     *
     * @param shipBoard The ship board from which the component is being grabbed
     * @return A new GrabPlacedComponentEvent with the player's name extracted from the ship board
     */
    public static GrabPlacedComponentEvent from(ShipBoard shipBoard) {
        return new GrabPlacedComponentEvent(
                Player.getPlayer(shipBoard).getNickname()
        );
    }
}
