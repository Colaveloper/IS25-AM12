package it.polimi.ingsw.galaxytruckers.model.state;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ComponentBank;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.Direction;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public abstract class ShipBuildingState extends GameState{
    private final ComponentBank componentBank;
    protected final Set<ShipBoard> completedShipBoards;

    private final Object coveredLock = new Object();
    private final Object uncoveredLock = new Object();

    public ShipBuildingState() {
        this.completedShipBoards = new HashSet<>();
        this.componentBank = new ComponentBank();
        this.componentBank.initialize();
    }

    @Override
    public void requestRandComponent(ShipBoard shipBoard) {
        if (getCompletedShipBoards().contains(shipBoard)) {
            throw new IllegalStateException("Ship Board already completed");
        }
        synchronized (coveredLock) {
            Component component = componentBank.drawRandComponent();
            try {
                shipBoard.offerComponent(component);
                game.getEventListener().notifyRequestFaceDownComponentEvent(shipBoard,component);
            } catch (IllegalStateException e) {
                componentBank.returnCoveredComponent(component);
                throw e;
            }
        }
    }

    @Override
    public void requestComponent(ShipBoard shipBoard, int componentId) {
        if (getCompletedShipBoards().contains(shipBoard)) {
            throw new IllegalStateException("Ship Board already completed");
        }
        synchronized (uncoveredLock) {
            Component component = componentBank.removeUncoveredComponent(componentId);
            try {
                shipBoard.offerComponent(component);
                game.getEventListener().notifyRequestFaceUpComponentEvent(shipBoard,component);
            } catch (IllegalStateException e) {
                componentBank.addToUncoveredComponents(component);
                throw e;
            }
        }
    }

    @Override
    public void rejectComponent(ShipBoard shipBoard) {
        if (getCompletedShipBoards().contains(shipBoard)) {
            throw new IllegalStateException("Ship Board already completed");
        }
        synchronized (uncoveredLock) {
            Component component = shipBoard.rejectComponent();
            if (component == null) {
                throw new IllegalStateException("You don't have any component to reject");
            }
            componentBank.addToUncoveredComponents(component);
            game.getEventListener().notifyRejectComponentEvent(shipBoard);
        }
    }

    @Override
    public void stashComponent(ShipBoard shipBoard) {
        throw new UnsupportedOperationException("This action is not available.");
    }

    @Override
    public void grabPlacedComponent(ShipBoard shipBoard) {
        if (getCompletedShipBoards().contains(shipBoard)) {
            throw new IllegalStateException("Ship Board already completed");
        }
        shipBoard.grabPlacedComponent();
        game.getEventListener().notifyGrabPlacedComponentEvent(shipBoard);
    }

    @Override
    public void grabStashedComponent(ShipBoard shipBoard, int index) {
        throw new UnsupportedOperationException("This action is not available.");
    }

    @Override
    public void placeComponent(ShipBoard shipBoard, Point point, Direction orientation) {
        if (getCompletedShipBoards().contains(shipBoard)) {
            throw new IllegalStateException("Ship Board already completed");
        }
        shipBoard.placeComponent(point, orientation);
        game.getEventListener().notifyPlaceComponentEvent(shipBoard,orientation, point);
    }

    @Override
    public void flipHourglass(ShipBoard shipBoard) {
        throw new UnsupportedOperationException("This action is not available at level " + game.getLevel());
    }

    @Override
    public void placeShipOnFlightBoard(ShipBoard shipBoard, int startingPosition) {
        throw new UnsupportedOperationException("This action is not available at level " + game.getLevel());
    }

    @Override
    public void placeShipOnFlightBoard(ShipBoard shipBoard) {
        if (getCompletedShipBoards().contains(shipBoard)) {
            throw new IllegalStateException("Ship Board already completed");
        }
        int position = game.getFlightBoard().placeShipOnFlightBoard(shipBoard);
        game.getEventListener().notifyFlightBoardUpdateEvent(shipBoard,position);
        completeShipBoard(shipBoard);
    }

    @Override
    public void acquireForecast(ShipBoard shipBoard, int deckIndex) {
        throw new UnsupportedOperationException("This action is not available at level " + game.getLevel());
    }

    @Override
    public void releaseForecast(ShipBoard shipBoard) {
        throw new UnsupportedOperationException("This action is not available at level " + game.getLevel());
    }

    protected abstract void endBuilding();

    protected void completeShipBoard(ShipBoard shipBoard) {
        synchronized (completedShipBoards) {
            completedShipBoards.add(shipBoard);
            if (completedShipBoards.size() == game.getShipBoards().size()) {
                endBuilding();
            }
        }
    }

    public ComponentBank getComponentBank() {
        return componentBank;
    }

    public Set<ShipBoard> getCompletedShipBoards() {
        synchronized (completedShipBoards) {
            return new HashSet<>(completedShipBoards);
        }
    }

    @VisibleForTesting
    protected ShipBuildingState(ComponentBank testBank) {
        this.completedShipBoards = new HashSet<>();
        this.componentBank = testBank;
        this.componentBank.initialize();
    }
}
