package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class ChoosePlanetState extends AdventureState {
    private final ShipBoard shipBoard;
    private final Set<Integer> options;
    private boolean isMyTurn;

    public ChoosePlanetState(ShipBoard shipBoard, Set<Integer> options, boolean isMyTurn) {
        this.options = options;
        this.shipBoard = shipBoard;
        this.isMyTurn = isMyTurn;
    }

    @Override
    public List<StateActions> getAvailableActions() {
        List<StateActions> actions = new ArrayList<>();
        if(!options.isEmpty() && isMyTurn) {
            actions.add(StateActions.CHOOSE_PLANET);
            actions.add(StateActions.GO_NEXT);
        }
        actions.addAll(super.getAvailableActions());
        return actions;
    }

    @Override
    public void notifyChoosePlanet(ShipBoard shipBoard, int choice, boolean isMyTurn) {
        this.isMyTurn = isMyTurn;
        options.remove(choice);
        game.getObservers().forEach(observer -> observer.notifyChoosePlanet(shipBoard,choice));
    }

    public ShipBoard getShipBoard() {
        return shipBoard;
    }

    public Set<Integer> getOptions() {
        return options;
    }
}
