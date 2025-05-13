package it.polimi.ingsw.galaxytruckers.serverController;

import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.awt.*;
import java.util.UUID;

public interface ServerControllerInterface {
    /**
     * Creates a new player with the given nickname
     * @param nickname the nickname of the new player
     * @throws IllegalArgumentException if there is already a player
     * with the given nickname
     */
    Player registerNickname(String nickname);

    /**
     * Creates a new lobby for a game of the chosen level and with
     * the specified number of players, adding the creator to it
     * @param creatorName nickname of the lobby creator
     * @param level level of the game
     * @param numPlayers number of players in the game
     * @return the game ID, as a {@code UUID} object
     * @throws IllegalArgumentException if {@code numPlayers} is < 2
     */
    void newGame(String creatorName, Level level, int numPlayers);

    /**
     * Adds the player with the specified nickname to the lobby
     * with the given lobby ID
     * @param nickname the nickname of the player joining a lobby
     * @param lobbyID the ID of the lobby the player wants to join
     * @throws IllegalArgumentException if there is no registered
     * player with the given nickname or if there is no lobby with
     * the given ID
     * @throws IllegalStateException if the specified lobby is not
     * in preparation phase
     */
    void joinLobby(String nickname, UUID lobbyID);

    /**
     * Removes the player with the given nickname from the lobby
     * they are currently in
     * @param nickname the nickname of the player wishing to leave
     *                 the lobby
     * @throws IllegalArgumentException if there is no registered
     * player with the given nickname
     * @throws IllegalStateException if the player has not joined
     * a lobby yet
     */
    void leaveLobby(String nickname);

    /**
     * Sets the color of the player with the given nickname to the
     * given color, if no other player in the same lobby has already
     * chosen that color
     * @param nickname the nickname of the player choosing the color
     * @param color the color chosen by the player
     * @throws IllegalArgumentException if there is no registered
     * player with the given nickname or if there is no lobby with
     * the given ID or if some other player in the lobby has
     * already chosen the given color
     * @throws IllegalStateException if the player has not joined
     * a lobby yet or if the lobby is not in preparation phase
     */
    void chooseColor(String nickname, Colors color);

    // Game methods

    // Ship building
    /**
     * Calls {@link it.polimi.ingsw.galaxytruckers.model.GameModelInterface#requestRandComponent(Game, ShipBoard)}
     * passing as parameters the game the player is in and their assigned ship board
     * @param nickname the nickname of the player wishing to perform the action
     * @throws IllegalArgumentException if there is no registered player with
     * the given nickname
     * @throws IllegalStateException if the player has not joined a lobby or if their
     * active lobby is not in game phase
     */
    void requestRandComponent(String nickname);

    /**
     * Calls {@link it.polimi.ingsw.galaxytruckers.model.GameModelInterface#requestComponent(Game, ShipBoard, int)}
     * passing as parameters the game the player is in and their assigned ship board
     * @param nickname the nickname of the player wishing to perform the action
     * @param componentID the id of the component the player wishes to take
     * @throws IllegalArgumentException if there is no registered player with
     * the given nickname
     * @throws IllegalStateException if the player has not joined a lobby or if their
     * active lobby is not in game phase
     */
    void requestComponent(String nickname, int componentID);

    //TODO : add documentation for these game methods
    void rejectComponent(String nickname);
    void stashComponent(String nickname);
    void grabStashedComponent(String nickname, int index);
    void placeComponent(String nickname, Point point, int orientation);
    void flipHourglass(String nickname);
    void placeShipOnFlightBoard(String nickname, int startingPosition);
    void acquireForecast(String nickname, int deckIndex);
    void releaseForecast(String nickname);

    // Ship validity check
    void removeComponent(String nickname, Point point);
    void chooseShipPiece(String nickname, int pieceIndex);

    // Ship init
    void initializeCabin(String nickname, Point point, CrewType crewType);

    // Adventure
    void drawCard(String nickname);
    void activateComponent(String nickname, Point point);
    void loseCrew(String nickname, Point point);
    void grabReward(String nickname, boolean rewardGrabbed);
    void placeGoods(String nickname, Point point, GoodsType goodsType);
    void removeGoods(String nickname, Point point, GoodsType goodsType);
    void loseGoods(String nickname, Point point);
    void useBattery(String nickname, Point point);
    void choosePlanet(String nickname, int choice);
    void goNext(String nickname);

    void giveUp(String nickname);
}
