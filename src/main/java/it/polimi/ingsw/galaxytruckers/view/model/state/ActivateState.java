package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.ArrayList;
import java.util.Set;
import java.util.List;

public sealed abstract class ActivateState extends AdventureState permits
        DeclareFirePowerState,
        DeclareEnginePowerState,
        HandleProjectileState
{
    protected final Set<Point> availablePositions;
    protected final boolean isMyTurn;

    ActivateState(ShipBoard myShip, ShipBoard currentShip, Set<Point> availablePositions) {
        this.myShip = myShip;
        this.isMyTurn = currentShip.equals(myShip);
        this.currentShip = currentShip;
        this.availablePositions = availablePositions;
    }

    public List<StateActions> getAvailableActions() {
        List<StateActions> actions = new ArrayList<>();
        if(imOut) return actions;
        if (isMyTurn) {
            if (!currentShip.getBatteries().isEmpty() && !currentShip.getActivatables().isEmpty()) {
                actions.add(StateActions.SPEND_BATTERIES);
                actions.add(StateActions.ACTIVATE_COMPONENT);
            }
            actions.add(StateActions.GO_NEXT);
        }
        actions.addAll(super.getAvailableActions());
        return actions;
    }

    @Override
    public void notifyActivateComponent(ShipBoard shipBoard, Point point) {
        shipBoard.activateComponent(point);
        game.getObservers().forEach(observer -> observer.notifyActivateComponent(shipBoard, point));
    }

    @Override
    public void notifyUseBattery(ShipBoard shipBoard, Point point) {
        shipBoard.useBattery(point);
        game.getObservers().forEach(observer -> observer.notifyUseBattery(shipBoard, point));
    }

    public Set<Point> getAvailablePositions() {
        return availablePositions;
    }
}
