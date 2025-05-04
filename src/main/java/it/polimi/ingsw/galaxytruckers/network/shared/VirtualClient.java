package it.polimi.ingsw.galaxytruckers.network.shared;


import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.view.ProjectileType;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface VirtualClient {

    // TODO: adjust some signatures to account for multiple ships

    // Setup
    void showNicknameRegistration(String nickname) throws Exception;
    void showGameCreation(Level level, int playersNum, UUID game) throws Exception;
    void showGameJoining(String nickname, UUID game) throws Exception;
    void showColorSelection(String nickname, Colors color) throws Exception;
    void setFlightBoard(int loopLength, List<Integer> startingPositions) throws Exception;
    void setShipBoard(Set<Point> shipArea) throws Exception;

    // Building
    void showStashUpdate(List<Integer> stashedComponentIds) throws Exception;
    void showComponentPositioning(int componentId, int direction, Point position) throws Exception;
    void showUncoveredUpdate(List<Integer> uncoveredComponentIds, int coveredComponents) throws Exception;
    void showForecast(List<Integer> cardsIds) throws Exception;
    void showNewHourglass() throws Exception;
    void showPlayerToPlaceUpdate(Map<String, Integer> playerToPlace) throws Exception;

    // Validity Check
    void showComponentRemoval(Point position) throws Exception;
    void showStatsUpdate() throws Exception; // TODO: define stats format

    // Initialization
    void showUpdateBatteries(Point position, int batteries) throws Exception;
    void showChoice(List<String> choices) throws Exception;
    void showUpdateCrew(Point position, int crew, CrewType crewType) throws Exception;

    // Flight
    void showNewCard(Integer cardId) throws Exception;
    void showProjectile(ProjectileType projectileType, int direction, int roll) throws Exception;
    void showUpdateCargoHold(Point position, List<GoodsType> goods) throws Exception;
    void showUpdateGoodBuffer(List<GoodsType> goods) throws Exception;
    void setSelectablePoints(List<Point> points) throws Exception;

    // End Game
    void showFinalStats() throws Exception; // TODO: define stats format
    void reportError(String details) throws Exception;
}
