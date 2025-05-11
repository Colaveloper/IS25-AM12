package it.polimi.ingsw.galaxytruckers.network.client;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import it.polimi.ingsw.galaxytruckers.view.viewEnums.ProjectileType;

import java.awt.*;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface ClientControllerInterface {
    void showInterfaceChoice(VirtualServer server) throws IOException;

    void showGameCreation() throws IOException;

    void showLobbyUpdate(List<String> names) throws IOException;

    void showConnectedAndNicknameChoice(String tempNickname) throws IOException;

    void setMyNickname(String nickname);

    void setNickname(String nickname);

    void showColorSelection(String nickname, Colors color);

    void setFlightBoard(int loopLength, List<Integer> startingPositions) throws IOException;

    void setCurrentComponent(int componentId) throws IOException;

    void showStashUpdate(List<Integer> stashedComponentIds) throws IOException;

    void showStartBuilding(int coveredComponentsTot) throws IOException;

    void showComponentPositioning(String nickname, int componentId, int direction, Point position) throws IOException;

    void showUncoveredUpdate(List<Integer> uncoveredComponentIds, int coveredComponents);

    void addReavealedComponent(int componentId) throws IOException;

    void setCoveredComponents(int coveredComponents) throws IOException;

    void showForecast(List<Integer> cardsIds);

    void showNewHourglass();

    void showPlayerToPlaceUpdate(Map<String, Integer> playerToPlace);

    void showComponentRemoval(Point position, String nickname) throws IOException;

    void showChoice(List<String> choices);

    void updateBatteries(String nickname, Point position, int batteries) throws IOException;

    void initializeCabin(String nickname, Point position, CrewType crewType, int crew) throws IOException;

    void updateCrewNumber(String nickname, Point position, int crew) throws IOException;

    void updateGoods(String nickname, Point position, List<GoodsType> goods) throws IOException;

    void updateCredits(String nickname, int credits) throws IOException;

    void updateLostComponent(String nickname, int componentsLost) throws IOException;

    void showNewCard(Integer cardId) throws IOException;

    // first time goods are shown on screen
    void showPlaceGoods() throws IOException;

    // planetId is an index and starts from 0, UI listing on screen starts from 1
    void choosePlanet(int planetId) throws IOException;

    // update each time player picks something removing good taken in the buffer by index
    void updateGoodsBuffer(int index) throws IOException;

    // set current player for any action that involves a decision
    void setCurrentPlayer(String nickname);

    // called for each projectile
    void showProjectile(ProjectileType projectileType, int direction, int roll) throws IOException;

    void showStatsUpdate();

    void showFinalStats();

    void showSelectablePoints(List<Point> points) throws IOException;

    void reportError(String details) throws RemoteException;
}
