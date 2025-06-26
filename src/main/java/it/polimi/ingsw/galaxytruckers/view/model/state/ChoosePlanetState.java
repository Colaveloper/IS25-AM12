package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Represents the state where players choose a planet in the Galaxy Truckers game.
 * This state allows players to select which planets to land on to receive goods
 * at the cost of losing flight days.
 */
public final class ChoosePlanetState extends AdventureState implements GameStateInterface, AdventureStateInterface {
    /** Array of planet options represented as ship boards */
    private final ShipBoard[] options;

    /** Flag indicating whether it's the current player's turn */
    private boolean isMyTurn;

    /**
     * Creates a new ChoosePlanetState with the specified parameters.
     * Initializes the state with the player's ship, the currently active ship,
     * and the number of planets to choose from.
     *
     * @param myShip The ship board of the local player
     * @param currentShip The ship board that is currently active
     * @param numPlanets The number of planet options available
     */
    public ChoosePlanetState(ShipBoard myShip, ShipBoard currentShip, int numPlanets) {
        this.myShip = myShip;
        options = new ShipBoard[numPlanets];
        this.currentShip = currentShip;
        this.isMyTurn = myShip.equals(currentShip);
    }

    @Override
    public List<StateActions> getAvailableActions() {
        List<StateActions> actions = new ArrayList<>();
        if(isMyTurn) {
            actions.add(StateActions.CHOOSE_PLANET);
            actions.add(StateActions.GO_NEXT);
        }
        actions.addAll(super.getAvailableActions());
        return actions;
    }

    @Override
    public void notifyChoosePlanet(ShipBoard shipBoard, int choice, ShipBoard nextShipBoard) {
        isMyTurn = myShip.equals(nextShipBoard);
        if(choice != -1) options[choice] = shipBoard;
        game.getObservers().forEach(observer -> observer.notifyChoosePlanet(shipBoard,choice,nextShipBoard));
    }

    @Override
    public void notifyCurrentPlayerUpdate(ShipBoard shipBoard) {
        isMyTurn = myShip.equals(shipBoard);
        game.getObservers().forEach(observer -> observer.notifyCurrentPlayerUpdate(shipBoard));
    }

    /**
     * Checks if it's the local player's turn to choose a planet.
     *
     * @return true if it's the local player's turn, false otherwise
     */
    public boolean isMyTurn() {
        return isMyTurn;
    }

    /**
     * Gets the array of planet options available for selection.
     * Each occupied planet is represented by a shipboard in the array.
     *
     * @return Array of ship boards representing planet options
     */
    public ShipBoard[] getOptions() {
        return options;
    }

    /**
     * Sets the planet options available for selection.
     * Copies the provided options array into the internal options array.
     *
     * @param options Array of ship boards representing planet options
     */
    public void setOptions(ShipBoard[] options) {
        System.arraycopy(options, 0, this.options, 0, this.options.length);
    }
}
