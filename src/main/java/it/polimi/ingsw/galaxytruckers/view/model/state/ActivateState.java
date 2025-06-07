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
    protected final ShipBoard shipBoard;
    protected final boolean isMyTurn;

    ActivateState(ShipBoard shipBoard, Set<Point> availablePositions, boolean isMyTurn) {
        this.isMyTurn = isMyTurn;
        this.shipBoard = shipBoard;
        this.availablePositions = availablePositions;
    }

    public List<StateActions> getAvailableActions() {
        List<StateActions> actions = new ArrayList<>();
        if (isMyTurn) {
            if (!shipBoard.getBatteries().isEmpty() && !shipBoard.getActivatables().isEmpty()) {
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

    public ShipBoard getShipBoard() {
        return shipBoard;
    }

}
