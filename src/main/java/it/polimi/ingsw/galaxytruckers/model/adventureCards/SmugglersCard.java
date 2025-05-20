package it.polimi.ingsw.galaxytruckers.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.*;

import java.util.*;

public class SmugglersCard extends AdventureCard {
    private final int firePowerThreshold;
    private final Map<GoodsType, Integer> goodsPrize;
    private final int flightDaysLoss;
    private final int goodsLoss;
    private boolean defeated;
    private boolean acquired;

    public SmugglersCard (Game game, Level level, int goodsLoss, int firePowerThreshold, Map<GoodsType, Integer> goodsPrize, int flightDaysLoss, int id) {
        super(game, level, id);
        this.firePowerThreshold = firePowerThreshold;
        this.goodsPrize = goodsPrize;
        this.flightDaysLoss = flightDaysLoss;
        this.goodsLoss = goodsLoss;
    }

    @Override
    public void initialize() {
        super.initialize();
        this.acquired = false;
        this.defeated = false;
    }

    @Override
    public GameState nextStep() {
        // Evaluating previous player firepower, after double cannons activation
        if (!defeated) {
            if (currentShipBoard != null) {  // There is a previous player who needs their firepower evaluated
                int currentFirePower = currentShipBoard.getFirePower();
                currentShipBoard.deactivateAll();
                if (currentFirePower > firePowerThreshold) {  // player defeats the enemy
                    defeated = true;
                    return new GrabRewardState(currentShipBoard, this::getReward); // Let the player choose whether to collect the prize
                } else if (currentFirePower < firePowerThreshold) { // player is defeated
                    ShipBoard tempShipBoard = currentShipBoard;
                    currentShipBoard = null;
                    return new RemoveGoodsState(goodsLoss, tempShipBoard);
                }
            }
            // Letting the currentPlayer activate double cannons
            if (currentPlayerIndex < flightBoard.getShipToPlace().size()) {  // There are other players to evaluate
                currentShipBoard = flightBoard.getOrderedShips().get(currentPlayerIndex);
                currentPlayerIndex++;
                return new DeclareFirePowerState(currentShipBoard); // Let the player activate double cannons
            } else {  // There are no more players and no one has defeated the enemy
                defeated = true;
                return new DrawCardState();
            }
        } else if (!acquired) {
            acquired = true;
            return new AddGoodsState(goodsPrize, currentShipBoard);
        } else {
            return new DrawCardState();
        }
    }

    public void getReward() {
        flightBoard.displaceShip(currentShipBoard, -flightDaysLoss);
        currentShipBoard = null;
    }
}
