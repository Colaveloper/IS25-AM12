package it.polimi.ingsw.galaxytruckers.client.model.state;

import it.polimi.ingsw.galaxytruckers.shared.enums.Direction;
import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.ComponentBank;
import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Abstract base class for ship building states in the Galaxy Truckers game.
 * This class represents the phase where players build their ships by selecting,
 * placing, and arranging components. It manages the component bank and tracks
 * the status of ship construction.
 * <p>
 * The class is sealed to restrict subclassing to specific ship building states.
 * </p>
 */
public sealed abstract class ShipBuildingState extends GameState permits
        SecondShipBuildingState,
        TestShipBuildingState
{
    /** The bank of components available for selection by players */
    private final ComponentBank componentBank;

    /** Set of ship boards that have completed the building phase */
    protected final Set<ShipBoard> completedShipBoards;

    /** Flag indicating whether the player has stashed a component in this turn */
    protected boolean hasStashed = false;

    /** Flag indicating whether the player has finished ship building */
    protected boolean hasFinished = false;

    /**
     * Creates a new ShipBuildingState.
     * Initializes the component bank and an empty set of completed ship boards.
     */
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

    /**
     * Updates the set of ship boards that have completed the building phase.
     * This method clears the existing set and replaces it with the provided set.
     *
     * @param completedShipBoards The set of ship boards that have completed building
     */
    public void setCompletedShipBoards(Set<ShipBoard> completedShipBoards) {
        this.completedShipBoards.clear();
        this.completedShipBoards.addAll(completedShipBoards);
    }

    /**
     * Checks whether the player currently has a component in hand (not placed).
     * A component is considered "in hand" if it has been selected but not yet placed
     * on the ship board or stashed.
     *
     * @return true if the player has a component in hand, false otherwise
     */
    public boolean componentInHand(){
        return myShip.getLastComponent() != null && myShip.getLastPosition() == null;
    }

    /**
     * Gets the component bank that contains available components for selection.
     * The component bank manages both covered (random) and uncovered (visible) components.
     *
     * @return The component bank for this building phase
     */
    public ComponentBank getComponentBank(){
        return componentBank;
    }
}
