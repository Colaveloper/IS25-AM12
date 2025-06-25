package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

public final class GrabRewardState extends AdventureState implements GameStateInterface {
    private final Runnable rewardMethod;
    private final ShipBoard shipBoard;

    public GrabRewardState(ShipBoard shipBoard, Runnable rewardMethod) {
        this.rewardMethod = rewardMethod;
        this.shipBoard = shipBoard;
    }

    @Override
    public synchronized void skip(ShipBoard shipBoard) {
        if (!expired && this.shipBoard.equals(shipBoard)) {
            getNextState();
        }
    }

    @Override
    public synchronized void grabReward(ShipBoard shipBoard) {
        if (!this.shipBoard.equals(shipBoard)) {
            throw new IllegalStateException("It's not your turn");
        }
        checkIfExpired();
        rewardMethod.run();
        getNextState();
    }

    @Override
    public synchronized void goNext(ShipBoard shipBoard) {
        if (!this.shipBoard.equals(shipBoard)) {
            throw new IllegalStateException("It's not your turn");
        }
        checkIfExpired();
        getNextState();
    }

    public synchronized ShipBoard getShipBoard() {
        return shipBoard;
    }
}
