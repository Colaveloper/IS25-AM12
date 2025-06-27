package it.polimi.ingsw.galaxytruckers.server.controller.lobby;

import it.polimi.ingsw.galaxytruckers.server.model.GameInterface;
import it.polimi.ingsw.galaxytruckers.shared.enums.GoodsType;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.shared.enums.Direction;

import java.awt.*;

/**
 * Interface for lobbies in the Galaxy Truckers game.
 * Each method delegates to a corresponding method in {@link GameInterface}.
 */
public interface LobbyInterface {

    //region Shipbuilding

    /**
     * Delegates to {@link GameInterface#requestRandComponent(ShipBoard)}.
     *
     * @param player the player wishing to perform the action
     */
    void requestRandComponent(Player player);

    /**
     * Delegates to {@link GameInterface#requestComponent(ShipBoard, int)}.
     *
     * @param player      the nickname of the player wishing to perform the action
     * @param componentID the id of the component the player wishes to take
     */
    void requestComponent(Player player, int componentID);

    /**
     * Delegates to {@link GameInterface#rejectComponent(ShipBoard)}.
     *
     * @param player the player wishing to reject the component
     */
    void rejectComponent(Player player);

    /**
     * Delegates to {@link GameInterface#stashComponent(ShipBoard)}.
     *
     * @param player the player wishing to stash the component
     */
    void stashComponent(Player player);

    /**
     * Delegates to {@link GameInterface#grabPlacedComponent(ShipBoard)}.
     *
     * @param player the player wishing to grab a placed component
     */
    void grabPlacedComponent(Player player);

    /**
     * Delegates to {@link GameInterface#grabStashedComponent(ShipBoard, int)}.
     *
     * @param player the player wishing to grab a stashed component
     * @param index the index of the component in the player's stash
     */
    void grabStashedComponent(Player player, int index);

    /**
     * Delegates to {@link GameInterface#placeComponent(ShipBoard, Point, Direction)}.
     *
     * @param player the player wishing to place the component
     * @param point the coordinates where the component should be placed
     * @param orientation the direction the component should face
     */
    void placeComponent(Player player, Point point, Direction orientation);

    /**
     * Delegates to {@link GameInterface#flipHourglass(ShipBoard)}.
     *
     * @param player the player flipping the hourglass
     */
    void flipHourglass(Player player);

    /**
     * Delegates to {@link GameInterface#placeShipOnFlightBoard(ShipBoard, int)}.
     *
     * @param player the player placing their ship
     * @param startingPosition the position on the flight board where the ship should be placed
     */
    void placeShipOnFlightBoard(Player player, int startingPosition);

    /**
     * Delegates to {@link GameInterface#placeShipOnFlightBoard(ShipBoard)}.
     *
     * @param player the player placing their ship
     */
    void placeShipOnFlightBoard(Player player);

    /**
     * Delegates to {@link GameInterface#acquireForecast(ShipBoard, int)}.
     *
     * @param player the player acquiring the forecast
     * @param deckIndex the index of the deck to view the forecast from
     */
    void acquireForecast(Player player, int deckIndex);

    /**
     * Delegates to {@link GameInterface#releaseForecast(ShipBoard)}.
     *
     * @param player the player releasing the forecast
     */
    void releaseForecast(Player player);

    //endregion

    //region Ship validity check

    /**
     * Delegates to {@link GameInterface#removeComponent(ShipBoard, Point)}.
     *
     * @param player the player removing the component
     * @param point the coordinates of the component to remove
     */
    void removeComponent(Player player, Point point);

    /**
     * Delegates to {@link GameInterface#chooseShipPiece(ShipBoard, int)}.
     *
     * @param player the player choosing the ship piece
     * @param pieceIndex the index of the piece to choose
     */
    void chooseShipPiece(Player player, int pieceIndex);
    //endregion

    //region Ship init

    /**
     * Delegates to {@link GameInterface#initializeCabin(ShipBoard, Point, CrewType)}.
     *
     * @param player the player initializing the cabin
     * @param point the coordinates where the cabin should be placed
     * @param crewType the type of crew to place in the cabin
     */
    void initializeCabin(Player player, Point point, CrewType crewType);
    //endregion

    //region Adventure

    /**
     * Delegates to {@link GameInterface#drawCard(ShipBoard)}.
     *
     * @param player the player drawing the card
     */
    void drawCard(Player player);

    /**
     * Delegates to {@link GameInterface#activateComponent(ShipBoard, Point)}.
     *
     * @param player the player activating the component
     * @param point the coordinates of the component to activate
     */
    void activateComponent(Player player, Point point);

    /**
     * Delegates to {@link GameInterface#loseCrew(ShipBoard, Point)}.
     *
     * @param player the player losing the crew
     * @param point the coordinates where the crew is lost
     */
    void loseCrew(Player player, Point point);

    /**
     * Delegates to {@link GameInterface#grabReward(ShipBoard)}.
     *
     * @param player the player grabbing the reward
     */
    void grabReward(Player player);

    /**
     * Delegates to {@link GameInterface#placeGoods(ShipBoard, Point, GoodsType)}.
     *
     * @param player the player placing the goods
     * @param point the coordinates where the goods should be placed
     * @param goodsType the type of goods to place
     */
    void placeGoods(Player player, Point point, GoodsType goodsType);

    /**
     * Delegates to {@link GameInterface#removeGoods(ShipBoard, Point, GoodsType)}.
     *
     * @param player the player removing the goods
     * @param point the coordinates from which to remove the goods
     * @param goodsType the type of goods to remove
     */
    void removeGoods(Player player, Point point, GoodsType goodsType);

    /**
     * Delegates to {@link GameInterface#loseGood(ShipBoard, Point)}.
     *
     * @param player the player losing the goods
     * @param point the coordinates where the goods are lost
     */
    void loseGoods(Player player, Point point);

    /**
     * Delegates to {@link GameInterface#useBattery(ShipBoard, Point)}.
     *
     * @param player the player using the battery
     * @param point the coordinates of the battery to use
     */
    void useBattery(Player player, Point point);

    /**
     * Delegates to {@link GameInterface#choosePlanet(ShipBoard, int)}.
     *
     * @param player the player choosing the planet
     * @param choice the index of the chosen planet
     */
    void choosePlanet(Player player, int choice);

    /**
     * Delegates to {@link GameInterface#goNext(ShipBoard)}.
     *
     * @param player the player who is ready to proceed
     */
    void goNext(Player player);

    /**
     * Delegates to {@link GameInterface#giveUp(ShipBoard)}.
     *
     * @param player the player giving up
     */
    void giveUp(Player player);
    //endregion
}
