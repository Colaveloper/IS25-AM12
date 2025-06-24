package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.awt.*;
import java.util.List;
import java.util.Set;

/**
 * Event that represents a validation failure due to disconnected ship components.
 * This event is dispatched when a player's ship contains separate, unconnected pieces
 * that do not form a single cohesive structure.
 *
 * @param playerName The name of the player whose ship failed validation
 * @param shipPieces A list of sets of points representing the disconnected ship pieces
 */
public record ShipNotConnectedEvent(String playerName, List<Set<Point>> shipPieces) implements LobbyEvent {
    /**
     * Creates a ShipNotConnectedEvent from a ship board and its disconnected pieces.
     *
     * @param shipBoard The ship board being validated
     * @param shipPieces A list of sets of points representing the disconnected ship pieces
     * @return A new ShipNotConnectedEvent with the player's name extracted from the ship board
     */
    public static ShipNotConnectedEvent from(ShipBoard shipBoard, List<Set<Point>> shipPieces) {
        return new ShipNotConnectedEvent(
                Player.getPlayer(shipBoard).getNickname(),
                shipPieces
        );
    }

}
