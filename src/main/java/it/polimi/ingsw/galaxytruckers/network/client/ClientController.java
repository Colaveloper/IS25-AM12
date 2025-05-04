package it.polimi.ingsw.galaxytruckers.network.client;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import it.polimi.ingsw.galaxytruckers.view.*;
import it.polimi.ingsw.galaxytruckers.view.screens.*;
import it.polimi.ingsw.galaxytruckers.view.viewEnums.ProjectileType;

import java.awt.*;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.List;

public class ClientController {
    private ClientModel model;
    private View view;

    public ClientController(VirtualServer server) {
        this.model = new ClientModel();
    }

    public void showInterfaceChoice (VirtualServer server) throws IOException {
        // TODO: consider whether to relocate prints and scans
        System.out.println("Enter \"G\" to switch to the Graphical Interface, or press any other key to continue here");
        if (new Scanner(System.in).nextLine().trim().equalsIgnoreCase("G")) {
            view = new GuiView(model, server);
        } else {
            view = new CliView(model, server);
        }
        view.run(new NicknameChoiceScreen());
    }

    public void setCLIViewManually (VirtualServer server) {
        view = new CliView(model, server);
    }


    // UPDATES FROM THE SERVER

    public void showGameCreation() throws IOException {
        view.run(new GameCreationScreen());
    }

    public void showLobbyUpdate(List<String> names) throws IOException {
        model.setNicknames(names);
        view.run(new LobbyScreen());
    }

    public void showConnectedAndNicknameChoice(String tempNickname) throws IOException {
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
        //view.run(new PointSelectionScreen());
    }

    public void setShipArea(Set<Point> shipArea) {
        model.setShipArea(shipArea);
        // view.show(ChosenStrategy)
    }

    // UPDATE FOR SHIP BUILDING

    public void showStashUpdate(List<Integer> stashedComponentIds) throws IOException {
        model.setStashedComponents(stashedComponentIds);
        // view.show(ChosenStrategy)
    }

    public void showComponentPositioning(String nickname, int componentId, int direction, Point position) throws IOException {
        model.setComponent(nickname, componentId, direction, position);
        view.run(new ShipBuildingScreen());
    }

    public void showUncoveredUpdate(List<Integer> uncoveredComponentIds, int coveredComponents) {
        // model.update
        // view.show(ChosenStrategy)
    }

    public void addReavealedComponent(int componentId) throws IOException {
        model.addRevealedComponent(componentId);
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

    public void showChoice(List<String> choices) {
        // model.update
        // view.show(ChosenStrategy)
    }

    // UPDATE FOR COMPONENT (only data update and nothing to ask or show)

    public void showUpdateBatteries(String nickname, Point position, int batteries) throws IOException {
        model.loseBatteries(nickname, position, batteries);
        view.refresh();
        // view.show(ChosenStrategy);
    }

    public void showUpdateCrew(String nickname, Point position, int crew, CrewType crewType) throws IOException {
        model.setCrew(nickname, position, crew, crewType);
        view.refresh();
        // view.show(ChosenStrategy)
    }

    public void showUpdateCargoHold(String nickname, Point position, List<GoodsType> goods) throws IOException {
        model.setGoods(nickname, position, goods);
        view.refresh();
        // view.show(ChosenStrategy)
    }

    // UPDATE FOR ADVENTURE

    public void showNewCard(Integer cardId) throws IOException {
        model.setCurrentCard(cardId);
        model.setCurrentPlayerNickname(model.getCurrentLeader());
        view.run(new NewCardScreen());
    }

    // first time goods are shown on screen
    public void showPlaceGoods() throws IOException {
        view.run(new GoodsScreen());
    }

    public void choosePlanet(int planetId) throws IOException {
        model.setPlanetGoodBuffer(planetId);
    }

    // update each time player picks something, index of good taken in the buffer
    public void updateGoodsBuffer(int index) throws IOException {
        model.updateGoodsBuffer(index);
        view.refresh();
    }

    public void setCurrentPlayer(String nickname) {
        model.setCurrentPlayerNickname(nickname);
    }

    public void showProjectile(ProjectileType projectileType, int direction, int roll) throws IOException {
        model.setProjectile(projectileType, direction, roll);
        view.run(new ProjectilesScreen());
    }

    public void showStatsUpdate() {
        // model.update
        // view.show(ChosenStrategy)
    }

    // UPDATE FOR ENDGAME

    public void showFinalStats() {
        // model.update
        // view.show(ChosenStrategy)
    }

    // other updates

    public void showUpdateGoodBuffer(List<GoodsType> goods) {
        // model.update
        // view.show(ChosenStrategy)
    }

    public void showSelectablePoints(List<Point> points) {
        model.setSelectablePoints(points);
        // view.show(ChosenStrategy)
    }

    public void reportError(String details) throws RemoteException {
        // model.update
        // view.show(ChosenStrategy)
    }





}
