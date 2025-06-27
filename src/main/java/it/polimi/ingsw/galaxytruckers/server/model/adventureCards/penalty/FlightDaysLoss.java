package it.polimi.ingsw.galaxytruckers.server.model.adventureCards.penalty;

import it.polimi.ingsw.galaxytruckers.server.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.server.model.state.AdventureState;

import java.util.Optional;

/**
 * Class representing a penalty that causes the player to lose a certain number of flight days.
 * Implements the Penalty interface.
 */
public class FlightDaysLoss implements Penalty {
    int flightDaysToLose;

    public FlightDaysLoss(int flightDaysToLose) {
        this.flightDaysToLose = flightDaysToLose;
    }

    /**
     * Inflicts a penalty by moving the player's ship backward by the specified number of flight days.
     * No additional AdventureState is required after this penalty.
     *
     * @param shipBoard the ShipBoard of the player being penalized
     * @param flightBoard the FlightBoard containing all players' ship boards
     * @return an empty Optional, as no further state is needed
     */
    @Override
    public Optional<AdventureState> inflictPenalty(ShipBoard shipBoard, FlightBoard flightBoard) {
        flightBoard.displaceShip(shipBoard, -flightDaysToLose);
        return Optional.empty();
    }
}
