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
    private final int maxActivate;
    private int batteriesActivated;
    private int componentActivated;

    ActivateState(ShipBoard myShip, ShipBoard currentShip, Set<Point> availablePositions) {
        this.myShip = myShip;
        this.isMyTurn = currentShip.equals(myShip);
        this.currentShip = currentShip;
        this.availablePositions = availablePositions;
        this.maxActivate = Math.min(currentShip.getNumBatteries(), availablePositions.size());
        batteriesActivated = 0;
        componentActivated = 0;
    }

    public List<StateActions> getAvailableActions() {
        List<StateActions> actions = new ArrayList<>();
        if(imOut) return actions;
        if (isMyTurn) {
            if (!currentShip.getBatteries().isEmpty() && !currentShip.getActivatables().isEmpty()) {
                if(batteriesActivated < maxActivate) actions.add(StateActions.SPEND_BATTERIES);
                if(componentActivated < maxActivate) actions.add(StateActions.ACTIVATE_COMPONENT);
            }
            if(componentActivated == batteriesActivated) actions.add(StateActions.GO_NEXT);
        }
        actions.addAll(super.getAvailableActions());
        return actions;
    }

    @Override
    public void notifyActivateComponent(ShipBoard shipBoard, Point point) {
        shipBoard.activateComponent(point);
        componentActivated++;
        game.getObservers().forEach(observer -> observer.notifyActivateComponent(shipBoard, point));
    }

    @Override
    public void notifyUseBattery(ShipBoard shipBoard, Point point) {
        shipBoard.useBattery(point);
        batteriesActivated++;
        game.getObservers().forEach(observer -> observer.notifyUseBattery(shipBoard, point));
    }

    public Set<Point> getAvailablePositions() {
        return availablePositions;
    }
}
