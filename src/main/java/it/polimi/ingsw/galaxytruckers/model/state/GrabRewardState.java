package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

public final class GrabRewardState extends AdventureState implements GameStateInterface{
    Runnable rewardMethod;
    ShipBoard shipBoard;

    public GrabRewardState(ShipBoard shipBoard, Runnable rewardMethod) {
        this.rewardMethod = rewardMethod;
        this.shipBoard = shipBoard;
    }

    @Override
    public synchronized void grabReward(ShipBoard shipBoard) {
        if (!shipBoard.equals(this.shipBoard)) {
            throw new IllegalStateException("It's not your turn");
        }
        checkIfExpired();
        rewardMethod.run();
        game.getEventListener().notifyGrabCreditsEvent(shipBoard, shipBoard.getCredits());
        getNextState();
    }

    @Override
    public synchronized void goNext(ShipBoard shipBoard) {
        if (!shipBoard.equals(this.shipBoard)) {
            throw new IllegalStateException("It's not your turn");
        }
        checkIfExpired();
        getNextState();
    }

    public synchronized ShipBoard getShipBoard() {
        return shipBoard;
    }
}
