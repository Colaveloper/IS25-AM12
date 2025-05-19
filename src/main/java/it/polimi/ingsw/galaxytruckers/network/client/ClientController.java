package it.polimi.ingsw.galaxytruckers.network.client;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.FourColors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.StatType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.view.*;
import it.polimi.ingsw.galaxytruckers.view.CliView;
import it.polimi.ingsw.galaxytruckers.view.GuiView;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.screens.*;
import it.polimi.ingsw.galaxytruckers.view.viewEnums.ProjectileType;
import javafx.application.Application;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class ClientController implements ClientControllerInterface {
    private final ClientModel model;
    private final VirtualServer server;
    private View view;
    private ConfigFactory config;

    public ClientController(VirtualServer server) {
        this.server = server;
        this.model = new ClientModel();
    }

    public void showInterfaceChoice() {
        // TODO: consider whether to relocate prints and scans
        System.out.println("Enter \"G\" to switch to the Graphical Interface, or press any other key to continue here");
        if (new Scanner(System.in).nextLine().trim().equalsIgnoreCase("G")) {
            view = new GuiView();
            view.setController(this);
            view.setModel(model);
            GuiView.screen = new NicknameChoiceScreen().getGuiScreen(model, this);
            Application.launch(GuiView.class); // calls view.setScreen(...)
        } else {
            view = new CliView();
            view.setController(this);
            view.setModel(model);
            view.setScreen(new NicknameChoiceScreen());
        }
    }

    //-----------------------------UPDATES FROM THE SERVER----------------------------------

//    @Override // TODO: DISCUSS
    public void showGameCreation() {
        view.setScreen(new GameCreationScreen());
    }

    @Override // Tommy approved
    public void updateLobbyPlayers(Map<String, FourColors> playerToColor) {
        for (Map.Entry<String, FourColors> entry : playerToColor.entrySet()) {
            model.setPlayerColor(entry.getKey(), entry.getValue());
        }
        view.setScreen(new LobbyScreen());
    }

    public void setMyNickname(String nickname) { // gets called only after legal registration
        model.setMyNickname(nickname);
        view.setScreen(new JoinOrCreateScreen());
    }

    public void joinLobby(UUID lobbyID) {
        server.joinLobby(lobbyID);
    }

    public void requestNewGame(Level level, int playersN) {
        server.requestNewGame(level, playersN);
    }

    @Override
    public void notifyNewGame(Level level, int playersN) {
        config = switch (level) {
            case TEST -> new TestConfiguarator();
            case FIRST -> throw new IllegalArgumentException("First level is not playable");
            case SECOND -> new SecondConfigurator();
        };
        model.setFlightBoard(config.getLoopLenght(), config.getStartingPositions());
        model.setShipArea(config.getShipArea());
        model.setCoveredComponents(config.getComponentsN());
        view.setScreen(new ShipBuildingScreen());
    }


    //-----------------------------BUILDING PHASE----------------------------------

    @Override//Tommy approved
    public void notifyStashComponent(String playerName, List<Integer> stashComponentIds) {
        runAndInterceptIOE(()->model.setStashedComponents(playerName, stashComponentIds));
        runAndInterceptIOE(()->model.setComponentInHand(playerName, 0));
    }

    @Override//Tommy approved
    public void notifyGrabFromStash(String playerName, int componentId, List<Integer> stashComponentIds) {
        runAndInterceptIOE(()->model.setStashedComponents(playerName, stashComponentIds));
        runAndInterceptIOE(()->model.setComponentInHand(playerName, componentId));
    }

    @Override//Tommy approved
    public void notifyComponentPositioning(String nickname, int componentId, int direction, Point position) {
        runAndInterceptIOE(()->model.setComponent(nickname, componentId, direction, position));
        model.clearComponentInHand(nickname);
    }

    @Override//Tommy approved
    public void notifyComponentRejection(String playerName, int componentId) {
        runAndInterceptIOE(()->model.addRevealedComponent(componentId));
        model.clearComponentInHand(playerName);
    }

    @Override//Tommy approved
    public void notifyFaceDownComponentRequest(String playerName, int componentId) {
        runAndInterceptIOE(()->model.setComponentInHand(playerName, componentId));
        model.setCoveredComponents(model.coveredComponentNProperty().get()-1);
    }

    @Override//Tommy approved
    public void notifyFaceUpComponentRequest(String playerName, int componentId) {
        runAndInterceptIOE(()->model.setComponentInHand(playerName, componentId));
        model.removeRevealedComponent(componentId);
    }

    @Override//Tommy approved
    public void notifyPeekForecast(String playerName, int deckIndex) {
        runAndInterceptIOE(()->model.blockForecast(deckIndex));
    }

    @Override//Tommy approved
    public void notifyReleaseForecast(String playerName, int deckIndex) {
        runAndInterceptIOE(()->model.freeForecast(deckIndex));
        if (model.isMyNickname(playerName)) {
            view.setScreen(new ShipBuildingScreen());
        }
    }

    @Override
    public void sendForecastDeck(List<Integer> deckCardIds) {
        model.setForecast(deckCardIds);
        view.setScreen(new ForecastScreen());
    }

    @Override
    public void notifyHourglassFlipped(String playerName, boolean isLast) {

    }

    @Override
    public void notifyHourglassEnd() {

    }

    @Override//Tommy approved
    public void notifyCabinUpdate(String nickname, Point position, int crew, CrewType crewType) {
        runAndInterceptIOE(()->model.setCabinStats(nickname, position, crewType, crew));
    }


    //-----------------------------BOTH BUILDING AND ADVENTURE----------------------------------

    @Override
    public void notifyPlayerPosition(String playerName, int position) {
        model.setPlayerToPlace(playerName, position);
    }

    @Override
    public void notifyComponentsRemoval(String nickname, List<Point> positionPoints) {
        for (Point p : positionPoints) {
            runAndInterceptIOE(()->model.removeComponent(p, nickname));
        }
    }

    @Override
    public void showShipPieces(String nickname, List<Set<Point>> shipPieces) {
        model.setSelectableShipPieces(nickname, shipPieces);
        model.setIsValid(!model.isMyNickname(nickname));
        view.setScreen(new ShipPieceChoiceScreen());
    }

    @Override
    public void notifyInvalidShipsUpdate(List<String> invalidPlayers) {
        model.setIsValid(!invalidPlayers.contains(model.getMyNickname()));
        view.setScreen(new ValidationScreen());
    }

    @Override
    public void notifyShipStatusUpdate(String nickname, StatType statType, int value) {
        model.setStat(nickname, statType, value);
    }


    //-----------------------------ADVENTURE PHASE----------------------------------

    @Override//Tommy approved
    public void notifyNewCard(int cardId) {
        runAndInterceptIOE(()->model.setCurrentCard(cardId));
        model.setCurrentPlayerNickname(model.getCurrentLeader());
//        view.setScreen(new NewCardScreen()); // TODO: restore
    }

    public void notifySelection(String nickname, List<Point> cannonsPositions) {

    }

    //todo added recently
    public void changeBatteriesOnComponent(String nickname, Point batteryComponent, int batteries) {
        runAndInterceptIOE(()->model.setBatteriesOnComponent(nickname, batteryComponent, batteries));
    }

    @Override//Tommy approved
    public void notifyCargoHoldUpdate(String nickname, Point position, Map<GoodsType, Integer> goods) {
        List<GoodsType> list = new ArrayList<>();
        for(GoodsType goodsType : goods.keySet()) {
            for(int index = 0; index < goods.get(goodsType); index++) {
                list.add(goodsType);
            }
        }
        runAndInterceptIOE(()->model.setGoods(nickname, position, list));
    }


    // first time goods are shown on screen
//    @Override
//    public void showPlaceGoods() {
////        view.setScreen(new GoodsScreen()); // TODO: restore
//    }

    // planetIndex is an index and starts from 0, UI listing on screen starts from 1
    @Override
    public void choosePlanet(int planetId, List<Point> cargoPositions) {
        model.setPlanetGoodBuffer(planetId);
    }

    @Override
    public void updateGoodsBuffer(GoodsType type) {
        runAndInterceptIOE(()->model.updateGoodsBuffer(type));
    }

    // set current player for any action that involves a decision
//    @Override // TODO: restore
//    public void setCurrentPlayer(String nickname) {
//        model.setCurrentPlayerNickname(nickname);
//    }

    // called for each projectile
    @Override
    public void showProjectile(ProjectileType projectileType, int direction, int roll, List<Point> selectablePoints, List<Point> batteries) {
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
    public void reportError(String details) {
        // view.show(ChosenStrategy)
    }

    //@Override
    public void registerNickname(String nickname) {
        try {
            server.registerNickname(nickname);
        } catch (IllegalArgumentException e) {
            reportError("Nickname "+nickname+" is unavailable");
        }
    }

    //@Override
    public void flipHourglass() {
        try {
            server.flipHourglass();
        } catch (IllegalArgumentException e) {
            reportError("cannot flip hourglass");
        }
    }

    //@Override
    public void requestRandComponent() {
        try {
            server.requestRandComponent();
        } catch (IllegalArgumentException e) {
            reportError("random component not available");
        }
    }

    //@Override
    public void requestComponent(int index) {
        try {
            server.requestComponent(index);
        } catch (IllegalArgumentException e) {
            reportError("component of index " + index + " not available");
        }
    }

    //@Override
    public void stashComponent() {
        try {
            server.stashComponent();
        } catch (IllegalArgumentException e) {
            reportError("cannot stash component");
        }
    }

    //@Override
    public void grabStashedComponent(int index) {
        try {
            server.grabStashedComponent(index);
        } catch (IllegalArgumentException e) {
            reportError("cannot grab stashed component");
        }
    }

    //@Override
    public void acquireForecast(int index) {
        try {
            server.acquireForecast(index);
        } catch (IllegalArgumentException e) {
            reportError("cannot acquire forecast");
        }
    }

    public void releaseForecast() {
        try {
            server.releaseForecast();
        } catch (IllegalArgumentException e) {
            reportError("cannot release forecast");
        }
    }

    //@Override
    public void rejectComponent() {
        try {
            server.rejectComponent();
        } catch (IllegalArgumentException e) {
            reportError("cannot reject component");
        }
    }

    //@Override
    public void placeComponent(Point point, int orientation) {
        try {
            server.placeComponent(point, orientation);
        } catch (IllegalArgumentException e) {
            reportError("cannot place component");
        }
    }

    private void runAndInterceptIOE(RunnableWithIOE action) {
        try {
            action.run();
        } catch (IOException e) {
            reportError("IO Exception: "+e.getMessage());
        }
    }
}

@FunctionalInterface
interface RunnableWithIOE {
    void run() throws IOException;
}
