package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import java.awt.*;

/**
 * Abstract base class that defines the interface for handling user interactions in the GUI.
 * This controller acts as an intermediary between GUI elements and game logic,
 * providing methods for various user actions during different phases of the game.
 * Concrete implementations will handle phase-specific behaviors.
 */
public abstract class GuiController {

    /**
     * Handles a user click or tap at a specific point on the shipboard.
     * This method is typically used for selecting components, tiles, or positions.
     *
     * @param currentPoint The coordinates where the user clicked
     */
    public void handlePointPress(Point currentPoint) {};

    /**
     * Advances to the next step or phase in the current game flow.
     * This method is typically triggered by "next" or "continue" buttons.
     */
    public void goNext() {}

    /**
     * Places the player's ship at a specific position on the flight board.
     *
     * @param position The position on the flight board where the ship should be placed
     */
    public void placeShipOnFlightBoard(int position) {};

    /**
     * Requests a random component from the available component pool.
     * Typically used during the ship building phase.
     */
    public void requestRandComponent() {};

    /**
     * Rejects the currently selected component, returning it to the rejected pile.
     * Used when a player decides not to use a component during ship building.
     */
    public void rejectComponent() {};

    /**
     * Requests a specific component by its ID.
     * Used for requesting a particular component type during ship building.
     *
     * @param id The unique identifier of the requested component
     */
    public void requestComponent(int id) {};

    /**
     * Retrieves a component from the player's stash at the specified index.
     * Used when a player wants to use a previously stashed component.
     *
     * @param i The index of the component in the player's stash
     */
    public void grabStashedComponent(int i) {};

    /**
     * Stashes the currently held component for later use.
     * Allows players to save components during ship building.
     */
    public void stashComponent() {};

    /**
     * Rotates the component currently in the player's hand.
     * Used during ship building to orient components before placement.
     */
    public void rotateHandComponent() {};

    /**
     * Acquires forecast information about upcoming adventure cards.
     * Allow peeking at future cards.
     *
     * @param finalI The index of the forecast to acquire
     */
    public void acquireForecast(int finalI) {}

    /**
     * Releases the current forecast deck in hand, allowing other players to access it.
     */
    public void releaseForecast() {}

    /**
     * Draws a new adventure card from the deck.
     * Used during the adventure phase to reveal the next challenge.
     */
    public void drawCard() {}

    /**
     * Selects a planet to visit during the adventure phase.
     * Used when a planet card is drawn and the player must choose which planet to land on.
     *
     * @param i The index of the chosen planet
     */
    public void choosePlanet(int i) {}
}
