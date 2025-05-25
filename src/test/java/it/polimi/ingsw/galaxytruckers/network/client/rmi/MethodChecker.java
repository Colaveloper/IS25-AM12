package it.polimi.ingsw.galaxytruckers.network.client.rmi;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;

import java.awt.*;
import java.util.UUID;

public interface MethodChecker {
    void registerNickname(String myNickname);
    void requestNewGame(Level level, int playerN);
    void drawCard();
    void joinLobby(UUID lobbyID);
    void leaveLobby();
    void requestRandComponent();
    void requestComponent(int componentID);
    void rejectComponent();
    void stashComponent();
    void grabStashedComponent(int index);
    void placeComponent(Point point, int orientation);
    void flipHourglass();
    void placeShipOnFlightBoard(int startingPosition);
    void acquireForecast(int deckIndex);
    void releaseForecast();
    void removeComponent(Point point);
    void chooseShipPiece(int pieceIndex);
    void initializeCabin(Point point, CrewType crewType);
    void activateComponent(Point point);
    void loseCrew(Point point);
    void grabReward(boolean rewardGrabbed);
    void placeGoods(Point point, GoodsType goodsType);
    void removeGoods(Point point, GoodsType goodsType);
    void loseGoods(Point point);
    void useBattery(Point point);
    void choosePlanet(int choice);
    void goNext();
    void giveUp();
}