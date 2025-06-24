package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.Player;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.Game;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.List;
import java.util.Set;

/**
 * Abstract base class for all game states in the Galaxy Truckers game.
 * This class implements the State pattern to model different phases of gameplay.
 * Each concrete state provides specific behavior for the actions that are valid
 * during that phase of the game, while default implementations produce error messages.
 * <p>
 * The class is sealed to restrict subclassing to the predefined set of game states:
 * AdventureState, ShipBuildingState, ShipCorrectionState, and ShipInitializationState.
 * </p>
 */
public abstract sealed class GameState permits
        AdventureState,
        ShipBuildingState,
        ShipCorrectionState,
        ShipInitializationState
{
    /** The game instance associated with this state */
    protected Game game;

    /** The client model that this state can modify */
    protected ClientModel clientModel;

    /** The ship board associated with the local player */
    protected ShipBoard myShip;

    /**
     * Gets the game instance associated with this state.
     *
     * @return The game instance
     */
    public Game getGame() {
        return game;
    }

    /**
     * Sets the ship board for the local player.
     *
     * @param shipBoard The local player's ship board
     */
    public void setMyShip(ShipBoard shipBoard) {
        this.myShip = shipBoard;
    }

    /**
     * Sets the game instance for this state.
     * This method is called when transitioning to this state.
     *
     * @param game The game instance to associate with this state
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * Gets the list of available actions in the current state.
     * Concrete states must implement this to define which actions are allowed.
     *
     * @return A list of available actions in this state
     */
    public abstract List<StateActions> getAvailableActions();

    /**
     * Performs cleanup when leaving this state.
     * By default, deactivates all components on all ship boards.
     */
    public void leave() {
        game.getShipBoards().forEach(ShipBoard::deactivateAll);
    }

    /**
     * Notifies that a random component has been requested.
     * Default implementation signals this action is not permitted in this state.
     *
     * @param shipBoard The ship board requesting the component
     * @param component The randomly selected component
     */
    public void notifyRequestRandComponent(ShipBoard shipBoard, Component component) {
        System.err.println("1This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    /**
     * Notifies that a specific component has been requested.
     * Default implementation signals this action is not permitted in this state.
     *
     * @param shipBoard The ship board requesting the component
     * @param componentId The requested component
     */
    public void notifyRequestComponent(ShipBoard shipBoard, Component componentId) {
        System.err.println("2This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    /**
     * Notifies that a component has been rejected.
     * Default implementation signals this action is not permitted in this state.
     *
     * @param shipBoard The ship board where the component was rejected
     */
    public void notifyRejectComponent(ShipBoard shipBoard) {
        System.err.println("3This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    /**
     * Notifies that a component has been stashed.
     * Default implementation signals this action is not permitted in this state.
     *
     * @param shipBoard The ship board where the component was stashed
     */
    public void notifyStashComponent(ShipBoard shipBoard) {
        System.err.println("4This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    /**
     * Notifies that a placed component has been grabbed.
     * Default implementation signals this action is not permitted in this state.
     *
     * @param shipBoard The ship board where the component was grabbed
     */
    public void notifyGrabPlacedComponent(ShipBoard shipBoard) {
        System.err.println("3.5This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    /**
     * Notifies that a stashed component has been grabbed.
     * Default implementation signals this action is not permitted in this state.
     *
     * @param shipBoard The ship board where the component was grabbed
     * @param index The index of the stashed component
     */
    public void notifyGrabStashedComponent(ShipBoard shipBoard, int index) {
        System.err.println("5This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    /**
     * Notifies that a component has been placed on the ship.
     * Default implementation signals this action is not permitted in this state.
     *
     * @param shipBoard The ship board where the component was placed
     * @param point The coordinates where the component was placed
     * @param orientation The orientation of the placed component
     */
    public void notifyPlaceComponent(ShipBoard shipBoard, Point point, Direction orientation) {
        System.err.println("6This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    /**
     * Notifies that the hourglass has been flipped.
     * Default implementation signals this action is not permitted in this state.
     *
     * @param shipBoard The ship board where the hourglass was flipped
     */
    public void notifyFlipHourglass(ShipBoard shipBoard) {
        System.err.println("7This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    /**
     * Notifies that the hourglass timer has ended.
     * Default implementation signals this action is not permitted in this state.
     */
    public void notifyHourglassEnd() {
        System.err.println("8This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    /**
     * Notifies that ships have surrendered and should be removed from the flight board.
     * This method is properly implemented in the base class to work in all states.
     *
     * @param shipBoards The set of ship boards that have surrendered
     */
    public void notifySurrenderShip(Set<ShipBoard> shipBoards) {
        for (ShipBoard shipBoard : shipBoards) game.getFlightBoard().removeShip(shipBoard);
        //doesn't notify observers so it doesnt print anything, but there's an event for that
    }

    /**
     * Notifies that a player has requested to surrender.
     * This method is properly implemented in the base class to work in all states.
     *
     * @param player The player who requested to surrender
     */
    public void notifySurrenderRequest(Player player) {
        game.getObservers().forEach(observer -> observer.notifyGiveUp(player));
    }

    /**
     * Updates a ship's position on the flight board.
     * This method is properly implemented in the base class to work in all states.
     *
     * @param shipBoard The ship board to update
     * @param position The new position on the flight board
     * @param isMyShip Whether this is the local player's ship
     */
    public void notifyFlightBoardPosition(ShipBoard shipBoard, int position, boolean isMyShip) {
        game.getFlightBoard().setShipPosition(shipBoard, position);
    }

    /**
     * Notifies that a forecast card has been peeked at.
     * Default implementation signals this action is not permitted in this state.
     *
     * @param shipBoard The ship board where the forecast was peeked
     * @param deckIndex The index of the deck being peeked at
     */
    public void notifyPeekForecast(ShipBoard shipBoard, int deckIndex) {
        System.err.println("9This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    /**
     * Sets the forecast deck with adventure cards.
     * Default implementation signals this action is not permitted in this state.
     *
     * @param adventureCards The list of adventure cards for the forecast deck
     */
    public void setForecastDeck(List<AdventureCard> adventureCards) {
        System.err.println("10This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    /**
     * Notifies that a forecast has been released.
     * Default implementation signals this action is not permitted in this state.
     *
     * @param shipBoard The ship board where the forecast was released
     * @param isMyShip Whether this is the local player's ship
     */
    public void notifyReleaseForecast(ShipBoard shipBoard, boolean isMyShip) {
        System.err.println("11This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    /**
     * Notifies that a component has been removed from the ship.
     * Default implementation signals this action is not permitted in this state.
     *
     * @param shipBoard The ship board where the component was removed
     * @param point The coordinates of the removed component
     */
    public void notifyRemoveComponent(ShipBoard shipBoard, Point point) {
        System.err.println("12This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    /**
     * Notifies that a ship piece has been chosen.
     * Default implementation signals this action is not permitted in this state.
     *
     * @param shipBoard The ship board where the piece was chosen
     * @param pieceIndex The index of the chosen piece
     */
    public void notifyChooseShipPiece(ShipBoard shipBoard, int pieceIndex) {
        System.err.println("13This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    /**
     * Notifies that a ship is not connected.
     * Default implementation signals this action is not permitted in this state.
     *
     * @param shipBoard The ship board that is not connected
     * @param shipPieces The disconnected ship pieces
     */
    public void notifyShipNotConnected(ShipBoard shipBoard, List<Set<Point>> shipPieces) {
        System.err.println("14This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    /**
     * Notifies that a ship has been validated.
     * Default implementation signals this action is not permitted in this state.
     *
     * @param shipBoard The ship board that was validated
     */
    public void notifyShipValidated(ShipBoard shipBoard) {
        System.err.println("15This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    /**
     * Notifies that a cabin has been initialized with crew.
     * Default implementation signals this action is not permitted in this state.
     *
     * @param shipBoard The ship board where the cabin was initialized
     * @param point The coordinates of the initialized cabin
     * @param crewType The type of crew assigned to the cabin
     */
    public void notifyInitializeCabin(ShipBoard shipBoard, Point point, CrewType crewType) {
        System.err.println("16This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    /**
     * Notifies that an adventure card has been drawn.
     * Default implementation signals this action is not permitted in this state.
     *
     * @param adventureCard The drawn adventure card
     */
    public void notifyDrawCard(AdventureCard adventureCard) {
        System.err.println("17This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    /**
     * Notifies that a component has been activated.
     * Default implementation signals this action is not permitted in this state.
     *
     * @param shipBoard The ship board where the component was activated
     * @param point The coordinates of the activated component
     */
    public void notifyActivateComponent(ShipBoard shipBoard, Point point) {
        System.err.println("18This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    /**
     * Notifies that crew has been lost from a cabin.
     * Default implementation signals this action is not permitted in this state.
     *
     * @param shipBoard The ship board where the crew was lost
     * @param point The coordinates of the cabin where crew was lost
     */
    public void notifyLoseCrew(ShipBoard shipBoard, Point point) {
        System.err.println("19This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    /**
     * Notifies that a reward has been grabbed.
     * Default implementation signals this action is not permitted in this state.
     *
     * @param shipBoard The ship board that grabbed the reward
     * @param credits The amount of credits grabbed
     */
    public void notifyGrabReward(ShipBoard shipBoard, int credits) {
        System.err.println("20This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    /**
     * Notifies that goods have been placed on the ship.
     * Default implementation signals this action is not permitted in this state.
     *
     * @param shipBoard The ship board where goods were placed
     * @param point The coordinates where goods were placed
     * @param goodsType The type of goods placed
     */
    public void notifyPlaceGoods(ShipBoard shipBoard, Point point, GoodsType goodsType) {
        System.err.println("21This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    /**
     * Notifies that goods have been removed from the ship.
     * Default implementation signals this action is not permitted in this state.
     *
     * @param shipBoard The ship board where goods were removed
     * @param point The coordinates where goods were removed
     * @param goodsType The type of goods removed
     */
    public void notifyRemoveGoods(ShipBoard shipBoard, Point point, GoodsType goodsType) {
        System.err.println("22This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    /**
     * Notifies that a battery has been used.
     * Default implementation signals this action is not permitted in this state.
     *
     * @param shipBoard The ship board where the battery was used
     * @param point The coordinates of the used battery
     */
    public void notifyUseBattery(ShipBoard shipBoard, Point point) {
        System.err.println("23This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    /**
     * Notifies that a planet has been chosen.
     * Default implementation signals this action is not permitted in this state.
     *
     * @param shipBoard The ship board choosing the planet
     * @param choice The index of the chosen planet
     * @param nextShipboard The ship board after the choice
     */
    public void notifyChoosePlanet(ShipBoard shipBoard, int choice, ShipBoard nextShipboard) {
        System.err.println("24This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    /**
     * Notifies that the current player has been updated.
     * Default implementation signals this action is not permitted in this state.
     *
     * @param shipBoard The ship board of the current player
     */
    public void notifyCurrentPlayerUpdate(ShipBoard shipBoard) {
        System.err.println("25This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    /**
     * Notifies that a ship has given up.
     *
     * @param shipBoard The ship board that has given up
     */
    public void notifyGiveUp(ShipBoard shipBoard) {
        System.err.println(shipBoard.getColor() + " is giving up");
    }
}