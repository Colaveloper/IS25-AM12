package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the state where players draw adventure cards in the Galaxy Truckers game.
 * This state allows the lead player to draw a card that determines the next encounter.
 * Only the leader of the current flight group can draw a card.
 */
public final class DrawCardState extends AdventureState {

    /** Flag indicating whether the local player is the leader of the flight group */
    private final boolean imLeader;

    /** Flag indicating whether a card has been drawn in the current state */
    private boolean hasDrawn = false;

    /**
     * Creates a new DrawCardState with the specified parameters.
     * Initializes the state with the player's ship and the currently active ship.
     * Determines if the local player is the leader based on whether their ship is the current ship.
     *
     * @param myShip The ship board of the local player
     * @param currentShip The ship board that is currently active (the leader)
     */
    public DrawCardState(ShipBoard myShip, ShipBoard currentShip) {
        this.currentShip = currentShip;
        this.myShip = myShip;
        this.imLeader = currentShip.equals(myShip);
    }

    @Override
    public List<StateActions> getAvailableActions() {
        List<StateActions> actions = new ArrayList<>();
        if(imLeader) {
            if (hasDrawn) actions.add(StateActions.GO_NEXT);
            else actions.add(StateActions.DRAW_CARD);
        }
        actions.addAll(super.getAvailableActions());
        return actions;
    }

    @Override
    public void notifyDrawCard(AdventureCard adventureCard) {
        game.setCurrentCard(adventureCard);
        hasDrawn = true;
        game.getObservers().forEach(observer -> observer.notifyDrawCard(adventureCard));
    }

    /**
     * Sets whether a card has been drawn in the current state.
     * This can be used to update the state externally, for example,
     * when synchronizing with server state.
     *
     * @param hasDrawn true if a card has been drawn, false otherwise
     */
    public void setHasDrawn(boolean hasDrawn) {
        this.hasDrawn = hasDrawn;
    }

    /**
     * Checks whether a card has been drawn in the current state.
     *
     * @return true if a card has been drawn, false otherwise
     */
    public boolean hasDrawn() {
        return hasDrawn;
    }
}
