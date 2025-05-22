package it.polimi.ingsw.galaxytruckers.model.state;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.Set;

public abstract class ActivateState extends AdventureState{
    Set<Point> availablePositions;
    ShipBoard shipBoard;
    int batteriesToSpend;
    int activatedComponents;

    ActivateState(ShipBoard shipBoard) {
        this.shipBoard = shipBoard;
        this.batteriesToSpend = 0;
        this.activatedComponents = 0;
    }

    @Override
    public void activateComponent(ShipBoard shipBoard, Point position) {
        if (!shipBoard.equals(this.shipBoard)) {
            throw new IllegalStateException("It's not your turn");
        }
        if (batteriesToSpend >= shipBoard.getNumBatteries()) {
            throw new IllegalStateException("You don't have enough batteries");
        }
        if (availablePositions.contains(position)) {
            if (shipBoard.activateComponent(position)) {
                batteriesToSpend++;
            }
        }
    }

    @Override
    public void spendBatteries(ShipBoard shipBoard, Point point, int amount) {
        if (!shipBoard.equals(this.shipBoard)) {
            throw new IllegalStateException("It's not your turn");
        }
        if (amount > batteriesToSpend) {
            throw new IllegalArgumentException("You are spending more batteries that required");
        }
        shipBoard.useBatteries(point, amount);
        batteriesToSpend -= amount;
    }

    @Override
    public void goNext(ShipBoard shipBoard) {
        if (!shipBoard.equals(this.shipBoard)) {
            throw new IllegalStateException("It's not your turn");
        }
        if (batteriesToSpend > 0) {
            throw new IllegalStateException("You still have batteries to spend");
        }
        game.setCurrentState(game.getDeck().getCurrentCard().nextStep());
    }
}
