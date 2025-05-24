package it.polimi.ingsw.galaxytruckers.view.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.view.enums.GoodsType;
import it.polimi.ingsw.galaxytruckers.view.enums.Level;

import java.util.Map;

public final class SmugglersCard extends AdventureCard {
    private final int firePowerThreshold;
    private final Map<GoodsType, Integer> goodsPrize;
    private final int flightDaysLoss;
    private final int goodsLoss;

    public SmugglersCard (Level level, int goodsLoss, int firePowerThreshold, Map<GoodsType, Integer> goodsPrize, int flightDaysLoss, int id) {
        super(level, id);
        this.firePowerThreshold = firePowerThreshold;
        this.goodsPrize = goodsPrize;
        this.flightDaysLoss = flightDaysLoss;
        this.goodsLoss = goodsLoss;
    }

    public int getFirePowerThreshold() {
        return firePowerThreshold;
    }

    public Map<GoodsType, Integer> getGoodsPrize() {
        return goodsPrize;
    }

    public int getFlightDaysLoss() {
        return flightDaysLoss;
    }

    public int getGoodsLoss() {
        return goodsLoss;
    }
}
