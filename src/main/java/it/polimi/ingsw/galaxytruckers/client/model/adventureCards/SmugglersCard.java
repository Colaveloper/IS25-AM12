package it.polimi.ingsw.galaxytruckers.client.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.shared.enums.GoodsType;
import it.polimi.ingsw.galaxytruckers.shared.enums.Level;

import java.util.Map;

/**
 * Represents a Smugglers adventure card in the game.
 * Extends {@link AdventureCard} and implements {@link AdventureCardInterface}.
 */
public final class SmugglersCard extends AdventureCard implements AdventureCardInterface {
    private final int firePowerThreshold;
    private final Map<GoodsType, Integer> goodsPrize;
    private final int flightDaysLoss;
    private final int goodsLoss;

    /**
     * Constructs a SmugglersCard with the specified parameters.
     *
     * @param level the level of the card
     * @param goodsLoss the number of goods lost if failed
     * @param firePowerThreshold the firepower required to succeed
     * @param goodsPrize the goods awarded as a prize
     * @param flightDaysLoss the number of flight days lost if failed
     * @param id the unique identifier of the card
     */
    public SmugglersCard (Level level, int goodsLoss, int firePowerThreshold, Map<GoodsType, Integer> goodsPrize, int flightDaysLoss, int id) {
        super(level, id);
        this.firePowerThreshold = firePowerThreshold;
        this.goodsPrize = goodsPrize;
        this.flightDaysLoss = flightDaysLoss;
        this.goodsLoss = goodsLoss;
    }

    /**
     * Gets the firepower threshold required to avoid penalties.
     * @return the firepower threshold
     */
    public int getFirePowerThreshold() {
        return firePowerThreshold;
    }

    /**
     * Gets the goods awarded as a prize.
     * @return a map of goods and their quantities
     */
    public Map<GoodsType, Integer> getGoodsPrize() {
        return goodsPrize;
    }

    /**
     * Gets the number of flight days lost if the challenge is failed.
     * @return the number of flight days lost
     */
    public int getFlightDaysLoss() {
        return flightDaysLoss;
    }

    /**
     * Gets the number of goods lost if the challenge is failed.
     * @return the number of goods lost
     */
    public int getGoodsLoss() {
        return goodsLoss;
    }
}
