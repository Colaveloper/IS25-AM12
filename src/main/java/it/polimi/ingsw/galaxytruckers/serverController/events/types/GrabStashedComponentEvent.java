package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

/**
 * Event that represents a player grabbing a component from their stash.
 * This event is dispatched when a player selects a previously stashed component
 * for potential placement on their ship.
 *
 * @param playerName The name of the player grabbing the component
 * @param index The index of the component in the player's stash
 */
public record GrabStashedComponentEvent(String playerName, int index) implements LobbyEvent {
    /**
     * Creates a GrabStashedComponentEvent from a ship board and stash index.
     *
     * @param shipBoard The ship board associated with the player
     * @param index The index of the component in the player's stash
     * @return A new GrabStashedComponentEvent with the player's name extracted from the ship board
     */
    public static GrabStashedComponentEvent from(ShipBoard shipBoard, int index) {
        return new GrabStashedComponentEvent(
                Player.getPlayer(shipBoard).getNickname(),
                index
        );
    }

}
