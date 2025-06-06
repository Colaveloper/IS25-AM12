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
    public void grabReward(ShipBoard shipBoard) {
        if (!shipBoard.equals(this.shipBoard)) {
            throw new IllegalStateException("It's not your turn");
        }
        rewardMethod.run();
        game.setCurrentState(super.getNextState());
    }

    @Override
    public void goNext(ShipBoard shipBoard) {
        if (!shipBoard.equals(this.shipBoard)) {
            throw new IllegalStateException("It's not your turn");
        }
        game.setCurrentState(super.getNextState());
    }

    public ShipBoard getShipBoard() {
        return shipBoard;
    }
}
