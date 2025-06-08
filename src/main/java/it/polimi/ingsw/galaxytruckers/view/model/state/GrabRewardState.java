package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class GrabRewardState extends AdventureState {

    private final ShipBoard shipBoard;

    public GrabRewardState(ShipBoard myShip, ShipBoard shipBoard) {
        this.myShip = myShip;
        this.shipBoard = shipBoard;
    }

    @Override
    public List<StateActions> getAvailableActions() {
        List<StateActions> actions = new ArrayList<>();
        if(shipBoard.equals(myShip)) {
            actions.add(StateActions.GRAB_REWARD);
            actions.add(StateActions.GO_NEXT);
        }
        actions.addAll(super.getAvailableActions());
        return actions;
    }

    public ShipBoard getShipBoard() {
        return shipBoard;
    }

    public void notifyGrabReward(ShipBoard shipBoard, boolean rewardGrabbed){}
}
