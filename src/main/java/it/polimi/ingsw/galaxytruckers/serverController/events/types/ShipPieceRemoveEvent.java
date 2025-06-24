package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

/**
 * Event that represents the removal of a disconnected ship piece.
 * This event is dispatched when a specific disconnected ship piece is removed from
 * a player's ship board.
 *
 * @param playerName The name of the player whose ship piece is being removed
 * @param index The index of the disconnected ship piece to remove
 */
public record ShipPieceRemoveEvent(String playerName, int index) implements LobbyEvent {
    /**
     * Creates a ShipPieceRemoveEvent from a ship board and piece index.
     *
     * @param shipBoard The ship board from which the piece is being removed
     * @param index The index of the disconnected ship piece to remove
     * @return A new ShipPieceRemoveEvent with the player's name extracted from the ship board
     */
    public static ShipPieceRemoveEvent from(ShipBoard shipBoard, int index) {
        return new ShipPieceRemoveEvent (
                Player.getPlayer(shipBoard).getNickname(),
                index
        );
    }
}
