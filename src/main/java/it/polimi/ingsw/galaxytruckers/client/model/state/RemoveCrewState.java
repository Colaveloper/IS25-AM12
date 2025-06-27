package it.polimi.ingsw.galaxytruckers.client.model.state;

import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the state where players must remove crew members in the Galaxy Truckers game.
 * This state is triggered when a ship encounters a situation that requires crew sacrifice,
 * such as the slavers card. The player must choose which crew members to remove from their ship.
 */
public final class RemoveCrewState extends AdventureState implements GameStateInterface, AdventureStateInterface {
    /** The number of crew members that must be sacrificed in this state */
    int crewSacrifice;

    /**
     * Creates a new RemoveCrewState with the specified parameters.
     * Initializes the state with the player's ship, the number of crew members to sacrifice,
     * and the currently active ship.
     *
     * @param myShip The ship board of the local player
     * @param crewSacrifice The number of crew members that must be sacrificed
     * @param currentShip The ship board that is currently active
     */
    public RemoveCrewState(ShipBoard myShip, int crewSacrifice, ShipBoard currentShip) {
        this.myShip = myShip;
        this.crewSacrifice = crewSacrifice;
        this.currentShip = currentShip;
    }

    @Override
    public List<StateActions> getAvailableActions() {
        List<StateActions> actions = new ArrayList<>();
        if(currentShip.equals(myShip)) actions.add(StateActions.LOSE_CREW);
        actions.addAll(super.getAvailableActions());
        return actions;
    }

    @Override
    public void notifyLoseCrew(ShipBoard shipBoard, Point point) {
        shipBoard.loseCrew(point);
        game.getObservers().forEach(observer -> observer.notifyLoseCrew(shipBoard, point));
    }
}
