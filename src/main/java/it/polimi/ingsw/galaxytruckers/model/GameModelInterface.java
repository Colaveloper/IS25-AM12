package it.polimi.ingsw.galaxytruckers.model;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.events.EventListener;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.LobbyEvent;

import java.awt.*;

public interface GameModelInterface {
    Game createGame(Level level, Object lock);
    ShipBoard addShip(Game game, GameColor color);
    void setEventListener(Game game, EventListener<LobbyEvent> listener);
    void startGame(Game game);

    //Ship building
    void requestRandComponent(Game game, ShipBoard shipBoard);
    void requestComponent(Game game, ShipBoard shipBoard, int componentID);
    void rejectComponent(Game game, ShipBoard shipBoard);
    void stashComponent(Game game, ShipBoard shipBoard);
    void grabStashedComponent(Game game, ShipBoard shipBoard, int index);
    void placeComponent(Game game, ShipBoard shipBoard, Point point, Direction orientation);
    void flipHourglass(Game game, ShipBoard shipBoard);
    void placeShipOnFlightBoard(Game game, ShipBoard shipBoard, int startingPosition);
    void acquireForecast(Game game, ShipBoard shipBoard, int deckIndex);
    void releaseForecast(Game game, ShipBoard shipBoard);

    // Ship validity check
    void removeComponent(Game game, ShipBoard shipBoard, Point point);
    void chooseShipPiece(Game game, ShipBoard shipBoard, int pieceIndex);

    // Ship init
    void initializeCabin(Game game, ShipBoard shipBoard, Point point, CrewType crewType);

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
