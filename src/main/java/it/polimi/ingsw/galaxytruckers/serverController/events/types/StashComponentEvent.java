package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

/**
 * Event that represents a player stashing a component for later use.
 * This event is dispatched when a player places a component in their personal stash
 * rather than immediately on their ship board, typically during the ship building phase.
 *
 * @param playerName The name of the player stashing the component
 */
public record StashComponentEvent(String playerName) implements LobbyEvent {
    /**
     * Creates a StashComponentEvent from a ship board.
     *
     * @param shipBoard The ship board associated with the player stashing the component
     * @return A new StashComponentEvent with the player's name extracted from the ship board
     */
    public static StashComponentEvent from(ShipBoard shipBoard) {
        return new StashComponentEvent(
                Player.getPlayer(shipBoard).getNickname()
        );
    }

}
