package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.view.Direction;
import it.polimi.ingsw.galaxytruckers.view.controller.ComponentRegistry;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ComponentBank;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public sealed abstract class ShipBuildingState extends GameState permits
        SecondShipBuildingState,
        TestShipBuildingState
{
    private final ComponentBank componentBank;
    protected final Set<ShipBoard> completedShipBoards;

    public ShipBuildingState() {
        this.completedShipBoards = new HashSet<>();
        this.componentBank = new ComponentBank(ComponentRegistry.getInstance().getSize());
    }

    @Override
    public void leave() {
        game.getShipBoards().forEach(ShipBoard::finishBuilding);
    }

    @Override
    public List<StateActions> getAvailableActions() {
        List<StateActions> actions = new ArrayList<>();
        if(componentBank.getCoveredComponentsN() != 0) actions.add(StateActions.REQUEST_RAND_COMPONENT);
        if (!componentBank.getUncoveredComponents().isEmpty()) actions.add(StateActions.REQUEST_COMPONENT);
        actions.add(StateActions.REJECT_COMPONENT);
        actions.add(StateActions.PLACE_COMPONENT);
        actions.add(StateActions.PLACE_SHIP_ON_FLIGHTBOARD);
        //TODO: implement conditional available actions if needed
        return actions;
    }

    @Override
    public void notifyRequestRandComponent(ShipBoard shipBoard, Component component) {
        shipBoard.offerComponent(component);
        componentBank.removeCoveredComponent();
        game.getObservers().forEach(observer -> observer.notifyRequestRandComponent(shipBoard, component));
    }

    @Override
    public void notifyRequestComponent(ShipBoard shipBoard, Component component) {
        shipBoard.offerComponent(component);
        componentBank.removeUncoveredComponent(component);
        game.getObservers().forEach(observer -> observer.notifyRequestComponent(shipBoard, component));
    }

    @Override
    public void notifyRejectComponent(ShipBoard shipBoard) {
        Point prevPos = shipBoard.getLastPosition();
        Component component = shipBoard.rejectComponent();
        componentBank.addUncoveredComponent(component);
        if (prevPos != null) {
            game.getObservers().forEach(observer -> observer.notifyRejectComponent(shipBoard, component, prevPos));
        } else {
            game.getObservers().forEach(observer -> observer.notifyRejectComponent(shipBoard, component));
        }
    }

    @Override
    public void notifyPlaceComponent(ShipBoard shipBoard, Point point, Direction orientation) {
        Point prevPos = shipBoard.getLastPosition();
        shipBoard.placeComponent(point, orientation);
        if (prevPos != null) {
            game.getObservers().forEach(observer -> observer.notifyPlaceComponent(shipBoard, prevPos, orientation, prevPos));
        } else {
            game.getObservers().forEach(observer -> observer.notifyPlaceComponent(shipBoard, point, orientation));
        }
    }

    public ComponentBank getComponentBank(){
        return componentBank;
    }
}
