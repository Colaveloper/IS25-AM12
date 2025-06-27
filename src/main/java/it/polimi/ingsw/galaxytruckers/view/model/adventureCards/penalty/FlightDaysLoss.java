package it.polimi.ingsw.galaxytruckers.view.model.adventureCards.penalty;

import it.polimi.ingsw.galaxytruckers.view.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;

import java.util.Optional;

/**
 * Represents a penalty that causes the loss of flight days in the game.
 * Implements the {@link Penalty} interface.
 */
public final class FlightDaysLoss implements Penalty {
    int flightDays;

    /**
     * Constructs a FlightDaysLoss penalty with the specified number of flight days lost.
     *
     * @param flightDays the number of flight days to lose
     */
    public FlightDaysLoss(int flightDays) {
        this.flightDays = flightDays;
    }
}
