package it.polimi.ingsw.galaxytruckers.view.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.view.enums.Level;

public final class SlaversCard extends AdventureCard {

    private final int firePowerThreshold;
    private final int creditPrize;
    private final int flightDaysLoss;
    private final int crewLoss;

    public SlaversCard(Level level, int crewLoss, int firePowerThreshold, int creditPrize, int flightDaysLoss, int id) {
        super(level, id);
        this.firePowerThreshold = firePowerThreshold;
        this.creditPrize = creditPrize;
        this.flightDaysLoss = flightDaysLoss;
        this.crewLoss = crewLoss;
    }

    public int getFirePowerThreshold() {
        return firePowerThreshold;
    }

    public int getCreditPrize() {
        return creditPrize;
    }

    public int getFlightDaysLoss() {
        return flightDaysLoss;
    }

    public int getCrewLoss() {
        return crewLoss;
    }
}
