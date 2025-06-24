package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Cabin;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.awt.*;

/**
 * Event that represents the initialization of a cabin on a player's ship.
 * This event is dispatched when a cabin component is placed and configured with a specific crew type.
 *
 * @param playerName The name of the player who owns the ship
 * @param point The coordinates of the cabin on the ship board
 * @param crewType The type of crew assigned to the cabin
 */
public record InitializeCabinEvent(String playerName, Point point, CrewType crewType) implements LobbyEvent {
    /**
     * Creates an InitializeCabinEvent from a ship board, point, and cabin information.
     *
     * @param shipBoard The ship board containing the cabin
     * @param point The coordinates of the cabin on the ship board
     * @param cabin The cabin being initialized
     * @return A new InitializeCabinEvent with the player's name extracted from the ship board
     */
    public static InitializeCabinEvent from(ShipBoard shipBoard, Point point, Cabin cabin) {
        return new InitializeCabinEvent(
                Player.getPlayer(shipBoard).getNickname(),
                point,
                cabin.getCrewType()
        );
    }

}
