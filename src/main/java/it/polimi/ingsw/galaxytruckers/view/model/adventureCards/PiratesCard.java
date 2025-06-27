package it.polimi.ingsw.galaxytruckers.view.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;

import java.util.List;

/**
 * Represents an adventure card for a pirates event in the game.
 * Stores the firepower threshold, credit prize, flight days lost, and projectiles associated with the pirates.
 * Extends {@link AdventureCard} and implements {@link AdventureCardInterface}.
 */
public final class PiratesCard extends AdventureCard implements AdventureCardInterface {
    // Card Parameters
    private final int firePowerThreshold;
    private final List<Projectile> projectiles;
    private final int creditPrize;
    private final int flightDaysLoss;

    /**
     * Constructs a PiratesCard with the specified parameters.
     *
     * @param level the level of the card
     * @param firePowerThreshold the firepower threshold required to defeat the pirates
     * @param creditPrize the credit prize for defeating the pirates
     * @param flightDaysLoss the number of flight days lost if not defeated
     * @param projectiles the list of projectiles associated with the pirates
     * @param id the unique identifier for the card
     */
    public PiratesCard(Level level, int firePowerThreshold, int creditPrize, int flightDaysLoss, List<Projectile> projectiles, int id) {
        super(level, id);
        this.firePowerThreshold = firePowerThreshold;
        this.creditPrize = creditPrize;
        this.flightDaysLoss = flightDaysLoss;
        this.projectiles = projectiles.reversed();  // list is inverted to be treated as a stack
    }

    /**
     * Returns the firepower threshold required to defeat the pirates.
     *
     * @return the firepower threshold
     */
    public int getFirePowerThreshold() {
        return firePowerThreshold;
    }

    /**
     * Returns the list of projectiles associated with the pirates card.
     *
     * @return the list of projectiles
     */
    public List<Projectile> getProjectiles() {
        return projectiles;
    }

    /**
     * Returns the credit prize for defeating the pirates.
     *
     * @return the credit prize
     */
    public int getCreditPrize() {
        return creditPrize;
    }

    /**
     * Returns the number of flight days lost if the pirates are not defeated.
     *
     * @return the number of flight days lost
     */
    public int getFlightDaysLoss() {
        return flightDaysLoss;
    }
}
