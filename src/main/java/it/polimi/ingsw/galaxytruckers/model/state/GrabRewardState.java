package it.polimi.ingsw.galaxytruckers.model.state;

public class GrabRewardState extends GameState {
    Runnable rewardMethod;

    public GrabRewardState(Runnable rewardMethod) {
        this.rewardMethod = rewardMethod;
    }

    @Override
    public void grabReward() {
        rewardMethod.run();
        goNext();
    }

    @Override
    public void goNext() {
        game.setCurrentState(game.getDeck().getCurrentCard().nextStep());
    }
}
