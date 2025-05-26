package it.polimi.ingsw.galaxytruckers.network.client;

import it.polimi.ingsw.galaxytruckers.view.enums.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;

import java.awt.*;
import java.util.UUID;

public interface ControllerToServer {

    void registerNickname(String input);

    void flipHourglass();

    void setMyNickname(String nickname);

    void releaseForecast();

    void requestNewGame(Level level, int playersN);

    void showGameCreation();

    void joinLobby(UUID lobbyID);

    void requestRandComponent();

    void requestComponent(int index);

    void stashComponent();

    void grabStashedComponent(int index);

    void acquireForecast(int index);

    void rejectComponent();

    void placeComponent(Point point, int orientation);

    void reportError(String details);

    void goNext();

    void chooseShipPiece(int choice);

    void activateComponent(Point point);

    void removeComponent(Point point);

    void useBattery(Point point);

    void initializeCabin(Point point, CrewType crewType);

    void placeShipOnFlightboard(int startingPosition);

    void drawCard();

    void placeGoods(Point point, GoodsType good);
}
