package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.view.model.Game;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.List;

/**
 * Abstract base class for all adventure-phase states in the Galaxy Truckers game.
 * This class represents the phase where players navigate through space and encounter
 * various adventure cards. It provides common functionality for all adventure substates.
 * <p>
 * The class is sealed to restrict subclassing to the predefined set of adventure states.
 * </p>
 */
public sealed abstract class AdventureState extends GameState permits
                                                              ActivateState,
                                                              AddGoodsState,
                                                              ChoosePlanetState,
                                                              ChooseShipPieceState,
                                                              DrawCardState,
                                                              GrabRewardState,
                                                              RemoveCrewState,
                                                              RemoveGoodsState {

    /** Flag indicating whether this player is out of the adventure phase */
    protected boolean imOut;

    /** The current ship board that is active in this adventure state */
    protected ShipBoard currentShip;

    /**
     * Gets whether the player is out of the adventure phase.
     * A player may be out if their ship has given up or been destroyed.
     *
     * @return true if the player is out, false otherwise
     */
    public boolean getImOut() {
        return imOut;
    }

    /**
     * Gets the current adventure card being resolved.
     * This represents the active event that players are responding to.
     *
     * @return The current adventure card
     */
    public AdventureCard getCurrentCard() {
        return game.getCurrentCard();
    }

    @Override
    public List<StateActions> getAvailableActions() {
        if (!imOut) return List.of(StateActions.GIVE_UP);
        else return List.of();
    }

    @Override
    public void setGame(Game game) {
        super.setGame(game);
        imOut = game.getGivenUpShips().contains(myShip);
    }

    @Override
    public void notifyLoseCrew(ShipBoard shipBoard, Point point) {
        shipBoard.loseCrew(point);
        game.getObservers().forEach(observer -> observer.notifyLoseCrew(shipBoard, point));
    }

    /**
     * Gets the current ship board that is active in this adventure state.
     * This represents the ship that is currently taking its turn or being affected.
     *
     * @return The current active ship board
     */
    public ShipBoard getShipBoard() {
        return currentShip;
    }
}
