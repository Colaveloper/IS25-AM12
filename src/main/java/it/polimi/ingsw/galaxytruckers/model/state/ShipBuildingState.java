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
    protected final Set<ShipBoard> completedShipBoards = new HashSet<>();
    protected final Set<ShipBoard> pendingShipBoards = new HashSet<>();

    private final Object coveredLock = new Object();
    private final Object uncoveredLock = new Object();

    private final Object endLock = new Object();

    public ShipBuildingState() {
        this.componentBank = new ComponentBank();
        this.componentBank.initialize();
    }

    @Override
    public void skip(ShipBoard shipBoard) {
        synchronized (endLock) {
            pendingShipBoards.add(shipBoard);
            tryToEnd();
        }
    }

    @Override
    public void cancelSkip(ShipBoard shipBoard) {
        removePending(shipBoard);
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
        game.getFlightBoard().placeShipOnFlightBoard(shipBoard);
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
        synchronized (endLock) {
            completedShipBoards.add(shipBoard);
            tryToEnd();
        }
    }

    private void tryToEnd() {
        Set<ShipBoard> missingShips = new HashSet<>(game.getShipBoards());
        missingShips.removeAll(completedShipBoards);
        missingShips.removeAll(pendingShipBoards);
        if (missingShips.isEmpty()) {
            endBuilding();
        }
    }

    private void removePending(ShipBoard shipBoard) {
        synchronized (endLock) {
            pendingShipBoards.remove(shipBoard);
        }
    }

    public ComponentBank getComponentBank() {
        return componentBank;
    }

    public Set<ShipBoard> getCompletedShipBoards() {
        synchronized (endLock) {
            return new HashSet<>(completedShipBoards);
        }
    }

    @VisibleForTesting
    protected ShipBuildingState(ComponentBank testBank) {
        this.componentBank = testBank;
        this.componentBank.initialize();
    }
}
