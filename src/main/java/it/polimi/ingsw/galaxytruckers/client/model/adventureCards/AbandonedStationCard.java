package it.polimi.ingsw.galaxytruckers.client.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.shared.enums.GoodsType;
import it.polimi.ingsw.galaxytruckers.shared.enums.Level;

import java.util.Map;

/**
 * Represents an adventure card for an abandoned station event in the game.
 * Stores information about the required crew, goods prize, and flight days lost.
 * Extends {@link AdventureCard} and implements {@link AdventureCardInterface}.
 */
public final class AbandonedStationCard extends AdventureCard implements AdventureCardInterface {
    private final int flightDaysLoss;
    private final Map<GoodsType, Integer> goodsPrize;
    private final int requiredCrew;

    /**
     * Constructs an AbandonedStationCard with the specified parameters.
     *
     * @param level the level of the card
     * @param goodsPrize the goods prize for claiming the station
     * @param requiredCrew the number of crew required
     * @param flightDaysLoss the number of flight days lost
     * @param id the unique identifier for the card
     */
    public AbandonedStationCard (Level level, Map<GoodsType, Integer> goodsPrize, int requiredCrew, int flightDaysLoss, int id) {
        super(level, id);
        this.flightDaysLoss = flightDaysLoss;
        this.goodsPrize = goodsPrize;
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
     * Returns the goods prize for claiming the abandoned station.
     *
     * @return a map of goods types to their quantities
     */
    public Map<GoodsType, Integer> getGoodsPrize() {
        return goodsPrize;
    }

    /**
     * Returns the number of crew members required to claim the abandoned station.
     *
     * @return the required crew
     */
    public int getRequiredCrew() {
        return requiredCrew;
    }
}
