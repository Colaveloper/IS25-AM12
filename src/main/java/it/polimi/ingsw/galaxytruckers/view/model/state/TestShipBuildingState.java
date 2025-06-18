package it.polimi.ingsw.galaxytruckers.view.model.state;

import java.util.ArrayList;
import java.util.List;

public final class TestShipBuildingState extends ShipBuildingState {
    public TestShipBuildingState() {
        super();
    }

    @Override
    public List<StateActions> getAvailableActions() {
        List<StateActions> actions = new ArrayList<>(super.getAvailableActions());
        if(!hasFinished) actions.add(StateActions.PLACE_SHIP_FOR_TEST);
        return actions;
    }
}
