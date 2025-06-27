package it.polimi.ingsw.galaxytruckers.server.model.adventureCards.penalty;

import it.polimi.ingsw.galaxytruckers.server.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.server.model.state.AdventureState;
import it.polimi.ingsw.galaxytruckers.server.model.state.RemoveCrewState;

import java.util.Optional;

/**
 * Represents a penalty that causes the player to lose a specified number of crew members.
 * If the number of crew members to lose is zero, no penalty is inflicted.
 */
public class CrewLoss implements  Penalty {
    int crewToLose;

    public CrewLoss(int crewToLose) {
        this.crewToLose = crewToLose;
    }

    /**
     * Inflicts a penalty by removing a specified number of crew members from the player's ship.
     * If the number to lose is zero, no penalty is applied.
     *
     * @param shipBoard the ShipBoard of the player being penalized
     * @param flightBoard the FlightBoard containing all players' ship boards
     * @return an Optional containing a RemoveCrewState if crew is to be removed, or empty if not
     */
    @Override
    public Optional<AdventureState> inflictPenalty(ShipBoard shipBoard, FlightBoard flightBoard) {
        if (crewToLose == 0) {
            return Optional.empty();
        } else {
            int crewToLose = this.crewToLose;
            this.crewToLose = 0;
            return Optional.of(new RemoveCrewState(crewToLose, shipBoard));
        }
    }
}
