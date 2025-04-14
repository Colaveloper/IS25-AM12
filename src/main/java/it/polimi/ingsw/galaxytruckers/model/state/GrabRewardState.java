package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

public class GrabRewardState extends GameState {
    Runnable rewardMethod;
    ShipBoard shipBoard;

    public GrabRewardState(Runnable rewardMethod) {
        this.rewardMethod = rewardMethod;
        //TODO : define a way to assign shipBoard attribute
    }

    @Override
    public void grabReward(ShipBoard shipBoard) {
        if (!shipBoard.equals(this.shipBoard)) {
            throw new IllegalStateException("It's not your turn");
        }
        rewardMethod.run();
        game.setCurrentState(game.getDeck().getCurrentCard().nextStep());
    }

    @Override
    public void goNext(ShipBoard shipBoard) {
        if (!shipBoard.equals(this.shipBoard)) {
            throw new IllegalStateException("It's not your turn");
        }
        game.setCurrentState(game.getDeck().getCurrentCard().nextStep());
    }
}
