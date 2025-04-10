package it.polimi.ingsw.galaxytruckers.network.client;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.network.server.VirtualClient;
import it.polimi.ingsw.galaxytruckers.view.*;
import it.polimi.ingsw.galaxytruckers.view.visualizationStrategy.NewCardVisualization;
import it.polimi.ingsw.galaxytruckers.view.visualizationStrategy.PointSelectionVisualization;
import it.polimi.ingsw.galaxytruckers.view.visualizationStrategy.WelcomeVisualization;

import java.awt.*;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.List;

public class ClientController {
    final VirtualClient client;
    final VirtualServer server;
    final ClientGameModel model;
    final View view;

    public ClientController(VirtualClient client, VirtualServer server) {
        this.client = client;
        this.server = server;
        this.model = new ClientGameModel();
        this.view = new CliView(model, this); // TODO: let the player choose his view
    }

    // REQUESTS TO THE SERVER
    public void drawCard() throws IOException {
        server.drawCard();
    }

    // UPDATES FROM THE SERVER
    public void showNewCard(Integer cardId) throws IOException {
        model.setCurrentCard(cardId);
        view.show(new NewCardVisualization());
    }

    public void showConnected() throws RemoteException {
        view.show(new WelcomeVisualization());
        server.registerNickname(client, new Scanner(System.in).nextLine());
    }

    public void setNickname(String nickname) { // gets called only after legal registration
        model.setMyNickname(nickname);
        // TODO Show next view
        System.out.println("registered with: "+nickname);
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

    public void setFlightBoard(int loopLength, List<Integer> startingPositions) {
        model.setFlightBoard(loopLength, startingPositions);
        view.show(new PointSelectionVisualization());
    }

    public void setShipArea(Set<Point> shipArea) {
        model.setShipArea(shipArea);
        // view.show(ChosenStrategy)
    }

    public void showStashUpdate(List<Integer> stashedComponentIds) {
        // model.update
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

    public void showComponentRemoval(Point position) {
        // model.update
        // view.show(ChosenStrategy)
    }

    public void showStatsUpdate() {
        // model.update
        // view.show(ChosenStrategy)
    }

    public void showUpdateBatteries(Point position, int batteries) {
        // model.update
        // view.show(ChosenStrategy)
    }

    public void showChoice(List<String> choices) {
        // model.update
        // view.show(ChosenStrategy)
    }

    public void showUpdateCrew(Point position, int crew, CrewType crewType) {
        // model.update
        // view.show(ChosenStrategy)
    }

    public void showProjectile(ProjectileType projectileType, int direction, int roll) {
        // model.update
        // view.show(ChosenStrategy)
    }

    public void showUpdateCargoHold(Point position, List<GoodsType> goods) {
        // model.update
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
