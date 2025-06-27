package it.polimi.ingsw.galaxytruckers.client.model.adventureCards.penalty;

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
