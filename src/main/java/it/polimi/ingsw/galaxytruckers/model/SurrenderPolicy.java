package it.polimi.ingsw.galaxytruckers.model;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.SurrenderCause;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.util.Set;

/**
 * Interface representing the surrender policy in the game.
 * It defines methods to check if surrender is enabled, request surrender,
 * confirm surrender, and retrieve surrendered ships.
 */
public interface SurrenderPolicy {

    /**
     * Checks if surrender is enabled in the game.
     *
     * @return true if surrender is enabled, false otherwise
     */
    boolean isSurrenderEnabled();

    /**
     * Requests surrender for a given ship board with a specified cause.
     *
     * @param shipBoard the ship board requesting surrender
     * @param cause the cause of the surrender
     * @return true if the surrender request is successful, false otherwise
     */
    boolean requestSurrender(ShipBoard shipBoard, SurrenderCause cause);

    /**
     * Confirms the surrender for a given flight board.
     *
     * @param flightBoard the flight board for which surrender is confirmed
     * @return a set of ship boards that have surrendered
     */
    Set<ShipBoard> confirmSurrender(FlightBoard flightBoard);

    /**
     * Retrieves the set of ship boards that have surrendered.
     *
     * @return a set of surrendered ship boards
     */
    Set<ShipBoard> getSurrenderedShips();
}
