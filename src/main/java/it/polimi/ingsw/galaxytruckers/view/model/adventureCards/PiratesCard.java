package it.polimi.ingsw.galaxytruckers.view.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;

import java.util.List;

public final class PiratesCard extends AdventureCard {
    // Card Parameters
    private final int firePowerThreshold;
    private final List<Projectile> projectiles;
    private final int creditPrize;
    private final int flightDaysLoss;

    public PiratesCard(Level level, int firePowerThreshold, int creditPrize, int flightDaysLoss, List<Projectile> projectiles, int id) {
        super(level, id);
        this.firePowerThreshold = firePowerThreshold;
        this.creditPrize = creditPrize;
        this.flightDaysLoss = flightDaysLoss;
        this.projectiles = projectiles.reversed();  // list is inverted to be treated as a stack
    }

    public int getFirePowerThreshold() {
        return firePowerThreshold;
    }

    public List<Projectile> getProjectiles() {
        return projectiles;
    }

    public int getCreditPrize() {
        return creditPrize;
    }

    public int getFlightDaysLoss() {
        return flightDaysLoss;
    }
}
