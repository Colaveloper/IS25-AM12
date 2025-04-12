package it.polimi.ingsw.galaxytruckers.model;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.UUID;

public interface GameModelInterface {
    Game createGame(Level level);
    ShipBoard addShip(Game game, Colors color);

    //Ship building
    void requestRandComponent(Game game, ShipBoard shipBoard);
    void requestComponent(Game game, ShipBoard shipBoard, int componentID);
    void rejectComponent(Game game, ShipBoard shipBoard);
    void stashComponent(Game game, ShipBoard shipBoard);
    void grabStashedComponent(Game game, ShipBoard shipBoard, int index);
    void placeComponent(Game game, ShipBoard shipBoard, Point point, int orientation);
    void flipHourglass(Game game, ShipBoard shipBoard);
    void placeShipOnFlightBoard(Game game, ShipBoard shipBoard, int startingPosition);
    void acquireForecast(Game game, ShipBoard shipBoard, int deckIndex);
    void releaseForecast(Game game, ShipBoard shipBoard);

    // Ship validity check
    void removeComponent(Game game, ShipBoard shipBoard, Point point);
    void chooseShipPiece(Game game, ShipBoard shipBoard, int pieceIndex);

    // Ship init
    void initializeCabin(Game game, ShipBoard shipBoard, CrewType crewType);

    // Adventure
    void drawCard(Game game, ShipBoard shipBoard);
    void activateComponent(Game game, ShipBoard shipBoard, Point point);
    void loseCrew(Game game, ShipBoard shipBoard, Point point);
    void grabReward(Game game, ShipBoard shipBoard, boolean rewardGrabbed);
    void placeGoods(Game game, ShipBoard shipBoard, Point point, GoodsType goodsType);
    void removeGoods(Game game, ShipBoard shipBoard, Point point, GoodsType goodsType);
    void loseGood(Game game, ShipBoard shipBoard, Point point);
    void useBattery(Game game, ShipBoard shipBoard, Point point);
    void choosePlanet(Game game, ShipBoard shipBoard, int choice);
    void giveUp(Game game, ShipBoard shipBoard);
    void goNext(Game game, ShipBoard shipBoard);
}
