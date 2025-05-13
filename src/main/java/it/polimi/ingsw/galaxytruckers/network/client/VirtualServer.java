package it.polimi.ingsw.galaxytruckers.network.client;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;

import java.awt.*;
import java.util.UUID;

public interface VirtualServer {
    void registerNickname(String myNickname);
    void newGame(Level level, int playerN);
    void drawCard();
    void joinLobby(UUID lobbyID);
    void leaveLobby(String nickname);

    // Game methods

    // Ship building
    void requestRandComponent(String nickname);
    void requestComponent(int componentID);
    void rejectComponent(String nickname);
    void stashComponent(String nickname);
    void grabStashedComponent(int index);
    void placeComponent(Point point, int orientation);
    void flipHourglass(String nickname);
    void placeShipOnFlightBoard(int startingPosition);
    void acquireForecast(int deckIndex);
    void releaseForecast(String nickname);

    // Ship validity check
    void removeComponent(Point point);
    void chooseShipPiece(int pieceIndex);

    // Ship init
    void initializeCabin(Point point, CrewType crewType);

    // Adventure
    void activateComponent(Point point);
    void loseCrew(Point point);
    void grabReward(boolean rewardGrabbed);
    void placeGoods(Point point, GoodsType goodsType);
    void removeGoods(Point point, GoodsType goodsType);
    void loseGoods(Point point);
    void useBattery(Point point);
    void choosePlanet(int choice);
    void goNext(String nickname);

    void giveUp(String nickname);
}
