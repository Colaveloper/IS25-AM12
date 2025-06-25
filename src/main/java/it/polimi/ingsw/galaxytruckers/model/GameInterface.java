package it.polimi.ingsw.galaxytruckers.model;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.Direction;

import java.awt.*;

public interface GameInterface {
    void requestSnapshot(ShipBoard shipBoard);

    ShipBoard addShipBoard(GameColor color);

    void setStartAdventureCallback(Runnable startAdventureCallback);

    void start();

    void skipBuilding();

    void skip(ShipBoard shipBoard);

    void requestRandComponent(ShipBoard shipBoard);

    void requestComponent(ShipBoard shipBoard, int componentID);

    void rejectComponent(ShipBoard shipBoard);

    void stashComponent(ShipBoard shipBoard);

    void grabPlacedComponent(ShipBoard shipBoard);

    void grabStashedComponent(ShipBoard shipBoard, int index);

    void placeComponent(ShipBoard shipBoard, Point point, Direction orientation);

    void flipHourglass(ShipBoard shipBoard);

    void placeShipOnFlightBoard(ShipBoard shipBoard, int startingPosition);

    void placeShipOnFlightBoard(ShipBoard shipBoard);

    void acquireForecast(ShipBoard shipBoard, int deckIndex);

    void releaseForecast(ShipBoard shipBoard);

    void removeComponent(ShipBoard shipBoard, Point point);

    void chooseShipPiece(ShipBoard shipBoard, int pieceIndex);

    void initializeCabin(ShipBoard shipBoard, Point point, CrewType crewType);

    void activateComponent(ShipBoard shipBoard, Point point);

    void loseCrew(ShipBoard shipBoard, Point point);

    void grabReward(ShipBoard shipBoard);

    void placeGoods(ShipBoard shipBoard, Point point, GoodsType goodsType);

    void removeGoods(ShipBoard shipBoard, Point point, GoodsType goodsType);

    void useBattery(ShipBoard shipBoard, Point point);

    void choosePlanet(ShipBoard shipBoard, int choice);

    void giveUp(ShipBoard shipBoard);

    void drawCard(ShipBoard shipBoard);

    void loseGood(ShipBoard shipBoard, Point point);

    void goNext(ShipBoard shipBoard);
}
