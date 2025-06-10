package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class GrabRewardState extends AdventureState {

    public GrabRewardState(ShipBoard myShip, ShipBoard currentShip) {
        this.myShip = myShip;
        this.currentShip = currentShip;
    }

    @Override
    public List<StateActions> getAvailableActions() {
        List<StateActions> actions = new ArrayList<>();
        if(currentShip.equals(myShip)) {
            actions.add(StateActions.GRAB_REWARD);
            actions.add(StateActions.GO_NEXT);
        }
        actions.addAll(super.getAvailableActions());
        return actions;
    }

    public void notifyGrabReward(ShipBoard shipBoard, int credits){
        currentShip.addCredits(credits);
    }
}
