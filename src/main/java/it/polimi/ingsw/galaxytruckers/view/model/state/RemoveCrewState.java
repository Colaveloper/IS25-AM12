package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class RemoveCrewState extends AdventureState {
    int crewSacrifice;

    public RemoveCrewState(ShipBoard myShip, int crewSacrifice, ShipBoard currentShip) {
        this.myShip = myShip;
        this.crewSacrifice = crewSacrifice;
        this.currentShip = currentShip;
    }

    @Override
    public List<StateActions> getAvailableActions() {
        List<StateActions> actions = new ArrayList<>();
        if(currentShip.equals(myShip)) actions.add(StateActions.LOSE_CREW);
        actions.addAll(super.getAvailableActions());
        return actions;
    }

    @Override
    public void notifyLoseCrew(ShipBoard shipBoard, Point point) {
        shipBoard.loseCrew(point);
        game.getObservers().forEach(observer -> observer.notifyLoseCrew(shipBoard, point));
    }
}
