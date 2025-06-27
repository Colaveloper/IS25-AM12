package it.polimi.ingsw.galaxytruckers.model.adventureCards;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.state.*;

import java.util.Map;

/**
 * Represents an Abandoned Station adventure card in the Galaxy Truckers game.
 */
public class AbandonedStationCard extends AdventureCard {
    private final int flightDaysLoss;
    private final Map<GoodsType, Integer> goodsPrize;
    private final int requiredCrew;
    private boolean accepted;
    private boolean acquired;

    /**
     * Constructs an AbandonedStationCard.
     *
     * @param game the game instance
     * @param level the adventure card level
     * @param goodsPrize the goods awarded for claiming the station
     * @param requiredCrew the number of crew required to claim the station
     * @param flightDaysLoss the number of flight days lost when claiming the station
     * @param id the unique card identifier
     */
    public AbandonedStationCard (Game game, Level level, Map<GoodsType, Integer> goodsPrize, int requiredCrew, int flightDaysLoss, int id) {
        super(game, level, id);
        this.flightDaysLoss = flightDaysLoss;
        this.goodsPrize = goodsPrize;
        this.requiredCrew = requiredCrew;
    }

    @Override
    public void initialize() {
        super.initialize();
        this.accepted = false;
        this.acquired = false;
    }

    @Override
    public AdventureState getNextState() {
        if(!accepted) {
            if (currentPlayerIndex < flightBoard.getShipToPlace().size()) { // There are other players to evaluate
                currentShipBoard = flightBoard.getOrderedShips().get(currentPlayerIndex);
            }
            else {
                return new DrawCardState();
            }
            currentPlayerIndex++;
            if (currentShipBoard.getCrewSize() >= requiredCrew) {
                return new GrabRewardState(currentShipBoard, this::getReward);
            }
            else {
                return getNextState();
            }
        }
        if (!acquired) {
            acquired = true;
            return new AddGoodsState(goodsPrize, currentShipBoard); // Let the player choose whether to collect the prize
        }
        return new DrawCardState();
    }


    /**
     * Grants the player the reward for claiming the abandoned station.
     * Visible for testing purposes.
     */
    @VisibleForTesting
    protected void getReward() {
        flightBoard.displaceShip(currentShipBoard, -flightDaysLoss);
        accepted = true;
    }
}
