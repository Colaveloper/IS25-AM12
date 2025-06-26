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

public abstract class ShipBuildingState extends GameState {
    private final ComponentBank componentBank;
    protected final Set<ShipBoard> completedShipBoards = new HashSet<>();
    protected final Set<ShipBoard> pendingShipBoards = new HashSet<>();

    private final Object coveredLock = new Object();
    private final Object uncoveredLock = new Object();

    protected final Object endLock = new Object();

    public ShipBuildingState() {
        this.componentBank = new ComponentBank();
        this.componentBank.initialize();
    }

    /**
     * Signals to the state that the player is inactive, so that the building
     * phase ends if all other ships are completed.
     *
     * @param shipBoard the ship board of the player who wants to skip
     */
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

    /**
     * Requests a face down component from the component bank and
     * offers it to the specified ship board (by calling {@link ShipBoard#offerComponent(Component)}).
     *
     * @param shipBoard the ship board requesting the component
     * @throws IllegalStateException if the ship board is already completed
     */
    @Override
    public void requestRandComponent(ShipBoard shipBoard) {
        if (getCompletedShipBoards().contains(shipBoard)) {
            throw new IllegalStateException("Ship Board already completed");
        }
        synchronized (coveredLock) {
            Component component = componentBank.drawRandComponent();
            try {
                shipBoard.offerComponent(component);
                game.getEventListener().notifyRequestFaceDownComponentEvent(shipBoard, component);
            } catch (IllegalStateException e) {
                componentBank.returnCoveredComponent(component);
                throw e;
            }
        }
    }

    /**
     * Requests a face up component from the component bank and offers
     * it to the ship board (by calling {@link ShipBoard#offerComponent(Component)}).
     *
     * @param shipBoard   the ship board requesting the component
     * @param componentId the ID of the component to request
     * @throws IllegalStateException if the ship board is already completed
     */
    @Override
    public void requestComponent(ShipBoard shipBoard, int componentId) {
        if (getCompletedShipBoards().contains(shipBoard)) {
            throw new IllegalStateException("Ship Board already completed");
        }
        synchronized (uncoveredLock) {
            Component component = componentBank.removeUncoveredComponent(componentId);
            try {
                shipBoard.offerComponent(component);
                game.getEventListener().notifyRequestFaceUpComponentEvent(shipBoard, component);
            } catch (IllegalStateException e) {
                componentBank.addToUncoveredComponents(component);
                throw e;
            }
        }
    }

    /**
     * Returns the in hand component of the specified ship board to
     * the component bank as an uncovered component (by calling {@link ShipBoard#rejectComponent()} and
     * {@link ComponentBank#addToUncoveredComponents(Component)}).
     *
     * @param shipBoard the ship board rejecting the component
     */
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

    /**
     * Stashes the in hand component of the specified ship board by calling
     * {@link ShipBoard#stashComponent()}, if the actions is implemented in the
     * specific state class.
     *
     * @param shipBoard the ship board stashing the component
     * @throws UnsupportedOperationException if the action is not available in this state
     */
    @Override
    public void stashComponent(ShipBoard shipBoard) {
        throw new UnsupportedOperationException("This action is not available.");
    }

    /**
     * Grabs the placed component of the specified ship board by calling
     * {@link ShipBoard#grabPlacedComponent()}.
     *
     * @param shipBoard the ship board grabbing the placed component
     * @throws IllegalStateException if the ship board is already completed
     */
    @Override
    public void grabPlacedComponent(ShipBoard shipBoard) {
        if (getCompletedShipBoards().contains(shipBoard)) {
            throw new IllegalStateException("Ship Board already completed");
        }
        shipBoard.grabPlacedComponent();
    }

    /**
     * Grabs a stashed component from the specified ship board by calling
     * {@link ShipBoard#grabStashedComponent(int)}, if the actions is implemented in the
     * specific state class.
     *
     * @param shipBoard the ship board grabbing the stashed component
     * @param index     the index of the stashed component
     * @throws UnsupportedOperationException if the action is not available in this state
     */
    @Override
    public void grabStashedComponent(ShipBoard shipBoard, int index) {
        throw new UnsupportedOperationException("This action is not available.");
    }

    /**
     * Places a component on the specified ship board at the given point and orientation
     * by calling {@link ShipBoard#placeComponent(Point, Direction)}.
     *
     * @param shipBoard   the ship board where the component is placed
     * @param point       the point where the component is placed
     * @param orientation the orientation of the component
     * @throws IllegalStateException if the ship board is already completed
     */
    @Override
    public void placeComponent(ShipBoard shipBoard, Point point, Direction orientation) {
        if (getCompletedShipBoards().contains(shipBoard)) {
            throw new IllegalStateException("Ship Board already completed");
        }
        shipBoard.placeComponent(point, orientation);
    }

    /**
     * Flips the hourglass if the action is implemented in the specific state class.
     *
     * @param shipBoard the ship board flipping the hourglass
     * @throws UnsupportedOperationException if the action is not available in this state
     */
    @Override
    public void flipHourglass(ShipBoard shipBoard) {
        throw new UnsupportedOperationException("This action is not available at level " + game.getLevel());
    }

    /**
     * Places a ship board on the flight board at the specified starting position
     * by calling {@link FlightBoard#placeShipOnFlightBoard(ShipBoard, int)}.
     * If the ship board is already completed, it throws an IllegalStateException.
     *
     * @param shipBoard        the ship board to place
     * @param startingPosition the starting position on the flight board
     * @throws UnsupportedOperationException if the action is not available in this state
     */
    @Override
    public void placeShipOnFlightBoard(ShipBoard shipBoard, int startingPosition) {
        throw new UnsupportedOperationException("This action is not available at level " + game.getLevel());
    }

    /**
     * Places a ship board on the flight board in the first available position by
     * calling {@link FlightBoard#placeShipOnFlightBoard(ShipBoard)}.
     *
     * @param shipBoard the ship board placing the ship
     * @throws IllegalStateException if the ship board is already completed
     */
    @Override
    public void placeShipOnFlightBoard(ShipBoard shipBoard) {
        if (getCompletedShipBoards().contains(shipBoard)) {
            throw new IllegalStateException("Ship Board already completed");
        }
        game.getFlightBoard().placeShipOnFlightBoard(shipBoard);
        completeShipBoard(shipBoard);
    }

    /**
     * Acquires a forecast for the specified ship board at the given deck index.
     *
     * @param shipBoard the ship board acquiring the forecast
     * @param deckIndex the index of the deck to acquire the forecast from
     * @throws UnsupportedOperationException if the action is not available in this state
     */
    @Override
    public void acquireForecast(ShipBoard shipBoard, int deckIndex) {
        throw new UnsupportedOperationException("This action is not available at level " + game.getLevel());
    }

    /**
     * Releases the forecast for the specified ship board.
     *
     * @param shipBoard the ship board releasing the forecast
     * @throws UnsupportedOperationException if the action is not available in this state
     */
    @Override
    public void releaseForecast(ShipBoard shipBoard) {
        throw new UnsupportedOperationException("This action is not available at level " + game.getLevel());
    }

    /**
     * Ends the ship building phase by calling the abstract method.
     * Moves on to {@link ShipCorrectionState}
     */
    protected abstract void endBuilding();

    /**
     * Flags the ship board as completed and tries to end the game
     *
     * @param shipBoard the ship board that has been completed
     */
    protected void completeShipBoard(ShipBoard shipBoard) {
        synchronized (endLock) {
            completedShipBoards.add(shipBoard);
            tryToEnd();
        }
    }

    /**
     * Checks if all ship boards are completed or have skipped and if so,
     * ends the game by calling {@link #endBuilding()}.
     */
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

    /**
     * @return the component bank used in the ship building phase
     */
    public ComponentBank getComponentBank() {
        return componentBank;
    }

    /**
     * @return a set of completed ship boards
     */
    public Set<ShipBoard> getCompletedShipBoards() {
        Set<ShipBoard> res;
        synchronized (endLock) {
            res = new HashSet<>(completedShipBoards);
        }
        return res;
    }

    /**
     * Custom constructor for testing purposes.
     *
     * @param testBank the component bank to use in the test
     */
    @VisibleForTesting
    protected ShipBuildingState(ComponentBank testBank) {
        this.componentBank = testBank;
        this.componentBank.initialize();
    }

    /**
     * To be used for testing purposes.
     *
     * @return set of pending ship boards
     */
    @VisibleForTesting
    public Set<ShipBoard> getPendingShipBoards() {
        Set<ShipBoard> res;
        synchronized (endLock) {
            res = new HashSet<>(pendingShipBoards);
        }
        return res;
    }
}
