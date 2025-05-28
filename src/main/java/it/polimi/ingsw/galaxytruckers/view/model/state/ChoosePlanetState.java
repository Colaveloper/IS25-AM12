package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class ChoosePlanetState extends AdventureState {
    private static final List<StateActions> availableActions = List.of(
            StateActions.CHOOSE_PLANET,
            StateActions.GO_NEXT
    );

    private final ShipBoard shipBoard;
    private final Set<Integer> options;

    public ChoosePlanetState(ShipBoard shipBoard, Set<Integer> options) {
        this.options = options;
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

    public Set<Integer> getOptions() {
        return options;
    }
}
