package it.polimi.ingsw.galaxytruckers.view.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.view.enums.Level;

import java.util.Map;

public final class AbandonedStationCard extends AdventureCard {
    private final int flightDaysLoss;
    private final Map<GoodsType, Integer> goodsPrize;
    private final int requiredCrew;

    public AbandonedStationCard (Level level, Map<GoodsType, Integer> goodsPrize, int requiredCrew, int flightDaysLoss, int id) {
        super(level, id);
        this.flightDaysLoss = flightDaysLoss;
        this.goodsPrize = goodsPrize;
        this.requiredCrew = requiredCrew;
    }

    public int getFlightDaysLoss() {
        return flightDaysLoss;
    }

    public Map<GoodsType, Integer> getGoodsPrize() {
        return goodsPrize;
    }

    public int getRequiredCrew() {
        return requiredCrew;
    }
}
