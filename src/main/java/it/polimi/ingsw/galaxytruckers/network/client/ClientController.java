package it.polimi.ingsw.galaxytruckers.network.client;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import it.polimi.ingsw.galaxytruckers.view.*;
import it.polimi.ingsw.galaxytruckers.view.screens.NewCardScreen;
import it.polimi.ingsw.galaxytruckers.view.screens.NicknameChoiceScreen;
import it.polimi.ingsw.galaxytruckers.view.screens.PointSelectionScreen;
import it.polimi.ingsw.galaxytruckers.view.screens.ProjectilesScreen;

import java.awt.*;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.List;

public class ClientController {
    private final ClientModel model;
    private final View view;

    public ClientController(VirtualServer server) {
        this.model = new ClientModel();
        this.view = new CliView(model, server); // View should only observe the model, not modify it
    }

    // REQUESTS TO THE SERVER // TODO MOVE IN STRATEGY
//    public void drawCard() throws IOException {
//        server.drawCard();
//    }

    // UPDATES FROM THE SERVER
    public void showNewCard(Integer cardId) throws IOException {
        model.setCurrentCard(cardId);
        view.run(new NewCardScreen());
    }

    public void showConnected(String tempNickname) throws IOException {
        model.setMyNickname(tempNickname);
        view.run(new NicknameChoiceScreen());
    }

    public void setNickname(String nickname) { // gets called only after legal registration
        model.setMyNickname(nickname);
    }

    public void showGameCreation(Level level, int playersNum) {
        // model.update
        // view.show(ChosenStrategy)
    }

    public void showGameJoining(String nickname) {
        model.addPlayer(nickname);
        // view.show(ChosenStrategy)
    }

    public void showColorSelection(String nickname, Colors color){
        model.setPlayerColor(nickname, color);
        // view.show(ChosenStrategy)
    }

    public void setFlightBoard(int loopLength, List<Integer> startingPositions) throws IOException {
        model.setFlightBoard(loopLength, startingPositions);
        view.run(new PointSelectionScreen());
    }

    public void setShipArea(Set<Point> shipArea) {
        model.setShipArea(shipArea);
        // view.show(ChosenStrategy)
    }

    // update for ship building

    public void showStashUpdate(List<Integer> stashedComponentIds) throws IOException {
        model.setStashedComponents(stashedComponentIds);
        // view.show(ChosenStrategy)
    }

    public void showComponentPositioning(String nickname, int componentId, int direction, Point position) throws IOException {
        model.setComponent(nickname, componentId, direction, position);
        // view.show(ChosenStrategy)
    }

    public void showUncoveredUpdate(List<Integer> uncoveredComponentIds, int coveredComponents) {
        // model.update
        // view.show(ChosenStrategy)
    }

    public void showForecast(List<Integer> cardsIds) {
        // model.update
        // view.show(ChosenStrategy)
    }

    public void showNewHourglass() {
        // model.update
        // view.show(ChosenStrategy)
    }

    public void showPlayerToPlaceUpdate(Map<String, Integer> playerToPlace) {
        model.setPlayerToPlace(playerToPlace);
        // view.show(ChosenStrategy)
    }

    public void showComponentRemoval(Point position, String nickname) throws IOException {
        model.removeComponent(position, nickname);
        // view.show(ChosenStrategy)
    }

    public void showStatsUpdate() {
        // model.update
        // view.show(ChosenStrategy)
    }

    public void showChoice(List<String> choices) {
        // model.update
        // view.show(ChosenStrategy)
    }

    public void showProjectile(ProjectileType projectileType, int direction, int roll) throws IOException {
        model.setProjectile(projectileType, direction, roll);
        view.run(new ProjectilesScreen());
    }

    // update for components

    public void showUpdateBatteries(String nickname, Point position, int batteries) throws IOException {
        model.loseBatteries(nickname, position, batteries);
        // view.show(ChosenStrategy);
    }

    public void showUpdateCrew(String nickname, Point position, int crew, CrewType crewType) throws IOException {
        model.setCrew(nickname, position, crew, crewType);
        // view.show(ChosenStrategy)
    }

    public void showUpdateCargoHold(String nickname, Point position, List<GoodsType> goods) throws IOException {
        model.setCargo(nickname, position, goods);
        // view.show(ChosenStrategy)
    }

    public void showUpdateGoodBuffer(List<GoodsType> goods) {
        // model.update
        // view.show(ChosenStrategy)
    }

    public void showSelectablePoints(List<Point> points) {
        model.setSelectablePoints(points);
        // view.show(ChosenStrategy)
    }

    public void showFinalStats() {
        // model.update
        // view.show(ChosenStrategy)
    }

    public void reportError(String details) throws RemoteException {
        // model.update
        // view.show(ChosenStrategy)
    }

}
