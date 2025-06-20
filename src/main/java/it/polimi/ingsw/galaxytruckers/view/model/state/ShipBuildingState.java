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
    protected boolean hasStashed = false;
    protected boolean hasFinished = false;


    public ShipBuildingState() {
        this.completedShipBoards = new HashSet<>();
        this.componentBank = new ComponentBank();
    }

    @Override
    public void leave() {
        game.getShipBoards().forEach(ShipBoard::finishBuilding);
    }

    @Override
    public List<StateActions> getAvailableActions() {
        List<StateActions> actions = new ArrayList<>();
        if(!hasFinished) {
            if(componentBank.getCoveredComponentsN() != 0) actions.add(StateActions.REQUEST_RAND_COMPONENT);
            if(!componentBank.getUncoveredComponents().isEmpty()) actions.add(StateActions.REQUEST_COMPONENT);
            if((myShip.getLastComponent() != null) && !hasStashed) actions.add(StateActions.REJECT_COMPONENT);
            if(myShip.getLastComponent() != null) actions.add(StateActions.PLACE_COMPONENT);
            if(componentInHand()) actions.add(StateActions.ROTATE_COMPONENT);
            if(myShip.getLastPosition() != null) actions.add(StateActions.GRAB_PLACED_COMPONENT);
        }
        return actions;
    }

    @Override
    public void notifyFlightBoardPosition(ShipBoard shipBoard, int position, boolean isMyShip) {
        if(isMyShip) hasFinished = true;
        game.getFlightBoard().setShipPosition(shipBoard, position);
        game.getObservers().forEach(observer -> observer.notifyFlightBoardPosition(shipBoard, position));
    }

    @Override
    public void notifyRequestRandComponent(ShipBoard shipBoard, Component component) {
        hasStashed = false;
        shipBoard.offerComponent(component);
        componentBank.removeCoveredComponent();
        game.getObservers().forEach(observer -> observer.notifyRequestRandComponent(shipBoard, component));
    }

    @Override
    public void notifyRequestComponent(ShipBoard shipBoard, Component component) {
        hasStashed = false;
        shipBoard.offerComponent(component);
        componentBank.removeUncoveredComponent(component);
        game.getObservers().forEach(observer -> observer.notifyRequestComponent(shipBoard, component));
    }

    @Override
    public void notifyGrabPlacedComponent(ShipBoard shipBoard) {
        Point prevPosition = shipBoard.getLastPosition();
        shipBoard.grabPlacedComponent();
        game.getObservers().forEach(observer -> observer.notifyGrabPlacedComponent(shipBoard,prevPosition));
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
            game.getObservers().forEach(observer -> observer.notifyPlaceComponent(shipBoard, point, orientation, prevPos));
        } else {
            game.getObservers().forEach(observer -> observer.notifyPlaceComponent(shipBoard, point, orientation));
        }
    }

    public void setCompletedShipBoards(Set<ShipBoard> completedShipBoards) {
        this.completedShipBoards.clear();
        this.completedShipBoards.addAll(completedShipBoards);
    }

    protected boolean componentInHand(){
        return myShip.getLastComponent() != null && myShip.getLastPosition() == null;
    }

    public ComponentBank getComponentBank(){
        return componentBank;
    }
}
