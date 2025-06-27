package it.polimi.ingsw.galaxytruckers.network;

import it.polimi.ingsw.galaxytruckers.shared.enums.GoodsType;
import it.polimi.ingsw.galaxytruckers.shared.enums.Level;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.shared.enums.Direction;

import java.awt.*;
import java.util.UUID;

/**
 * Interface for client-side requests to the virtual server in the Galaxy Truckers game.
 * <p>
 * Methods in this interface allow the client to interact with the game server for lobby management,
 * ship building, adventure actions, and other gameplay operations.
 * </p>
 */
public interface VirtualServer {

    //region App methods

    /**
     * Registers the client's nickname with the server.
     *
     * @param myNickname the nickname to register
     */
    void registerNickname(String myNickname);

    /**
     * Requests to create a new game lobby.
     *
     * @param level the game level
     * @param playerN the number of players
     */
    void requestNewGame(Level level, int playerN);

    /**
     * Requests to join an existing lobby.
     *
     * @param lobbyID the UUID of the lobby to join
     */
    void joinLobby(UUID lobbyID);

    /**
     * Requests to leave the current lobby.
     */
    void leaveLobby();

    //endregion

    //region Game methods

    //region Ship building

    /**
     * Requests a random component for ship building.
     */
    void requestRandComponent();

    /**
     * Requests a specific component for ship building.
     *
     * @param componentID the id of the component to request
     */
    void requestComponent(int componentID);

    /**
     * Rejects the currently offered component.
     */
    void rejectComponent();

    /**
     * Stashes the currently offered component.
     */
    void stashComponent();

    /**
     * Requests to grab a placed component from the ship.
     */
    void grabPlacedComponent();

    /**
     * Requests to grab a stashed component.
     *
     * @param index the index of the stashed component to grab
     */
    void grabStashedComponent(int index);

    /**
     * Places a component on the ship at the specified location and orientation.
     *
     * @param point the coordinates where the component should be placed
     * @param orientation the direction the component should face
     */
    void placeComponent(Point point, Direction orientation);

    /**
     * Flips the hourglass
     */
    void flipHourglass();

    /**
     * Places the ship on the flight board at the specified starting position.
     *
     * @param startingPosition the position on the flight board
     */
    void placeShipOnFlightBoard(int startingPosition);

    /**
     * Places the ship on the flight board.
     */
    void placeShipOnFlightBoard();

    /**
     * Acquires a forecast from the specified deck.
     *
     * @param deckIndex the index of the deck to view the forecast from
     */
    void acquireForecast(int deckIndex);

    /**
     * Releases the acquired forecast.
     */
    void releaseForecast();

    //endregion

    //region Ship validity check

    /**
     * Removes a component from the ship at the specified location.
     *
     * @param point the coordinates of the component to remove
     */
    void removeComponent(Point point);

    /**
     * Chooses a ship piece during ship validity check.
     *
     * @param pieceIndex the index of the piece to choose
     */
    void chooseShipPiece(int pieceIndex);

    //endregion

    //region Ship init

    /**
     * Initializes a cabin at the specified location with the given crew type.
     *
     * @param point the coordinates where the cabin should be placed
     * @param crewType the type of crew to place in the cabin
     */
    void initializeCabin(Point point, CrewType crewType);

    //endregion

    //region Adventure

    /**
     * Draws an adventure card.
     */
    void drawCard();

    /**
     * Activates a component at the specified location.
     *
     * @param point the coordinates of the component to activate
     */
    void activateComponent(Point point);

    /**
     * Removes a crew member from the specified location.
     *
     * @param point the coordinates where the crew is lost
     */
    void loseCrew(Point point);

    /**
     * Grabs a reward.
     */
    void grabReward();

    /**
     * Places goods at the specified location.
     *
     * @param point the coordinates where the goods should be placed
     * @param goodsType the type of goods to place
     */
    void placeGoods(Point point, GoodsType goodsType);

    /**
     * Removes goods from the specified location.
     *
     * @param point the coordinates from which to remove the goods
     * @param goodsType the type of goods to remove
     */
    void removeGoods(Point point, GoodsType goodsType);

    /**
     * Loses goods at the specified location or loses batteries if no
     * goods are present on the ship
     *
     * @param point the coordinates where the goods are lost
     */
    void loseGoods(Point point);

    /**
     * Uses a battery at the specified location.
     *
     * @param point the coordinates of the battery to use
     */
    void useBattery(Point point);

    /**
     * Chooses a planet during an adventure.
     *
     * @param choice the index of the chosen planet
     */
    void choosePlanet(int choice);

    /**
     * Signals readiness to proceed to the next phase.
     */
    void goNext();

    /**
     * Gives up the current game.
     */
    void giveUp();

    //endregion

    //endregion

}
