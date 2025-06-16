package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.Set;

public abstract class ActivateState extends AdventureState {
    protected final Set<Point> availablePositions;
    protected final ShipBoard shipBoard;
    int batteriesToSpend;
    int activatedComponents;

    protected ActivateState(ShipBoard shipBoard, Set<Point> availablePositions) {
        this.shipBoard = shipBoard;
        this.batteriesToSpend = 0;
        this.activatedComponents = 0;
        this.availablePositions = availablePositions;
    }

    @Override
    public synchronized void activateComponent(ShipBoard shipBoard, Point position) {
        if (!shipBoard.equals(this.shipBoard)) {
            throw new IllegalStateException("It's not your turn");
        }
        checkIfExpired();
        if (batteriesToSpend >= shipBoard.getNumBatteries()) {
            throw new IllegalStateException("You don't have enough batteries");
        }
        if (availablePositions.contains(position)) {
            if (shipBoard.activateComponent(position)) {
                batteriesToSpend++;
                activatedComponents++;
                game.getEventListener().notifyActivateComponentEvent(shipBoard,position,true);
            }
        }
    }

    @Override
    public synchronized void spendBatteries(ShipBoard shipBoard, Point point) {
        if (!shipBoard.equals(this.shipBoard)) {
            throw new IllegalStateException("It's not your turn");
        }
        checkIfExpired();
        if (batteriesToSpend <= 0 && activatedComponents - batteriesToSpend >= availablePositions.size()) {
            throw new IllegalStateException("You don't have components to spend that battery on");
        }
        shipBoard.useBatteries(point);
        batteriesToSpend--;
        game.getEventListener().notifyUseBatteryEvent(shipBoard,point);
    }

    @Override
    public synchronized void goNext(ShipBoard shipBoard) {
        if (!shipBoard.equals(this.shipBoard)) {
            throw new IllegalStateException("It's not your turn");
        }
        checkIfExpired();
        if (batteriesToSpend > 0) {
            throw new IllegalStateException("You still have batteries to spend");
        } else if (batteriesToSpend < 0) {
            throw new IllegalStateException("You still have to activate components");
        }
        getNextState();
    }

    public synchronized Set<Point> getAvailablePositions() {
        return availablePositions;
    }

    public synchronized ShipBoard getShipBoard() {
        return shipBoard;
    }

    public synchronized int getBatteriesToSpend() {
        return batteriesToSpend;
    }

    public synchronized int getActivatedComponents() {
        return activatedComponents;
    }
}
