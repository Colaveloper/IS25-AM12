package it.polimi.ingsw.galaxytruckers.serverController.lobby;

import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.Direction;

import java.awt.*;

public interface LobbyInterface {

    //region Shipbuilding
    /**
     * Calls {@link it.polimi.ingsw.galaxytruckers.model.GameModelInterface#requestRandComponent(Game, ShipBoard)}
     * on the lobby game and the player's shipBoard
     * @param player the player wishing to perform the action
     */
    void requestRandComponent(Player player);

    /**
     * Calls {@link it.polimi.ingsw.galaxytruckers.model.GameModelInterface#requestComponent(Game, ShipBoard, int)}
     * on the lobby game, the player's shipboard and the given id
     * @param player the nickname of the player wishing to perform the action
     * @param componentID the id of the component the player wishes to take
     */
    void requestComponent(Player player, int componentID);

    //TODO : add documentation for these game methods
    void rejectComponent(Player player);
    void stashComponent(Player player);
    void grabStashedComponent(Player player, int index);
    void placeComponent(Player player, Point point, Direction orientation);
    void flipHourglass(Player player);
    void placeShipOnFlightBoard(Player player, int startingPosition);
    void acquireForecast(Player player, int deckIndex);
    void releaseForecast(Player player);

    //endregion

    //region Ship validity check
    void removeComponent(Player player, Point point);
    void chooseShipPiece(Player player, int pieceIndex);
    //endregion

    //region Ship init
    void initializeCabin(Player player, Point point, CrewType crewType);
    //endregion

    //region Adventure
    void drawCard(Player player);
    void activateComponent(Player player, Point point);
    void loseCrew(Player player, Point point);
    void grabReward(Player player, boolean rewardGrabbed);
    void placeGoods(Player player, Point point, GoodsType goodsType);
    void removeGoods(Player player, Point point, GoodsType goodsType);
    void loseGoods(Player player, Point point);
    void useBattery(Player player, Point point);
    void choosePlanet(Player player, int choice);
    void goNext(Player player);

    void giveUp(Player player);
    //endregion
}
