package it.polimi.ingsw.galaxytruckers.server.model.adventureCards.penalty;

import it.polimi.ingsw.galaxytruckers.server.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.server.model.state.AdventureState;

import java.util.Optional;

/**
 * Interface for applying penalties in the game.
 * It defines a method to inflict a penalty on a player, by changing his ship or the flight board.
 * It also specifies what state, if any, the player must be brought to after the penalty can be applied.
 */
public interface Penalty {
    /**
     * Inflicts a penalty on the player, modifying their ship or the flight board.
     * If the player must be brought to a specific state after the penalty can be applied, the returned
     * optional will contain that AdventureState.
     *
     * @param shipBoard the ShipBoard of the player being penalized
     * @param flightBoard the FlightBoard containing all players' ship boards
     * @return an Optional containing the AdventureState the player must be brought to after the
     * penalty, or empty if not applicable
     */
    Optional<AdventureState> inflictPenalty(ShipBoard shipBoard, FlightBoard flightBoard);
}
