package it.polimi.ingsw.galaxytruckers.view.model.state;

import java.util.ArrayList;
import java.util.List;

/**
 * A specialized ship building state used for testing purposes in the Galaxy Truckers game.
 * This state extends the standard ship building functionality with additional actions
 * that allow placing ships for test scenarios.
 */
public final class TestShipBuildingState extends ShipBuildingState {
    /**
     * Creates a new TestShipBuildingState.
     * Initializes the state with the default ship building configuration.
     */
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
