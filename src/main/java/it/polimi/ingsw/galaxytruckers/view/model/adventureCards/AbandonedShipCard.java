package it.polimi.ingsw.galaxytruckers.view.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;

public final class AbandonedShipCard extends AdventureCard implements AdventureCardInterface {
    private final int flightDaysLoss;
    private final int creditPrize;
    private final int requiredCrew;

    public AbandonedShipCard (Level level, int creditPrize, int requiredCrew, int flightDaysLoss, int id) {
        super(level, id);
        this.flightDaysLoss = flightDaysLoss;
        this.creditPrize = creditPrize;
        this.requiredCrew = requiredCrew;
    }

    public int getFlightDaysLoss() {
        return flightDaysLoss;
    }

    public int getCreditPrize() {
        return creditPrize;
    }

    public int getRequiredCrew() {
        return requiredCrew;
    }
}
