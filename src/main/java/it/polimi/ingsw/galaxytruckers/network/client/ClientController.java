package it.polimi.ingsw.galaxytruckers.network.client;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import it.polimi.ingsw.galaxytruckers.view.*;
import it.polimi.ingsw.galaxytruckers.view.screens.*;
import javafx.application.Application;

import java.awt.*;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.List;

public class ClientController {
    private final ClientModel model;
    protected View view;

    public ClientController(VirtualServer server) {
        this.model = new ClientModel();
    }

    public void showInterfaceChoice (VirtualServer server) throws IOException {
        // TODO: consider whether to relocate prints and scans
        System.out.println("Enter \"G\" to switch to the Graphical Interface, or press any other key to continue here");
        if (new Scanner(System.in).nextLine().trim().equalsIgnoreCase("G")) {
            view = new GuiView();
            view.setServer(server);
            view.setModel(model);
            GuiView.strategy = new NicknameChoiceScreen();
            Application.launch(GuiView.class); // calls view.setScreen(...)
        } else {
            view = new CliView();
            view.setServer(server);
            view.setModel(model);
            view.setScreen(new NicknameChoiceScreen());
        }
    }

    // UPDATES FROM THE SERVER
    public void showNewCard(Integer cardId) throws IOException {
        model.setCurrentCard(cardId);
        view.setScreen(new NewCardScreen());
    }

    public void showGameCreation() throws IOException {
        view.setScreen(new GameCreationScreen());
    }

    public void showLobbyUpdate(List<String> names) throws IOException {
        model.setNicknames(names);
        view.setScreen(new LobbyScreen());
    }

    public void showConnectedAndNicknameChoice(String tempNickname) throws IOException {
        model.setMyNickname(tempNickname);
        view.setScreen(new NicknameChoiceScreen());
    }

    public void setNickname(String nickname) { // gets called only after legal registration
        model.setMyNickname(nickname);
    }

    // better to automate
//    public void showColorSelection(String nickname, Colors color) {
//        model.setPlayerColor(nickname, color);
//        // view.show(ChosenStrategy)
//    }

    public void setupGame(int loopLength, List<Integer> startingPositions, Set<Point> shipArea) throws IOException {
        model.setFlightBoard(loopLength, startingPositions);
        model.setShipArea(shipArea);
        view.setScreen(new ShipBuildingScreen());
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
        view.setScreen(new ProjectilesScreen());
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

    //TODO: notifica cambio di stato

}
