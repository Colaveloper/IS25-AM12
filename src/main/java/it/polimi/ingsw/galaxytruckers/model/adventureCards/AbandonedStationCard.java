package it.polimi.ingsw.galaxytruckers.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.state.AddGoodsState;
import it.polimi.ingsw.galaxytruckers.model.state.GrabRewardState;
import it.polimi.ingsw.galaxytruckers.model.state.DrawCardState;
import it.polimi.ingsw.galaxytruckers.model.state.GameState;

import java.util.Map;

public class AbandonedStationCard extends AdventureCard {
    private final int flightDaysLoss;
    private final Map<GoodsType, Integer> goodsPrize;
    private final int requiredCrew;
    private boolean accepted;
    private boolean acquired;

    public AbandonedStationCard (Game game, Level level, Map<GoodsType, Integer> goodsPrize, int requiredCrew, int flightDaysLoss) {
        super(game, level);
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
    public GameState nextStep() {
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
                return nextStep();
            }
        }
        if (!acquired) {
            acquired = true;
            return new AddGoodsState(goodsPrize, currentShipBoard); // Let the player choose whether to collect the prize
        }
        return new DrawCardState();
    }


    public void getReward() {
        flightBoard.displaceShip(currentShipBoard, -flightDaysLoss);
        accepted = true;
    }
}
