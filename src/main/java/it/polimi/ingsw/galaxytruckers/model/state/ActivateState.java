package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.*;
import java.util.List;

public abstract class ActivateState extends AdventureState {
    protected final Set<Point> availablePositions;
    protected final ShipBoard shipBoard;
    protected int batteriesToSpend;
    protected int activatedComponents;

    protected ActivateState(ShipBoard shipBoard, Set<Point> availablePositions) {
        this.shipBoard = shipBoard;
        this.batteriesToSpend = 0;
        this.activatedComponents = 0;
        this.availablePositions = availablePositions;
    }

    @Override
    public synchronized void skip(ShipBoard shipBoard) {
        if (!expired && this.shipBoard.equals(shipBoard)) {
            if (batteriesToSpend > 0) {
                Iterator<Point> positions = shipBoard.getBatteries().keySet().iterator();
                while (batteriesToSpend > 0) {
                    Point currentPosition = positions.next();
                    int min = Integer.min(batteriesToSpend, shipBoard.getBatteries().get(currentPosition).getNumBatteries());
                    for (int i = 0; i < min; i++) {
                        shipBoard.useBatteries(currentPosition);
                        batteriesToSpend--;
                    }
                }
            } else if (batteriesToSpend < 0) {
                Iterator<Point> positions = getAvailablePositions().stream()
                        .filter(p -> !shipBoard.getActivatables().get(p).isActive())
                        .iterator();
                while (batteriesToSpend < 0) {
                    Point currentPosition = positions.next();
                    shipBoard.activateComponent(currentPosition);
                    batteriesToSpend++;
                }
            }
            endStateAction();
        }
    }

    @Override
    public synchronized void activateComponent(ShipBoard shipBoard, Point position) {
        if (!this.shipBoard.equals(shipBoard)) {
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
            }
        }
    }

    @Override
    public synchronized void spendBatteries(ShipBoard shipBoard, Point point) {
        if (!this.shipBoard.equals(shipBoard)) {
            throw new IllegalStateException("It's not your turn");
        }
        checkIfExpired();
        if (batteriesToSpend <= 0 && activatedComponents - batteriesToSpend >= availablePositions.size()) {
            throw new IllegalStateException("You don't have components to spend that battery on");
        }
        shipBoard.useBatteries(point);
        batteriesToSpend--;
    }

    @Override
    public synchronized void goNext(ShipBoard shipBoard) {
        if (!this.shipBoard.equals(shipBoard)) {
            throw new IllegalStateException("It's not your turn");
        }
        checkIfExpired();
        if (batteriesToSpend > 0) {
            throw new IllegalStateException("You still have batteries to spend");
        } else if (batteriesToSpend < 0) {
            throw new IllegalStateException("You still have to activate components");
        }
        endStateAction();
    }

    protected void endStateAction() {
        getNextState();
    }

    public synchronized Set<Point> getAvailablePositions() {
        return availablePositions;
    }

    public synchronized ShipBoard getShipBoard() {
        return shipBoard;
    }
}
