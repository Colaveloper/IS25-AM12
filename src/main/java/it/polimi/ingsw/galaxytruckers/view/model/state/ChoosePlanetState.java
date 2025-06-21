package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class ChoosePlanetState extends AdventureState {
    private final ShipBoard[] options;
    private boolean isMyTurn;

    public ChoosePlanetState(ShipBoard myShip, ShipBoard currentShip, int numPlanets) {
        this.myShip = myShip;
        options = new ShipBoard[numPlanets];
        this.currentShip = currentShip;
        this.isMyTurn = myShip.equals(currentShip);
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
        if(choice != -1) options[choice] = shipBoard;
        game.getObservers().forEach(observer -> observer.notifyChoosePlanet(shipBoard,choice,nextShipBoard));
    }

    @Override
    public void notifyCurrentPlayerUpdate(ShipBoard shipBoard) {
        isMyTurn = myShip.equals(shipBoard);
        game.getObservers().forEach(observer -> observer.notifyCurrentPlayerUpdate(shipBoard));
    }

    public boolean isMyTurn() {
        return isMyTurn;
    }

    public ShipBoard[] getOptions() {
        return options;
    }

    public void setOptions(ShipBoard[] options) {
        System.arraycopy(options, 0, this.options, 0, this.options.length);
    }
}
