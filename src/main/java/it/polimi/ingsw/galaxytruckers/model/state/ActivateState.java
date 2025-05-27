package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.Set;

public abstract sealed class ActivateState extends AdventureState permits DeclareEnginePowerState,
                                                                          DeclareFirePowerState, HandleProjectileState,
                                                                          ActivateStateStub {
    Set<Point> availablePositions;
    ShipBoard shipBoard;
    int batteriesToSpend;
    int activatedComponents;

    protected ActivateState(ShipBoard shipBoard) {
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
                game.getEventListener().notifyActivateComponentEvent(shipBoard,position,true);
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
        game.getEventListener().notifyUserBatteryEvent(shipBoard,point);
    }

    @Override
    public void goNext(ShipBoard shipBoard) {
        if (!shipBoard.equals(this.shipBoard)) {
            throw new IllegalStateException("It's not your turn");
        }
        if (batteriesToSpend > 0) {
            throw new IllegalStateException("You still have batteries to spend");
        }
        game.setCurrentState(super.getNextState());
    }

    public Set<Point> getAvailablePositions() {
        return availablePositions;
    }

    public ShipBoard getShipBoard() {
        return shipBoard;
    }

    public int getBatteriesToSpend() {
        return batteriesToSpend;
    }

    public int getActivatedComponents() {
        return activatedComponents;
    }
}
