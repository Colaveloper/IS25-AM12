package it.polimi.ingsw.galaxytruckers.serverController.lobby;

import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.Direction;

import java.awt.*;

/**
 * Interface for lobbies in the Galaxy Truckers game.
 * Provides methods for interacting with the game model during different phases of gameplay,
 * including ship building, ship validity checking, ship initialization, and adventure phases.
 * Each method delegates to corresponding methods in the underlying game model.
 */
public interface LobbyInterface {

    //region Shipbuilding

    /**
     * Calls {@link it.polimi.ingsw.galaxytruckers.model.GameInterface#requestRandComponent(ShipBoard)}
     * on the lobby game and the player's shipBoard
     *
     * @param player the player wishing to perform the action
     */
    void requestRandComponent(Player player);

    /**
     * Calls {@link it.polimi.ingsw.galaxytruckers.model.GameInterface#requestComponent(ShipBoard, int)}
     * on the lobby game, the player's shipboard and the given id
     *
     * @param player      the nickname of the player wishing to perform the action
     * @param componentID the id of the component the player wishes to take
     */
    void requestComponent(Player player, int componentID);

    /**
     * Rejects the currently selected component, returning it to the available components pool.
     *
     * @param player the player wishing to reject the component
     */
    void rejectComponent(Player player);

    /**
     * Stashes the currently selected component into the player's temporary storage.
     *
     * @param player the player wishing to stash the component
     */
    void stashComponent(Player player);

    /**
     * Grabs a component that was previously placed on the player's ship.
     *
     * @param player the player wishing to grab a placed component
     */
    void grabPlacedComponent(Player player);

    /**
     * Retrieves a component from the player's stash.
     *
     * @param player the player wishing to grab a stashed component
     * @param index the index of the component in the player's stash
     */
    void grabStashedComponent(Player player, int index);

    /**
     * Places the currently selected component on the player's ship.
     *
     * @param player the player wishing to place the component
     * @param point the coordinates where the component should be placed
     * @param orientation the direction the component should face
     */
    void placeComponent(Player player, Point point, Direction orientation);

    /**
     * Signals that the player wants to flip the hourglass during the ship building phase.
     *
     * @param player the player flipping the hourglass
     */
    void flipHourglass(Player player);

    /**
     * Places the player's ship on the flight board at a specific starting position.
     *
     * @param player the player placing their ship
     * @param startingPosition the position on the flight board where the ship should be placed
     */
    void placeShipOnFlightBoard(Player player, int startingPosition);

    /**
     * Places the player's ship on the flight board at the default starting position.
     *
     * @param player the player placing their ship
     */
    void placeShipOnFlightBoard(Player player);

    /**
     * Allows a player to view a forecast card from the specified deck.
     *
     * @param player the player acquiring the forecast
     * @param deckIndex the index of the deck to view the forecast from
     */
    void acquireForecast(Player player, int deckIndex);

    /**
     * Releases a previously acquired forecast card.
     *
     * @param player the player releasing the forecast
     */
    void releaseForecast(Player player);

    //endregion

    //region Ship validity check

    /**
     * Removes a component from the player's ship at the specified coordinates.
     *
     * @param player the player removing the component
     * @param point the coordinates of the component to remove
     */
    void removeComponent(Player player, Point point);

    /**
     * Selects a specific ship piece during the ship building phase.
     *
     * @param player the player choosing the ship piece
     * @param pieceIndex the index of the piece to choose
     */
    void chooseShipPiece(Player player, int pieceIndex);
    //endregion

    //region Ship init

    /**
     * Initializes a cabin on the player's ship with the specified crew type.
     *
     * @param player the player initializing the cabin
     * @param point the coordinates where the cabin should be placed
     * @param crewType the type of crew to place in the cabin
     */
    void initializeCabin(Player player, Point point, CrewType crewType);
    //endregion

    //region Adventure

    /**
     * Draws a card during the adventure phase.
     *
     * @param player the player drawing the card
     */
    void drawCard(Player player);

    /**
     * Activates a component on the player's ship at the specified coordinates.
     *
     * @param player the player activating the component
     * @param point the coordinates of the component to activate
     */
    void activateComponent(Player player, Point point);

    /**
     * Signals that a crew member is lost at the specified location.
     *
     * @param player the player losing the crew
     * @param point the coordinates where the crew is lost
     */
    void loseCrew(Player player, Point point);

    /**
     * Collects a reward during the adventure phase.
     *
     * @param player the player grabbing the reward
     */
    void grabReward(Player player);

    /**
     * Places goods of a specific type at the specified location on the player's ship.
     *
     * @param player the player placing the goods
     * @param point the coordinates where the goods should be placed
     * @param goodsType the type of goods to place
     */
    void placeGoods(Player player, Point point, GoodsType goodsType);

    /**
     * Removes goods of a specific type from the specified location on the player's ship.
     *
     * @param player the player removing the goods
     * @param point the coordinates from which to remove the goods
     * @param goodsType the type of goods to remove
     */
    void removeGoods(Player player, Point point, GoodsType goodsType);

    /**
     * Signals that goods are lost at the specified location.
     *
     * @param player the player losing the goods
     * @param point the coordinates where the goods are lost
     */
    void loseGoods(Player player, Point point);

    /**
     * Uses a battery component at the specified location.
     *
     * @param player the player using the battery
     * @param point the coordinates of the battery to use
     */
    void useBattery(Player player, Point point);

    /**
     * Selects a planet during the adventure phase.
     *
     * @param player the player choosing the planet
     * @param choice the index of the chosen planet
     */
    void choosePlanet(Player player, int choice);

    /**
     * Signals that the player is ready to proceed to the next screen/state.
     *
     * @param player the player who is ready to proceed
     */
    void goNext(Player player);

    /**
     * Signals that the player wants to abandon the current flight.
     *
     * @param player the player giving up
     */
    void giveUp(Player player);
    //endregion
}
