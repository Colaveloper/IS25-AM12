package it.polimi.ingsw.galaxytruckers.network.client;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.StatType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.view.enums.ProjectileType;

import java.awt.*;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface FakeInterface {
    //void showInterfaceChoice(VirtualServer server) throws IOException;    //interno

    //void showGameCreation() throws IOException; //interno

    void updateLobbyPlayers(Map<String, GameColor> playerToColor) throws IOException;

    //void showConnectedAndNicknameChoice(String tempNickname) throws IOException;

    //void setMyNickname(String nickname);//usare exceptino

    void notifyStashComponent(String playerName, List<Integer> stashComponentIds) throws IOException;

    void notifyGrabFromStash(String playerName, int componentId, List<Integer> stashComponentIds) throws IOException;

    void notifyComponentPositioning(String nickname, int componentId, int direction, Point position) throws IOException;

    void notifyComponentRejection(String playerName, int componentId) throws IOException;

    void notifyFaceDownComponentRequest(String playerName, int componentId) throws IOException;

    void notifyFaceUpComponentRequest(String playerName, int componentId) throws IOException;

    void notifyPeekForecast(String playerName, int deckIndex) throws IOException;

    void notifyReleaseForecast(String playerName, int deckIndex) throws IOException;

    void sendForecastDeck(List<Integer> deckCardIds) throws IOException;

    void notifyHourglassFlipped(String playerName, boolean isLast);//il countdown è lato client

    void notifyHourglassEnd();

    //void notifyPlaceShipOnFlightBoard(String nickname, Map<String, Integer> playerToPlace);

    void notifyPlayerPosition(String playerName, int position) throws IOException;//tommy approbved

    void notifyComponentRemoval(String nickname, Point positionPoint) throws IOException;//todo change order

    void notifyBatteryUpdate(String nickname, Point position, int batteries) throws IOException;

    void notifyCabinUpdate(String nickname, Point position, int crew, CrewType crewType) throws IOException;

    void notifyCargoHoldUpdate(String nickname, Point position, Map<GoodsType, Integer> goods) throws IOException;

    //void updateCredits(String nickname, int credits) throws IOException;

    void notifyShipStatusUpdate(String nickname, StatType statType, int value) throws IOException;//todo add for other enums

    //void updateLostComponent(String nickname, int componentsLost) throws IOException;

    void notifyNewCard(int cardId) throws IOException;

    // first time goods are shown on screen
    //void showPlaceGoods() throws IOException;//todo stati pain

    // planetId is an index and starts from 0, UI listing on screen starts from 1
    void choosePlanet(int planetId) throws IOException;

    // update each time player picks something removing good taken in the buffer by index
    void updateGoodsBuffer(GoodsType type) throws IOException;

    // set current player for any action that involves a decision
    //void setCurrentPlayer(String nickname);//todo

    // called for each projectile
    void showProjectile(ProjectileType projectileType, int direction, int roll) throws IOException;

    //void showStatsUpdate();

    void showFinalStats();

    void showSelectablePoints(List<Point> points) throws IOException;

    void reportError(String details) throws RemoteException;
}