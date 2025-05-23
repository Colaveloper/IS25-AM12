package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.view.model.ComponentRegistry;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ComponentBank;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class ShipBuildingState extends GameState {
    private final ComponentBank componentBank;
    protected final Set<ShipBoard> completedShipBoards;

    public ShipBuildingState() {
        this.completedShipBoards = new HashSet<>();
        this.componentBank = new ComponentBank(ComponentRegistry.getInstance().getComponentNumber());
    }

    @Override
    public List<StateActions> getAvailableActions() {
        List<StateActions> actions = new ArrayList<>();
        actions.add(StateActions.REQUEST_RAND_COMPONENT);
        if (!componentBank.getUncoveredComponents().isEmpty()) {
            actions.add(StateActions.REQUEST_COMPONENT);
        }
        actions.add(StateActions.REJECT_COMPONENT);
        actions.add(StateActions.PLACE_COMPONENT);
        actions.add(StateActions.PLACE_SHIP_ON_FLIGHTBOARD);
        //TODO: implement conditional available actions if needed
        return actions;
    }
}
