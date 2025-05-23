package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.util.ArrayList;
import java.util.List;

public class DrawCardState extends AdventureState {
    private final static List<StateActions> availableActions = List.of(
            StateActions.DRAW_CARD
    );
    private final ShipBoard shipBoard;

    public DrawCardState(ShipBoard shipBoard) {
        this.shipBoard = shipBoard;
    }

    @Override
    public List<StateActions> getAvailableActions() {
        List<StateActions> actions = new ArrayList<>(availableActions);
        actions.addAll(super.getAvailableActions());
        return actions;
    }
}
