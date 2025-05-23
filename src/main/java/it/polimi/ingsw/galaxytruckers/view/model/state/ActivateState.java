package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.ArrayList;
import java.util.Set;
import java.util.List;

public abstract class ActivateState extends AdventureState {
    private static final List<StateActions> availableActions = List.of(
            StateActions.ACTIVATE_COMPONENT,
            StateActions.SPEND_BATTERIES,
            StateActions.GO_NEXT
    );

    Set<Point> availablePositions;
    ShipBoard shipBoard;
    int batteriesToSpend;

    ActivateState(ShipBoard shipBoard) {
        this.shipBoard = shipBoard;
        this.batteriesToSpend = 0;
    }

    public List<StateActions> getAvailableActions() {
        List<StateActions> actions = new ArrayList<>(availableActions);
        actions.addAll(super.getAvailableActions());
        return actions;
    }
}
