package it.polimi.ingsw.galaxytruckers.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.*;
import javafx.scene.image.Image;

import java.awt.*;
import java.util.*;

public class SmugglersCard extends AdventureCard {
    private final int firePowerThreshold;
    private final Map<GoodsType, Integer> goodsPrize;
    private final int flightDaysLoss;
    private final int goodsLoss;
    private boolean defeated;
    private boolean acquired;

    public SmugglersCard (Image image, Level cardLevel, FlightBoard flightBoard, int goodsLoss, int firePowerThreshold, Map<GoodsType, Integer> goodsPrize, int flightDaysLoss) {
        super(image, cardLevel, flightBoard);
        this.firePowerThreshold = firePowerThreshold;
        this.goodsPrize = goodsPrize;
        this.flightDaysLoss = flightDaysLoss;
        this.goodsLoss = goodsLoss;
        this.acquired = false;
        this.defeated = false;
    }

    @Override
    public GameState nextStep() {
        // Evaluating previous player firepower, after double cannons activation
        if (!defeated) {
            if (currentShipBoard != null) {  // There is a previous player who needs their firepower evaluated
                if (currentShipBoard.getFirePower() > firePowerThreshold) {  // player defeats the enemy
                    defeated = true;
                    return new ChoiceState(); // Let the player choose whether to collect the prize
                } else if (currentShipBoard.getFirePower() < firePowerThreshold) { // player is defeated
                    ShipBoard tempShipBoard = currentShipBoard;
                    currentShipBoard = null;
                    return new RemoveGoodsState(goodsLoss, tempShipBoard);
                }
            }
            // Letting the currentPlayer activate double cannons
            if (currentPlayerIndex < flightBoard.getShipToPlace().size()) {  // There are other players to evaluate
                currentShipBoard = flightBoard.getOrderedShips().get(currentPlayerIndex);
                currentPlayerIndex++;
                Set<Point> availablePositions = new HashSet<>(currentShipBoard.getCannons().keySet());
                availablePositions.retainAll(currentShipBoard.getActivatables().keySet());
                return new ActivateState(availablePositions, currentShipBoard); // Let the player activate double cannons
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

    @Override
    public void choose(boolean choice) {
        // TODO: add: if (choice) { }
        if (choice) {
            flightBoard.displaceShip(currentShipBoard, -flightDaysLoss);
        }
        currentShipBoard = null;
    }
}
