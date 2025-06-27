package it.polimi.ingsw.galaxytruckers.server.model.state;

import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.*;

/**
 * Abstract class representing a state in which the player can activate components
 * and spend batteries on their ship board.
 */
public abstract class ActivateState extends AdventureState {
    /**
     * A set containing the positions on the ship board of components
     * that can be activated.
     */
    protected final Set<Point> availablePositions;
    /**
     * The ship board of the player who is activating components.
     */
    protected final ShipBoard shipBoard;
    /**
     * The number of batteries the player has to spend. If it's negative
     * it indicates the number of components that need to be activated.
     */
    protected int batteriesToSpend;
    /**
     * The number of components that have been activated by the player.
     */
    protected int activatedComponents;

    /**
     * Constructor for ActivateState.
     *
     * @param shipBoard          the ship board of the player who is activating components
     * @param availablePositions the positions on the ship board where components can be activated
     */
    protected ActivateState(ShipBoard shipBoard, Set<Point> availablePositions) {
        this.shipBoard = shipBoard;
        this.batteriesToSpend = 0;
        this.activatedComponents = 0;
        this.availablePositions = availablePositions;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Skips this state if it's the given ship board's turn.
     * If the player has batteries to spend or components to activate
     * they will be used/activated.
     * </p>
     *
     * @param shipBoard the ship board of the player who should skip
     */
    @Override
    public synchronized void skip(ShipBoard shipBoard) {
        if (!expired && this.shipBoard.equals(shipBoard)) {
            if (batteriesToSpend > 0) {
                Iterator<Point> positions = shipBoard.getBatteries().keySet().iterator();
                while (batteriesToSpend > 0) {
                    Point currentPosition = positions.next();
                    int min = Integer.min(batteriesToSpend, shipBoard.getBatteries().get(currentPosition).getNumBatteries());
                    for (int i = 0; i < min; i++) {
                        shipBoard.useBatteries(currentPosition);
                        batteriesToSpend--;
                    }
                }
            } else if (batteriesToSpend < 0) {
                Iterator<Point> positions = getAvailablePositions().stream()
                        .filter(p -> !shipBoard.getActivatables().get(p).isActive())
                        .iterator();
                while (batteriesToSpend < 0) {
                    Point currentPosition = positions.next();
                    shipBoard.activateComponent(currentPosition);
                    batteriesToSpend++;
                }
            }
            endStateAction();
        }
    }

    /**
     * Activates a component on the ship board at the specified position.
     *
     * @param shipBoard the ship board on which the component is located
     * @param position  the position of the component to activate
     * @throws IllegalStateException if it's not the player's turn, or if
     *                               the player has not enough batteries to activate the component.
     */
    @Override
    public synchronized void activateComponent(ShipBoard shipBoard, Point position) {
        if (!this.shipBoard.equals(shipBoard)) {
            throw new IllegalStateException("It's not your turn");
        }
        checkIfExpired();
        if (batteriesToSpend >= shipBoard.getNumBatteries()) {
            throw new IllegalStateException("You don't have enough batteries");
        }
        if (availablePositions.contains(position)) {
            if (shipBoard.activateComponent(position)) {
                batteriesToSpend++;
                activatedComponents++;
            }
        }
    }

    /**
     * Spends a battery on the ship board at the specified point.
     *
     * @param shipBoard the ship board where the battery is used
     * @param point     the point on the ship board where the battery is located
     * @throws IllegalStateException if it's not the player's turn, or if
     *                               the player has not enough activatable components to spend the battery on.
     */
    @Override
    public synchronized void spendBatteries(ShipBoard shipBoard, Point point) {
        if (!this.shipBoard.equals(shipBoard)) {
            throw new IllegalStateException("It's not your turn");
        }
        checkIfExpired();
        if (batteriesToSpend <= 0 && activatedComponents - batteriesToSpend >= availablePositions.size()) {
            throw new IllegalStateException("You don't have components to spend that battery on");
        }
        shipBoard.useBatteries(point);
        batteriesToSpend--;
    }

    /**
     * {@inheritDoc}
     *
     * @param shipBoard the ship board of the player that requested the action
     * @throws IllegalStateException if it's not the player's turn, or if the player
     *                               has batteries to spend or components to activate.
     */
    @Override
    public synchronized void goNext(ShipBoard shipBoard) {
        if (!this.shipBoard.equals(shipBoard)) {
            throw new IllegalStateException("It's not your turn");
        }
        checkIfExpired();
        if (batteriesToSpend > 0) {
            throw new IllegalStateException("You still have batteries to spend");
        } else if (batteriesToSpend < 0) {
            throw new IllegalStateException("You still have to activate components");
        }
        endStateAction();
    }

    /**
     * Ends the state action by transitioning to the next state.
     */
    protected void endStateAction() {
        getNextState();
    }

    /**
     * @return a set containing the positions on the ship board of
     * components that can be activated.
     */
    public synchronized Set<Point> getAvailablePositions() {
        return availablePositions;
    }

    /**
     * @return the ship board of the player who is activating components.
     */
    public synchronized ShipBoard getShipBoard() {
        return shipBoard;
    }
}
