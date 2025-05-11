package it.polimi.ingsw.galaxytruckers.network.client;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import it.polimi.ingsw.galaxytruckers.view.*;
import it.polimi.ingsw.galaxytruckers.view.screens.*;
import it.polimi.ingsw.galaxytruckers.view.viewEnums.ProjectileType;
import javafx.application.Application;

import java.awt.*;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.List;

public class ClientController implements ClientControllerInterface {
    private final ClientModel model;
    private View view;

    public ClientController(VirtualServer server) {
        this.model = new ClientModel();
    }

    @Override
    public void showInterfaceChoice(VirtualServer server) throws IOException {
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

    public void setCLIViewManually () {
        view = new CliView();
        view.setModel(model);
    }


    // UPDATES FROM THE SERVER

    @Override
    public void showGameCreation() throws IOException {
        view.setScreen(new GameCreationScreen());
    }

    @Override
    public void updateLobbyPlayers(Map<String, Colors> playerToColor) throws IOException {
        for (Map.Entry<String, Colors> entry : playerToColor.entrySet()) {
            model.setPlayerColor(entry.getKey(), entry.getValue());
            model.addPlayer(entry.getKey());
        }
        view.setScreen(new LobbyScreen());
    }

    @Override
    public void showConnectedAndNicknameChoice(String tempNickname) throws IOException {
        model.setMyNickname(tempNickname);
        view.setScreen(new NicknameChoiceScreen());
    }

    @Override
    public void setMyNickname(String nickname) { // gets called only after legal registration
        model.setMyNickname(nickname);
    }

    // integrate with following method
//    public void showColorSelection(String nickname, Colors color) {
//        model.setPlayerColor(nickname, color);
//        // view.show(ChosenStrategy)
//    }

    public void setupGame(int loopLength, List<Integer> startingPositions, Set<Point> shipArea, int coveredComponent) throws IOException {
        model.setFlightBoard(loopLength, startingPositions);
        model.setShipArea(shipArea);
        model.setCoveredComponents(coveredComponent);
        view.setScreen(new ShipBuildingScreen());
    }

    // UPDATE FOR SHIP BUILDING

    @Override
    public void notifyStashComponent(String playerName, List<Integer> stashComponentIds) throws IOException {
        model.setStashedComponents(stashComponentIds);//todo: add for other players
    }

    @Override
    public void showStartBuilding(int coveredComponentsTot) throws IOException {

    }

    @Override
    public void showComponentPositioning(String nickname, int componentId, int direction, Point position) throws IOException {
        model.setComponent(nickname, componentId, direction, position);
        //view.setScreen(new ShipBuildingScreen());
    }

    @Override
    public void notifyComponentRejection(String playerName, int componentId, List<Integer> faceUpComponentIds) throws IOException {
        if (model.isMyNickname(playerName)) {//TODO add for other players
            model.clearCurrentComponent();
        }
        model.setRevealedComponent(faceUpComponentIds);
    }

    @Override
    public void notifyFaceDownComponentRequest(String playerName, int componentId, int numFaceDown) throws IOException {
        if (model.isMyNickname(playerName)) {//TODO add for other players
            model.setCurrentComponent(componentId);
        }
        model.setCoveredComponents(numFaceDown);
    }

    @Override
    public void notifyFaceUpComponentRequest(String playerName, int componentId, List<Integer> faceUpComponentIds) throws IOException {
        if (model.isMyNickname(playerName)) {//TODO add for other players
            model.setCurrentComponent(componentId);
        }
        model.setRevealedComponent(faceUpComponentIds);
    }


    @Override
    public void showForecast(List<Integer> cardsIds) {
        // model.update
        // view.show(ChosenStrategy)
    }

    @Override
    public void showNewHourglass() {
        // model.update
        // view.show(ChosenStrategy)
    }

    @Override
    public void showPlayerToPlaceUpdate(Map<String, Integer> playerToPlace) {
        model.setPlayerToPlace(playerToPlace);
        // view.show(ChosenStrategy)
    }

    @Override
    public void showComponentRemoval(Point position, String nickname) throws IOException {
        model.removeComponent(position, nickname);
        // view.show(ChosenStrategy)
    }

    @Override
    public void showChoice(List<String> choices) {
        // model.update
        // view.show(ChosenStrategy)
    }

    // UPDATE FOR COMPONENT (only data update and nothing to ask or show, called for any player independently)

    @Override
    public void updateBatteries(String nickname, Point position, int batteries) throws IOException {
        model.setBatteries(nickname, position, batteries);
        view.refresh();
    }

    @Override
    public void initializeCabin(String nickname, Point position, CrewType crewType, int crew) throws IOException {
        model.initializeCabin(nickname, position, crewType, crew);
    }

    @Override
    public void updateCrewNumber(String nickname, Point position, int crew) throws IOException {
        model.setCrewNumber(nickname, position, crew);
        view.refresh();
    }

    @Override
    public void updateGoods(String nickname, Point position, List<GoodsType> goods) throws IOException {
        model.setGoods(nickname, position, goods);
        view.refresh();
    }

    @Override
    public void updateCredits(String nickname, int credits) throws IOException {
        model.setCredits(nickname, credits);
        view.refresh();
    }

    @Override
    public void updateLostComponent(String nickname, int componentsLost) throws IOException {
        model.setLostComponent(nickname, componentsLost);
    }

    // UPDATE FOR ADVENTURE

    @Override
    public void showNewCard(int cardId) throws IOException {
        model.setCurrentCard(cardId);
        model.setCurrentPlayerNickname(model.getCurrentLeader());
        view.setScreen(new NewCardScreen());
    }

    // first time goods are shown on screen
    @Override
    public void showPlaceGoods() throws IOException {
        view.setScreen(new GoodsScreen());
    }

    // planetId is an index and starts from 0, UI listing on screen starts from 1
    @Override
    public void choosePlanet(int planetId) throws IOException {
        model.setPlanetGoodBuffer(planetId);
    }

    // update each time player picks something removing good taken in the buffer by index
    @Override
    public void updateGoodsBuffer(int index) throws IOException {
        model.updateGoodsBuffer(index);
        //view.refresh();
        //view.run(new GoodsScreen());
    }

    // set current player for any action that involves a decision
    @Override
    public void setCurrentPlayer(String nickname) {
        model.setCurrentPlayerNickname(nickname);
    }

    // called for each projectile
    @Override
    public void showProjectile(ProjectileType projectileType, int direction, int roll) throws IOException {
        model.setProjectile(projectileType, direction, roll);
        view.setScreen(new ProjectilesScreen());
    }

    @Override
    public void showStatsUpdate() {
        // model.update
        // view.show(ChosenStrategy)
    }

    // UPDATE FOR ENDGAME

    @Override
    public void showFinalStats() {
        // model.update
        // view.show(ChosenStrategy)
    }

    // other updates

    @Override
    public void showSelectablePoints(List<Point> points) throws IOException {
        model.setSelectablePoints(points);
        //view.run(new PointSelectionScreen());
    }

    @Override
    public void reportError(String details) throws RemoteException {
        // model.update
        // view.show(ChosenStrategy)
    }





}
