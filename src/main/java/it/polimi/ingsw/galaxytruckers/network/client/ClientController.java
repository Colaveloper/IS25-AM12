package it.polimi.ingsw.galaxytruckers.network.client;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.StatType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import it.polimi.ingsw.galaxytruckers.view.*;
import it.polimi.ingsw.galaxytruckers.view.cli.CliView;
import it.polimi.ingsw.galaxytruckers.view.gui.GuiView;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
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
    private ConfigFactory config;

    public ClientController(VirtualServer server) {
        this.model = new ClientModel();
    }

    public void showInterfaceChoice(VirtualServer server) throws IOException {
        // TODO: consider whether to relocate prints and scans
        System.out.println("Enter \"G\" to switch to the Graphical Interface, or press any other key to continue here");
        if (new Scanner(System.in).nextLine().trim().equalsIgnoreCase("G")) {
            view = new GuiView();
            view.setServer(server);
            view.setModel(model);
            GuiView.strategy = new NicknameChoiceScreen(model);
            Application.launch(GuiView.class); // calls view.setScreen(...)
        } else {
            view = new CliView();
            view.setServer(server);
            view.setModel(model);
            view.setScreen(new NicknameChoiceScreen(model));
        }
    }

    //-----------------------------UPDATES FROM THE SERVER----------------------------------

//    @Override // TODO: DISCUSS
    public void showGameCreation() throws IOException {
        view.setScreen(new GameCreationScreen(model));
    }

    @Override // Tommy approved
    public void updateLobbyPlayers(Map<String, Colors> playerToColor) throws IOException {
        for (Map.Entry<String, Colors> entry : playerToColor.entrySet()) {
            model.setPlayerColor(entry.getKey(), entry.getValue());
        }
        view.setScreen(new LobbyScreen(model));
    }

    public void setMyNickname(String nickname) { // gets called only after legal registration
        model.setMyNickname(nickname);
    }

    public void setupGame(Level level) throws IOException {
        config = switch (level) {
            case TEST -> new TestConfiguarator();
            case FIRST -> throw new IllegalArgumentException("First level is not playable");
            case SECOND -> new SecondConfigurator();
        };
        model.setFlightBoard(config.getLoopLenght(), config.getStartingPositions());
        model.setShipArea(config.getShipArea());
        model.setCoveredComponents(config.getComponentsN());
        view.setScreen(new ShipBuildingScreen(model));
    }


    //-----------------------------BUILDING PHASE----------------------------------

    @Override//Tommy approved
    public void notifyStashComponent(String playerName, List<Integer> stashComponentIds) throws IOException {
        model.setStashedComponents(playerName, stashComponentIds);//todo: add for other players
    }

    @Override//Tommy approved
    public void notifyGrabFromStash(String playerName, int componentId, List<Integer> stashComponentIds) throws IOException {
        model.setStashedComponents(playerName, stashComponentIds);//todo: add for other players
        model.setComponentInHand(playerName, componentId);//todo: add for other players
    }

    @Override//Tommy approved
    public void notifyComponentPositioning(String nickname, int componentId, int direction, Point position) throws IOException {
        model.setComponent(nickname, componentId, direction, position);
        model.clearComponentInHand(nickname);
    }

    @Override//Tommy approved
    public void notifyComponentRejection(String playerName, int componentId) throws IOException {
        model.addRevealedComponent(componentId);
        model.clearComponentInHand(playerName);
    }

    @Override//Tommy approved
    public void notifyFaceDownComponentRequest(String playerName, int componentId) throws IOException {
        model.setComponentInHand(playerName, componentId);
        model.setCoveredComponents(model.coveredComponentNProperty().get()-1);
    }

    @Override//Tommy approved
    public void notifyFaceUpComponentRequest(String playerName, int componentId) throws IOException {
        model.setComponentInHand(playerName, componentId);
        model.removeRevealedComponent(componentId);
    }

//    @Override//Tommy approved
//    public void notifyComponentRejection(String playerName, int componentId, List<Integer> faceUpComponentIds) throws IOException {
//        if (model.isMyNickname(playerName)) {//TODO add for other players
//            model.clearComponentInHand();
//        }
//        model.setRevealedComponent(faceUpComponentIds);
//    }
//
//    @Override//Tommy approved
//    public void notifyFaceDownComponentRequest(String playerName, int componentId, int numFaceDown) throws IOException {
//        if (model.isMyNickname(playerName)) {//TODO add for other players
//            model.setComponentInHand(componentId);
//        }
//        model.setCoveredComponents(numFaceDown);
//    }
//
//    @Override//Tommy approved
//    public void notifyFaceUpComponentRequest(String playerName, int componentId, List<Integer> faceUpComponentIds) throws IOException {
//        if (model.isMyNickname(playerName)) {//TODO add for other players
//            model.setComponentInHand(componentId);
//        }
//        model.setRevealedComponent(faceUpComponentIds);
//    }

    @Override//Tommy approved
    public void notifyPeekForecast(String playerName, int deckIndex) throws IOException {
        model.blockForecast(deckIndex);
    }

    @Override//Tommy approved
    public void notifyReleaseForecast(String playerName, int deckIndex) throws IOException {
        model.freeForecast(deckIndex);
        if (model.isMyNickname(playerName)) {
            view.setScreen(new ShipBuildingScreen(model));
        }
    }

    @Override
    public void sendForecastDeck(List<Integer> deckCardIds) throws IOException {
        model.setForecast(deckCardIds);
        view.setScreen(new ForecastScreen(model));
    }

    @Override
    public void notifyHourglassFlipped(String playerName, boolean isLast) {

    }

    @Override
    public void notifyHourglassEnd() {

    }

    @Override//Tommy approved
    public void notifyCabinUpdate(String nickname, Point position, int crew, CrewType crewType) throws IOException {
        model.setCabinStats(nickname, position, crewType, crew);
    }


    //-----------------------------BOTH BUILDING AND ADVENTURE----------------------------------

    @Override
    public void notifyPlayerPosition(String playerName, int position) throws IOException {
        model.setPlayerToPlace(playerName, position);
    }

    @Override
    public void notifyComponentRemoval(String nickname, Point positionPoint) throws IOException {

    }

    @Override
    public void notifyShipPieceRemoval(String nickname, List<Point> positionPoints) throws IOException {

    }

    @Override
    public void notifyShipStatusUpdate(String nickname, StatType statType, int value) throws IOException {

    }


    //-----------------------------ADVENTURE PHASE----------------------------------

    @Override//Tommy approved
    public void notifyNewCard(int cardId) throws IOException {
        model.setCurrentCard(cardId);
        model.setCurrentPlayerNickname(model.getCurrentLeader());
//        view.setScreen(new NewCardScreen()); // TODO: restore
    }

    public void notifySelection(String nickname, List<Point> cannonsPositions) throws IOException {

    }

    @Override//Tommy approved
    public void notifyCargoHoldUpdate(String nickname, Point position, Map<GoodsType, Integer> goods) throws IOException {
        List<GoodsType> list = new ArrayList<>();
        for(GoodsType goodsType : goods.keySet()) {
            for(int index = 0; index < goods.get(goodsType); index++) {
                list.add(goodsType);
            }
        }
        model.setGoods(nickname, position, list);
    }


    // first time goods are shown on screen
//    @Override
//    public void showPlaceGoods() throws IOException {
////        view.setScreen(new GoodsScreen()); // TODO: restore
//    }

    // planetIndex is an index and starts from 0, UI listing on screen starts from 1
    @Override
    public void choosePlanet(int planetId, List<Point> cargoPositions) throws IOException {
        model.setPlanetGoodBuffer(planetId);
    }

    @Override
    public void updateGoodsBuffer(GoodsType type) throws IOException {
        model.updateGoodsBuffer(type);
    }

    // set current player for any action that involves a decision
//    @Override // TODO: restore
//    public void setCurrentPlayer(String nickname) {
//        model.setCurrentPlayerNickname(nickname);
//    }

    // called for each projectile
    @Override
    public void showProjectile(ProjectileType projectileType, int direction, int roll, List<Point> selectablePoints, List<Point> batteries) throws IOException {
        model.setProjectile(projectileType, direction, roll);
//        view.setScreen(new ProjectilesScreen()); // TODO: restore
    }

    // UPDATE FOR ENDGAME

    @Override
    public void showFinalStats() {
        // model.update
        // view.show(ChosenStrategy)
    }

    @Override
    public void reportError(String details) throws RemoteException {
        // model.update
        // view.show(ChosenStrategy)
    }

}
