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
    private static final List<StateActions> availableActions = List.of(
            StateActions.ACTIVATE_COMPONENT,
            StateActions.SPEND_BATTERIES,
            StateActions.GO_NEXT
    );

    protected final Set<Point> availablePositions;
    protected final ShipBoard shipBoard;
    protected int batteriesToSpend;

    ActivateState(ShipBoard shipBoard, Set<Point> availablePositions) {
        this.shipBoard = shipBoard;
        this.batteriesToSpend = 0;
        this.availablePositions = availablePositions;
    }

    public List<StateActions> getAvailableActions() {
        List<StateActions> actions = new ArrayList<>(availableActions);
        actions.addAll(super.getAvailableActions());
        return actions;
    }

    @Override
    public void notifyActivateComponent(ShipBoard shipBoard, Point point) {
        shipBoard.activateComponent(point);
        batteriesToSpend++;
        game.getObservers().forEach(observer -> observer.notifyActivateComponent(shipBoard, point));
    }

    @Override
    public void notifyUseBattery(ShipBoard shipBoard, Point point) {
        shipBoard.useBattery(point);
        batteriesToSpend--;
        game.getObservers().forEach(observer -> observer.notifyUseBattery(shipBoard, point));
    }

    public Set<Point> getAvailablePositions() {
        return availablePositions;
    }

    public ShipBoard getShipBoard() {
        return shipBoard;
    }

    public int getBatteriesToSpend() {
        return batteriesToSpend;
    }
}
