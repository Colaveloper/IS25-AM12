package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class ChoosePlanetState extends AdventureState {
    private final ShipBoard shipBoard;
    private final ShipBoard[] options;
    private boolean isMyTurn;

    public ChoosePlanetState(ShipBoard myShip, ShipBoard shipBoard, int numPlanets) {
        this.myShip = myShip;
        options = new ShipBoard[numPlanets];
        this.shipBoard = shipBoard;
        this.isMyTurn = myShip.equals(shipBoard);
    }

    @Override
    public List<StateActions> getAvailableActions() {
        List<StateActions> actions = new ArrayList<>();
        if(isMyTurn) {
            actions.add(StateActions.CHOOSE_PLANET);
            actions.add(StateActions.GO_NEXT);
        }
        actions.addAll(super.getAvailableActions());
        return actions;
    }

    @Override
    public void notifyChoosePlanet(ShipBoard shipBoard, int choice, ShipBoard nextShipBoard) {
        isMyTurn = myShip.equals(nextShipBoard);
        options[choice] = shipBoard;
        game.getObservers().forEach(observer -> observer.notifyChoosePlanet(shipBoard,choice,nextShipBoard));
    }

    public ShipBoard getShipBoard() {
        return shipBoard;
    }

    public boolean isMyTurn() {
        return isMyTurn;
    }

    public ShipBoard[] getOptions() {
        return options;
    }
}
