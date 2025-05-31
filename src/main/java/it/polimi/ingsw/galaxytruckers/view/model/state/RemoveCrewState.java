package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class RemoveCrewState extends AdventureState {
    int crewSacrifice;
    ShipBoard shipBoard;

    public RemoveCrewState(int crewSacrifice, ShipBoard shipBoard) {
        this.crewSacrifice = crewSacrifice;
        this.shipBoard = shipBoard;
    }

    @Override
    public List<StateActions> getAvailableActions() {
        List<StateActions> actions = new ArrayList<>(super.getAvailableActions());
        actions.add(StateActions.LOSE_CREW);
        return actions;
    }

    @Override
    public void notifyLoseCrew(ShipBoard shipBoard, Point point) {
        shipBoard.loseCrew(point);
        game.getObservers().forEach(observer -> observer.notifyLoseCrew(shipBoard, point));
    }

    public ShipBoard getShipBoard() {
        return shipBoard;
    }
}
