package it.polimi.ingsw.galaxytruckers.model.adventureCards.penalty;

import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.AdventureState;
import it.polimi.ingsw.galaxytruckers.model.state.GameState;

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

    @Override
    public Optional<AdventureState> inflictPenalty(ShipBoard shipBoard, FlightBoard flightBoard) {
        flightBoard.displaceShip(shipBoard, -flightDaysToLose);
        return Optional.empty();
    }
}
