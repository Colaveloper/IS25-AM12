package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.util.ArrayList;
import java.util.List;

public final class GrabRewardState extends AdventureState {
    private static final List<StateActions> availableActions = List.of(
            StateActions.GRAB_REWARD,
            StateActions.GO_NEXT
    );

    private final ShipBoard shipBoard;

    public GrabRewardState(ShipBoard shipBoard) {
        this.shipBoard = shipBoard;
    }

    @Override
    public List<StateActions> getAvailableActions() {
        List<StateActions> actions = new ArrayList<>(availableActions);
        actions.addAll(super.getAvailableActions());
        return actions;
    }

    public ShipBoard getShipBoard() {
        return shipBoard;
    }
}
