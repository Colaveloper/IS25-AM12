package it.polimi.ingsw.galaxytruckers.view.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;

/**
 * Represents an adventure card for an abandoned ship event in the game.
 * Stores information about the required crew, credit prize, and flight days lost.
 * Extends {@link AdventureCard} and implements {@link AdventureCardInterface}.
 */
public final class AbandonedShipCard extends AdventureCard implements AdventureCardInterface {
    private final int flightDaysLoss;
    private final int creditPrize;
    private final int requiredCrew;

    /**
     * Constructs an AbandonedShipCard with the specified parameters.
     *
     * @param level the level of the card
     * @param creditPrize the credit prize for claiming the ship
     * @param requiredCrew the number of crew required
     * @param flightDaysLoss the number of flight days lost
     * @param id the unique identifier for the card
     */
    public AbandonedShipCard (Level level, int creditPrize, int requiredCrew, int flightDaysLoss, int id) {
        super(level, id);
        this.flightDaysLoss = flightDaysLoss;
        this.creditPrize = creditPrize;
        this.requiredCrew = requiredCrew;
    }

    /**
     * Returns the number of flight days lost.
     *
     * @return the number of flight days lost
     */
    public int getFlightDaysLoss() {
        return flightDaysLoss;
    }

    /**
     * Returns the credit prize for claiming the abandoned ship.
     *
     * @return the credit prize
     */
    public int getCreditPrize() {
        return creditPrize;
    }

    /**
     * Returns the number of crew members required to claim the abandoned ship.
     *
     * @return the required crew
     */
    public int getRequiredCrew() {
        return requiredCrew;
    }
}
