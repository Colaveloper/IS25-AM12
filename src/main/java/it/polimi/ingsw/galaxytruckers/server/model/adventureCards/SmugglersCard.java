package it.polimi.ingsw.galaxytruckers.server.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.server.model.Game;
import it.polimi.ingsw.galaxytruckers.server.model.state.*;
import it.polimi.ingsw.galaxytruckers.shared.enums.GoodsType;
import it.polimi.ingsw.galaxytruckers.shared.enums.Level;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.ShipBoard;

import java.util.*;

public class SmugglersCard extends AdventureCard {
    private final int firePowerThreshold;
    private final Map<GoodsType, Integer> goodsPrize;
    private final int flightDaysLoss;
    private final int goodsLoss;
    private boolean defeated;
    private boolean acquired;

    /**
     * Constructs a SmugglersCard.
     *
     * @param game the game instance
     * @param level the adventure card level
     * @param goodsLoss the number of goods lost if defeated
     * @param firePowerThreshold the firepower required to defeat the smugglers
     * @param goodsPrize the goods awarded for defeating the smugglers
     * @param flightDaysLoss the number of flight days lost when claiming the prize
     * @param id the unique card identifier
     */
    public SmugglersCard (Game game, Level level, int goodsLoss, int firePowerThreshold, Map<GoodsType, Integer> goodsPrize, int flightDaysLoss, int id) {
        super(game, level, id);
        this.firePowerThreshold = firePowerThreshold*2; // Firepower is doubled to account for half firepower from the ships
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
    public AdventureState getNextState() {
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
    }
}
