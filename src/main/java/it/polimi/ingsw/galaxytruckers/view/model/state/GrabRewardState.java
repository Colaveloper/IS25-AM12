package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the state where players can grab rewards in the Galaxy Truckers game.
 * This state allows the current player to choose whether to take a reward (credits)
 * or skip it. Players can only interact with rewards during their turn.
 */
public final class GrabRewardState extends AdventureState implements GameStateInterface, AdventureStateInterface {

    /**
     * Creates a new GrabRewardState with the specified parameters.
     * Initializes the state with the player's ship and the currently active ship.
     *
     * @param myShip The ship board of the local player
     * @param currentShip The ship board that is currently active
     */
    public GrabRewardState(ShipBoard myShip, ShipBoard currentShip) {
        this.myShip = myShip;
        this.currentShip = currentShip;
    }

    @Override
    public List<StateActions> getAvailableActions() {
        List<StateActions> actions = new ArrayList<>();
        if(currentShip.equals(myShip)) {
            actions.add(StateActions.GRAB_REWARD);
            actions.add(StateActions.GO_NEXT);
        }
        actions.addAll(super.getAvailableActions());
        return actions;
    }

    /**
     * Updates the credits for a ship board when a reward is grabbed.
     * Sets the specified amount of credits for the current ship.
     * This method is called when a player decides to take a reward.
     *
     * @param shipBoard The ship board that is grabbing the reward
     * @param credits The amount of credits to add as a reward
     */
    public void notifyGrabReward(ShipBoard shipBoard, int credits){
        currentShip.setCredits(credits);
    }
}
