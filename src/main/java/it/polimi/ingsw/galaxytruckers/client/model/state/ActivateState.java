package it.polimi.ingsw.galaxytruckers.client.model.state;

import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.ArrayList;
import java.util.Set;
import java.util.List;

/**
 * Abstract base class for states that handle component activation in the Galaxy Truckers game.
 * This class represents the phase where players can activate their ship components and
 * use batteries. It manages the balance between activated components and batteries used.
 * <p>
 * The class is sealed to restrict subclassing to specific activation states.
 * </p>
 */
public sealed abstract class ActivateState extends AdventureState implements AdventureStateInterface permits
        DeclareFirePowerState,
        DeclareEnginePowerState,
        HandleProjectileState
{
    /** Set of positions where components can be activated */
    protected final Set<Point> availablePositions;

    /** Flag indicating whether it's the current player's turn */
    protected final boolean isMyTurn;

    /** Maximum number of components that can be activated (limited by batteries or available positions) */
    private final int maxActivate;

    /** Counter for the number of batteries that have been activated */
    private int batteriesActivated;

    /** Counter for the number of components that have been activated */
    private int componentActivated;

    /**
     * Creates a new ActivateState with the specified parameters.
     * Initializes the state with the player's ship, the currently active ship,
     * and the positions where components can be activated.
     *
     * @param myShip The ship board of the local player
     * @param currentShip The ship board that is currently active
     * @param availablePositions Set of positions where components can be activated
     */
    ActivateState(ShipBoard myShip, ShipBoard currentShip, Set<Point> availablePositions) {
        this.myShip = myShip;
        this.isMyTurn = currentShip.equals(myShip);
        this.currentShip = currentShip;
        this.availablePositions = availablePositions;
        this.maxActivate = Math.min(currentShip.getNumBatteries(), availablePositions.size());
        batteriesActivated = 0;
        componentActivated = 0;
    }

    /**
     * Gets the list of actions available in the current activation state.
     * The available actions depend on:
     * <ul>
     *   <li>Whether the player is out of the game</li>
     *   <li>Whether it's the player's turn</li>
     *   <li>The number of batteries and components that have been activated</li>
     *   <li>Whether the maximum number of activations has been reached</li>
     * </ul>
     * Possible actions include spending batteries, activating components,
     * and proceeding to the next step when batteries and components are balanced.
     *
     * @return A list of available actions in this state
     */
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

    /**
     * Gets the set of positions where components can be activated.
     * These positions represent valid locations for component activation
     * during this state.
     *
     * @return Set of points where components can be activated
     */
    public Set<Point> getAvailablePositions() {
        return availablePositions;
    }
}
