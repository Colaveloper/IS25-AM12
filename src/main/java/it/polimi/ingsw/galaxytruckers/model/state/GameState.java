package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.Direction;

import java.awt.*;

public abstract non-sealed class GameState implements GameStateInterface {
    protected Game game;

    protected boolean expired = false;

    /**
     * Sets the game associated with this state and initializes the state.
     * @param game the game to associate with this state
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * If allowed, lets the player with the specified ship board skip to the next state.
     * @param shipBoard the ship board of the player who wants to skip
     */
    public abstract void skip(ShipBoard shipBoard);

    /**
     * Ensures the state will not be granted to the player for the specified ship board.
     * (e.g., if the player has no actions to perform)
     * @param shipBoard
     */
    public void cancelSkip(ShipBoard shipBoard) {}

    /**
     * Activates a component at the specified position on the ship board.
     * @param shipBoard the ship board on which the component is located
     * @param position the position of the component to activate
     */
    public void activateComponent(ShipBoard shipBoard, Point position) {
        throw new IllegalStateException("This action is unsupported in this state");
    }

    /**
     * Spends batteries at the specified point on the ship board.
     * Notifies the controller about the use of a battery component.
     *
     * @param shipBoard the ship board where the battery is used
     * @param point the point on the ship board where the battery is located
     */
    public void spendBatteries(ShipBoard shipBoard, Point point) {
        throw new IllegalStateException("This action is unsupported in this state");
    }

    /**
     * Grants a reward to the specified ship board.
     * Notifies the controller about the acquisition of credits or other rewards.
     *
     * @param shipBoard the ship board acquiring the reward
     */
    public void grabReward(ShipBoard shipBoard) {
        throw new IllegalStateException("This action is unsupported in this state");
    }

    /**
     * Lets the player choose a ship piece to remove by its index.
     * @param shipBoard the ship board of the player
     * @param pieceIndex the index of the ship piece to remove
     */
    public void chooseShipPiece(ShipBoard shipBoard, int pieceIndex) {
        throw new IllegalStateException("This action is unsupported in this state");
    }

    /**
     * Draws a new adventure card for the specified ship board.
     * @param shipBoard the ship board drawing the card
     */
    public void drawCard(ShipBoard shipBoard) {
        throw new IllegalStateException("This action is unsupported in this state");
    }

    /**
     * Removes a crew member from the specified position on the ship board.
     * @param shipBoard the ship board losing the crew
     * @param position the position of the crew to remove
     */
    public void loseCrew(ShipBoard shipBoard, Point position)  {
        throw new IllegalStateException("This action is unsupported in this state");
    }

    /**
     * Removes a good from the specified position on the ship board.
     * @param shipBoard the ship board losing the good
     * @param position the position of the good to remove
     */
    public void loseGood(ShipBoard shipBoard, Point position)  {
        throw new IllegalStateException("This action is unsupported in this state");
    }

    /**
     * Adds a good to the specified position on the ship board.
     * @param shipBoard the ship board gaining the good
     * @param position the position to add the good
     * @param good the type of good to add
     */
    public void addGood(ShipBoard shipBoard, Point position, GoodsType good)  {
        throw new IllegalStateException("This action is unsupported in this state");
    }

    /**
     * Removes a good of the specified type from the given position on the ship board.
     * @param shipBoard the ship board losing the good
     * @param position the position of the good to remove
     * @param good the type of good to remove
     */
    public void removeGood(ShipBoard shipBoard, Point position, GoodsType good)  {
        throw new IllegalStateException("This action is unsupported in this state");
    }

    /**
     * Moves to the next state of the game.
     * @param shipBoard the ship board of the player that requested the action
     */
    public void goNext(ShipBoard shipBoard) {
        throw new IllegalStateException("This action is unsupported in this state");
    }

    /**
     * Lets the player choose a planet.
     * @param shipBoard the ship board of the player
     * @param option the option representing the chosen planet
     */
    public void choosePlanet(ShipBoard shipBoard, int option) {
        throw new IllegalStateException("This action is unsupported in this state");
    }

    /**
     * Requests a random component for the specified ship board.
     * @param shipBoard the ship board requesting the component
     */
    public void requestRandComponent(ShipBoard shipBoard){
        throw new IllegalStateException("This action is unsupported in this state");
    }

    /**
     * Requests a specific component by ID for the specified ship board.
     * @param shipBoard the ship board requesting the component
     * @param componentId the ID of the component to request
     */
    public void requestComponent(ShipBoard shipBoard, int componentId){
        throw new IllegalStateException("This action is unsupported in this state");
    }

    /**
     * Rejects the current component for the specified ship board.
     * @param shipBoard the ship board rejecting the component
     */
    public void rejectComponent(ShipBoard shipBoard){
        throw new IllegalStateException("This action is unsupported in this state");
    }

    /**
     * Stashes the current component for the specified ship board.
     * @param shipBoard the ship board stashing the component
     */
    public void stashComponent(ShipBoard shipBoard){
        throw new IllegalStateException("This action is unsupported in this state");
    }

    /**
     * Grabs the placed component for the specified ship board.
     * @param shipBoard the ship board grabbing the placed component
     */
    public void grabPlacedComponent(ShipBoard shipBoard) {
        throw new IllegalStateException("This action is unsupported in this state");
    }

    /**
     * Grabs a stashed component by index for the specified ship board.
     * @param shipBoard the ship board grabbing the stashed component
     * @param index the index of the stashed component
     */
    public void grabStashedComponent(ShipBoard shipBoard, int index){
        throw new IllegalStateException("This action is unsupported in this state");
    }

    /**
     * Places a component at the specified point and orientation on the ship board.
     * @param shipBoard the ship board placing the component
     * @param point the point where the component is placed
     * @param orientation the orientation of the component
     */
    public void placeComponent(ShipBoard shipBoard, Point point, Direction orientation){
        throw new IllegalStateException("This action is unsupported in this state");
    }

    /**
     * Lets the player with the specified ship board flip the hourglass.
     * @param shipBoard the ship board flipping the hourglass
     */
    public void flipHourglass(ShipBoard shipBoard){
        throw new IllegalStateException("This action is unsupported in this state");
    }

    /**
     * Places the ship on the flight board at the specified starting position.
     * @param shipBoard the ship board placing the ship
     * @param startingPosition the starting position on the flight board
     */
    public void placeShipOnFlightBoard(ShipBoard shipBoard, int startingPosition){
        throw new IllegalStateException("This action is unsupported in this state");
    }

    /**
     * Places the ship on the flight board.
     * @param shipBoard the ship board placing the ship
     */
    public void placeShipOnFlightBoard(ShipBoard shipBoard) {
        throw new IllegalStateException("This action is unsupported in this state");
    }

    /**
     * Acquires a forecast card from the specified deck for the ship board.
     * @param shipBoard the ship board acquiring the forecast
     * @param deckIndex the index of the deck to acquire the forecast from
     */
    public void acquireForecast(ShipBoard shipBoard, int deckIndex){
        throw new IllegalStateException("This action is unsupported in this state");
    }

    /**
     * Releases the current forecast for the specified ship board.
     * @param shipBoard the ship board releasing the forecast
     */
    public void releaseForecast(ShipBoard shipBoard){
        throw new IllegalStateException("This action is unsupported in this state");
    }

    /**
     * Removes a component from the specified position on the ship board.
     * @param shipBoard the ship board removing the component
     * @param point the position of the component to remove
     */
    public void removeComponent(ShipBoard shipBoard, Point point) {
        throw new IllegalStateException("This action is unsupported in this state");
    }

    /**
     * Initializes the cabin at the specified position on the ship board with the given crew type.
     * @param shipBoard the ship board initializing the cabin
     * @param point the position of the cabin
     * @param crewType the type of crew to assign to the cabin
     */
    public void initializeCabin(ShipBoard shipBoard, Point point, CrewType crewType) {
        throw new IllegalStateException("This action is unsupported in this state");
    }

    /**
     * Lets the player give up.
     * @param shipBoard the ship board of the player giving up
     */
    public void giveUp(ShipBoard shipBoard){
        throw new IllegalStateException("This action is unsupported in this state");
    }
}

