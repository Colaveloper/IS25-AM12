package it.polimi.ingsw.galaxytruckers.client.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.shared.enums.Level;

/**
 * Represents a Slavers adventure card in the game.
 * Extends {@link AdventureCard} and implements {@link AdventureCardInterface}.
 */
public final class SlaversCard extends AdventureCard implements AdventureCardInterface {
    private final int firePowerThreshold;
    private final int creditPrize;
    private final int flightDaysLoss;
    private final int crewLoss;

    /**
     * Constructs a new SlaversCard with the specified parameters.
     *
     * @param level the level of the card
     * @param crewLoss the number of crew members lost
     * @param firePowerThreshold the firepower threshold to overcome the card
     * @param creditPrize the prize in credits for overcoming the card
     * @param flightDaysLoss the number of flight days lost if failed
     * @param id the unique identifier for the card
     */
    public SlaversCard(Level level, int crewLoss, int firePowerThreshold, int creditPrize, int flightDaysLoss, int id) {
        super(level, id);
        this.firePowerThreshold = firePowerThreshold;
        this.creditPrize = creditPrize;
        this.flightDaysLoss = flightDaysLoss;
        this.crewLoss = crewLoss;
    }

    /**
     * Gets the firepower threshold required to overcome the card.
     * @return the firepower threshold
     */
    public int getFirePowerThreshold() {
        return firePowerThreshold;
    }

    /**
     * Gets the credit prize for overcoming the card.
     * @return the credit prize
     */
    public int getCreditPrize() {
        return creditPrize;
    }

    /**
     * Gets the number of flight days lost if the card is not overcome.
     * @return the number of flight days lost
     */
    public int getFlightDaysLoss() {
        return flightDaysLoss;
    }

    /**
     * Gets the number of crew members lost if the card is not overcome.
     * @return the number of crew members lost
     */
    public int getCrewLoss() {
        return crewLoss;
    }
}
