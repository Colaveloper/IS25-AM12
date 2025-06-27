package it.polimi.ingsw.galaxytruckers.server.model;

import it.polimi.ingsw.galaxytruckers.shared.enums.GameColor;
import it.polimi.ingsw.galaxytruckers.shared.enums.GoodsType;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.shared.enums.Direction;

import java.awt.*;

public interface GameInterface {

    /**
     * Requests a snapshot of the game state for the given ship board. The
     * snapshot will be notified to the event listener.
     *
     * @param shipBoard the ship board for which to request the snapshot
     */
    void requestSnapshot(ShipBoard shipBoard);

    /**
     * Adds a ship board to the game with the specified color.
     *
     * @param color the color of the ship board to add
     * @return the newly added ship board
     */
    ShipBoard addShipBoard(GameColor color);

    /**
     * Sets the callback to be executed when the adventure starts.
     *
     * @param startAdventureCallback the callback to be executed when the adventure starts
     */
    void setStartAdventureCallback(Runnable startAdventureCallback);

    /**
     * Starts the game, setting the game state to the initial state.
     */
    void start();

    /**
     * Starts a game directly in the adventure phase, skipping the ship building phase.
     * Initializes the deck and the flight board and sets the current state to DrawCardState.
     */
    void skipBuilding();

    /**
     * Skips the available actions for the given ship board.
     *
     * @param shipBoard the ship board for which to skip actions
     */
    void skip(ShipBoard shipBoard);

    /**
     * Requests a random component for the specified ship board.
     *
     * @param shipBoard the ship board to request the component for
     */
    void requestRandComponent(ShipBoard shipBoard);

    /**
     * Requests a specific component by its ID for the specified ship board.
     *
     * @param shipBoard the ship board to request the component for
     * @param componentID the ID of the component to request
     */
    void requestComponent(ShipBoard shipBoard, int componentID);

    /**
     * Rejects the current component of the specified ship board.
     *
     * @param shipBoard the ship board from which to reject the component
     */
    void rejectComponent(ShipBoard shipBoard);

    /**
     * Stashes the current component of the specified ship board.
     *
     * @param shipBoard the ship board from which to stash the component
     */
    void stashComponent(ShipBoard shipBoard);

    /**
     * Grabs the placed component from the specified ship board.
     *
     * @param shipBoard the ship board from which to grab the placed component
     */
    void grabPlacedComponent(ShipBoard shipBoard);

    /**
     * Grabs a stashed component from the specified ship board at the given index.
     *
     * @param shipBoard the ship board from which to grab the stashed component
     * @param index the index of the stashed component to grab
     */
    void grabStashedComponent(ShipBoard shipBoard, int index);

    /**
     * Places a component on the specified ship board at the given point and orientation.
     *
     * @param shipBoard the ship board on which to place the component
     * @param point the point where the component should be placed
     * @param orientation the orientation of the component
     */
    void placeComponent(ShipBoard shipBoard, Point point, Direction orientation);

    /**
     * Let the specified ship board flip the hourglass
     * @param shipBoard the ship board that flips the hourglass
     */
    void flipHourglass(ShipBoard shipBoard);

    /**
     * Places a ship on the flight board at the specified starting position.
     *
     * @param shipBoard the ship board to place
     * @param startingPosition the starting position on the flight board
     */
    void placeShipOnFlightBoard(ShipBoard shipBoard, int startingPosition);

    /**
     * Places a ship on the flight board.
     *
     * @param shipBoard the ship board to place
     */
    void placeShipOnFlightBoard(ShipBoard shipBoard);

    /**
     * Acquires a forecast for the specified ship board at the given deck index.
     * @param shipBoard the ship board for which to acquire the forecast
     * @param deckIndex the index of the deck from which to acquire the forecast
     */
    void acquireForecast(ShipBoard shipBoard, int deckIndex);

    /**
     * Releases the forecast from the specified ship board.
     *
     * @param shipBoard the ship board for which to release the forecast
     */
    void releaseForecast(ShipBoard shipBoard);

    /**
     * Removes a component from the specified ship board at the given point.
     *
     * @param shipBoard the ship board from which to remove the component
     * @param point the point where the component is located
     */
    void removeComponent(ShipBoard shipBoard, Point point);

    /**
     * Chooses a ship piece from the specified ship board at the given index.
     *
     * @param shipBoard the ship board from which to choose the piece
     * @param pieceIndex the index of the piece to choose
     */
    void chooseShipPiece(ShipBoard shipBoard, int pieceIndex);

    /**
     * Initializes a cabin on the specified ship board at the given point with the specified crew type.
     *
     * @param shipBoard the ship board on which to initialize the cabin
     * @param point the point where the cabin should be initialized
     * @param crewType the type of crew that will occupy the cabin
     */
    void initializeCabin(ShipBoard shipBoard, Point point, CrewType crewType);

    /**
     * Activates a component on the specified ship board at the given point.
     *
     * @param shipBoard the ship board on which to activate the component
     * @param point the point where the component is located
     */
    void activateComponent(ShipBoard shipBoard, Point point);

    /**
     * Loses a crew member from the specified ship board at the given point.
     *
     * @param shipBoard the ship board from which to lose the crew member
     * @param point the point where the crew member is located
     */
    void loseCrew(ShipBoard shipBoard, Point point);

    /**
     * Grabs a reward from the specified ship board.
     *
     * @param shipBoard the ship board from which to grab the reward
     */
    void grabReward(ShipBoard shipBoard);

    /**
     * Places goods of a specific type on the specified ship board at the given point.
     *
     * @param shipBoard the ship board on which to place the goods
     * @param point the point where the goods should be placed
     * @param goodsType the type of goods to place
     */
    void placeGoods(ShipBoard shipBoard, Point point, GoodsType goodsType);

    /**
     * Removes goods of a specific type from the specified ship board at the given point.
     *
     * @param shipBoard the ship board from which to remove the goods
     * @param point the point where the goods are located
     * @param goodsType the type of goods to remove
     */
    void removeGoods(ShipBoard shipBoard, Point point, GoodsType goodsType);

    /**
     * Uses a battery on the specified ship board at the given point.
     *
     * @param shipBoard the ship board on which to use the battery
     * @param point the point where the battery is located
     */
    void useBattery(ShipBoard shipBoard, Point point);

    /**
     * Chooses a planet from the specified ship board based on the given choice.
     *
     * @param shipBoard the ship board from which to choose the planet
     * @param choice the index of the planet to choose
     */
    void choosePlanet(ShipBoard shipBoard, int choice);

    /**
     * Let the specified ship board give up on the current game.
     *
     * @param shipBoard the ship board that gives up
     */
    void giveUp(ShipBoard shipBoard);

    /**
     * Let the specified ship board draw a card from the deck.
     *
     * @param shipBoard the ship board that draws a card
     */
    void drawCard(ShipBoard shipBoard);

    /**
     * Removes the most valuable good from the specified ship board at the given point.
     * @param shipBoard the ship board from which to remove the good
     * @param point the point where the good is located
     */
    void loseGood(ShipBoard shipBoard, Point point);

    /**
     * Moves to the next state of the game for the specified ship board.
     *
     * @param shipBoard the ship board that asks to go to the next state
     */
    void goNext(ShipBoard shipBoard);
}
