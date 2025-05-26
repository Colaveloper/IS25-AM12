package it.polimi.ingsw.galaxytruckers.network.client;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.StatType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.view.*;
import it.polimi.ingsw.galaxytruckers.view.CliView;
import it.polimi.ingsw.galaxytruckers.view.enums.ProjectileType;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.GuiView;
import it.polimi.ingsw.galaxytruckers.view.model.Player;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputFilter;
import java.util.*;
import java.util.List;

public class ClientController implements ClientControllerInterface, ControllerToServer {
    private final ClientModel model;
    private VirtualServer server;
    private View view;
    private ConfigFactory config;

    public ClientController(VirtualServer server) {
        this.server = server;
        this.model = new ClientModel();
    }

    public ClientController() {
        this.model = new ClientModel();
    }

    public void setServer(VirtualServer server) {
        this.server = server;
    }

    public void showInterfaceChoice() {
        // TODO: consider whether to relocate prints and scans
        System.out.println("Enter \"G\" to switch to the Graphical Interface, or press any other key to continue here");
        if (new Scanner(System.in).nextLine().trim().equalsIgnoreCase("G")) {
            view = new GuiView(this, model);
//            view.setController(this);
//            view.setModel(model);
//            GuiView.screen = new NicknameChoiceScreen().getGuiScreen(model, this);
//            Application.launch(GuiView.class); // calls view.setScreen(...)
        } else {
            view = new CliView(this, model);
//            view = new CliView();
//            view.setController(this);
//            view.setModel(model);
//            view.setScreen(new NicknameChoiceScreen());
        }
    }

    //    //-----------------------------UPDATES FROM THE SERVER----------------------------------
//
    @Override // TODO: DISCUSS
    public void showGameCreation() {
        view.setScreen(model.getGame().getCurrentState());
    }

    //
//    @Override // Tommy approved
//    public void updateLobbyPlayers(Map<String, FourColors> playerToColor) {
//        for (Map.Entry<String, FourColors> entry : playerToColor.entrySet()) {
//            model.setPlayerColor(entry.getKey(), entry.getValue());
//        }
//        view.setScreen(new LobbyScreen());
//    }
//
    @Override
    public void setMyNickname(String nickname) { // gets called only after legal registration
        model.setPlayer(new Player(nickname));
        view.setScreen(model.getGame().getCurrentState());
    }

    @Override
    public void joinLobby(UUID lobbyID) {
        server.joinLobby(lobbyID);
    }


    @Override
    public void requestNewGame(Level level, int playersN) {
        server.requestNewGame(level, playersN);
    }

//    @Override
//    public void notifyNewGame(Level level, int playersN) {
//        config = switch (level) {
//            case TEST -> new TestConfiguarator();
//            case FIRST -> throw new IllegalArgumentException("First level is not playable");
//            case SECOND -> new SecondConfigurator();
//        };
//        model.setFlightBoard(config.getLoopLength(), config.getStartingPositions());
//        model.setShipArea(config.getShipArea());
//        model.setCoveredComponents(config.getComponentsN());
//        view.setScreen(new ShipBuildingScreen(config));
//    }
//
//
//    //-----------------------------BUILDING PHASE----------------------------------
//
//    @Override//Tommy approved
//    public void notifyStashComponent(String playerName, List<Integer> stashComponentIds) {
//        runAndInterceptIOE(()->model.stashComponents(playerName, stashComponentIds));
//    }
//
//    @Override//Tommy approved
//    public void notifyGrabFromStash(String playerName, int componentId, List<Integer> stashComponentIds) {
//        runAndInterceptIOE(()->model.unstashComponents(playerName, stashComponentIds));
//        runAndInterceptIOE(()->model.setComponentInHand(playerName, componentId));
//    }
//
//    @Override//Tommy approved
//    public void notifyComponentPositioning(String nickname, int componentId, int direction, Point position) {
//        runAndInterceptIOE(()->model.setComponent(nickname, componentId, direction, position));
//    }
//
//    @Override//Tommy approved
//    public void notifyComponentRejection(String playerName, int componentId) {
//        runAndInterceptIOE(()->model.addRevealedComponent(componentId));
//        model.clearUnwelded(playerName);
//    }
//
//    @Override//Tommy approved
//    public void notifyFaceDownComponentRequest(String playerName, int componentId) {
//        runAndInterceptIOE(()->model.setComponentInHand(playerName, componentId));
//        model.setCoveredComponents(model.coveredComponentNProperty().get()-1);
//    }
//
//    @Override//Tommy approved
//    public void notifyFaceUpComponentRequest(String playerName, int componentId) {
//        runAndInterceptIOE(()->model.setComponentInHand(playerName, componentId));
//        model.removeRevealedComponent(componentId);
//    }
//
//    @Override//Tommy approved
//    public void notifyPeekForecast(String playerName, int deckIndex) {
//        runAndInterceptIOE(()->model.blockForecast(deckIndex));
//    }
//
//    @Override//Tommy approved
//    public void notifyReleaseForecast(String playerName, int deckIndex) {
//        runAndInterceptIOE(()->model.freeForecast(deckIndex));
//        if (model.isMyNickname(playerName)) {
//            view.setScreen(new ShipBuildingScreen(config));
//        }
//    }
//
//    @Override
//    public void sendForecastDeck(List<Integer> deckCardIds) {
//        model.setForecast(deckCardIds);
//        model.setExistsUnweldedComponent(model.getMyNickname(), false); //quote:  picking up a pile welds your most recent component to your ship
//        view.setScreen(new ForecastScreen());
//    }
//
//    @Override
//    public void notifyHourglassFlipped(String playerName, boolean isLast) {
//
//    }
//
//    @Override
//    public void notifyHourglassEnd() {
//
//    }
//
//    @Override//Tommy approved
//    public void notifyCabinUpdate(String nickname, Point position, int crew, CrewType crewType) {
//        if(nickname.equals(model.getMyNickname()) && crewType!= CrewType.HUMAN) {
//            runAndInterceptIOE(()->model.placeAliens(crewType, position));//happens only in building
//        }
//        runAndInterceptIOE(()->model.setCabinStats(nickname, position, crewType, crew));
//    }
//
//    @Override//called once at the start of the phase
//    public void notifyCrewInitialization(Map<String, Map<CrewType, List<Point>>> playerToCabin) {
//        if(playerToCabin.containsKey(model.getMyNickname())){
//            model.setIsValid(false);
//            model.setUnplacedCrew(playerToCabin.get(model.getMyNickname()));
//        }
//        view.setScreen(new CrewInitialization());
//    }
//
//
//    //-----------------------------BOTH BUILDING AND ADVENTURE----------------------------------
//
//    @Override
//    public void notifyPlayerPosition(String playerName, int position) {
//        model.setPlayerToPlace(playerName, position);
//    }
//
//    @Override
//    public void notifyComponentRemoval(String playerName, Point position) {
//        runAndInterceptIOE(()->model.removeComponent(position, playerName));
//    }
//
//    @Override
//    public void notifyShipPieceRemoval(String nickname, List<Point> positionPoints) {//use ONLY for disconnected ship
//        if(model.isMyNickname(nickname)) {
//            model.setIsValid(true);
//        }
//        model.resetAllSelections(nickname);
//        for (Point p : positionPoints) {
//            runAndInterceptIOE(()->model.removeComponent(p, nickname));
//        }
//    }
//
//    @Override
//    public void showShipPieces(Map<String, List<Set<Point>>> brokenShips) {
//        for(String nickname : brokenShips.keySet()) {
//            model.setSelectableShipPieces(nickname, brokenShips.get(nickname));
//            if(model.isMyNickname(nickname)) {
//                model.setIsValid(false);
//            }
//        }
//        view.setScreen(new ShipPieceChoiceScreen());
//    }
//
//    @Override
//    public void notifyInvalidShipsUpdate(List<String> invalidPlayers) {
//        model.setIsValid(!invalidPlayers.contains(model.getMyNickname()));
//        for(String playerName : model.getNicknames()) {
//            if(!invalidPlayers.contains(playerName)) {
//                model.resetAllSelections(playerName);
//            }
//        }
//        view.setScreen(new ValidationScreen());
//    }
//
//    @Override
//    public void notifyShipStatusUpdate(String nickname, StatType statType, int value) {
//        model.setStat(nickname, statType, value);
//    }
//
//
//    //-----------------------------ADVENTURE PHASE----------------------------------
//
//    @Override//Tommy approved
//    public void notifyNewCard(int cardId) {
//        runAndInterceptIOE(()->model.setCurrentCard(cardId));
//        model.setCurrentPlayerNickname(model.getCurrentLeader());
//        view.setScreen(new NewCardScreen());
//    }
//
//    @Override
//    public void notifyComponentActivation(String playerName, Point position){
//        runAndInterceptIOE(()->model.activateComponent(playerName, position));
//
//    }
//
//    @Override
//    public void notifySelection(String nickname, List<Point> cannonsPositions, List<Point> batteryPositions) {//todo add batteries list
//        model.setCurrentPlayerNickname(nickname);
//        model.setSelectablePoints(nickname, cannonsPositions);
//        model.setSelectableBatteries(nickname, batteryPositions);
//        view.setScreen(new PointSelectionScreen());
//    }
//
//    @Override
//    public void changeBatteriesOnComponent(String nickname, Point batteryComponent, int batteries) {
//        runAndInterceptIOE(()->model.setBatteriesOnComponent(nickname, batteryComponent, batteries));
//    }
//
//    @Override//Tommy approved
//    public void notifyCargoHoldUpdate(String nickname, Point position, Map<GoodsType, Integer> goods) {
//        List<GoodsType> list = new ArrayList<>();
//        for(GoodsType goodsType : goods.keySet()) {
//            for(int index = 0; index < goods.get(goodsType); index++) {
//                list.add(goodsType);
//            }
//        }
//        runAndInterceptIOE(()->model.setGoods(nickname, position, list));
//    }
//
//    // planetIndex is an index and starts from 0, UI listing on screen starts from 1
//    @Override
//    public void notifyLandOnPlanet(String nickname, int planetId) {
//        //todo block planet with that id
//    }
//
//    //first call in place goods phase
//    @Override
//    public void notifyGrabGoodsState(String nickname, Map<GoodsType, Integer> goods, List<Point> cargoPositions){
//        //todo setup goodsbuffer
//        model.setSelectablePoints(nickname, cargoPositions);
//        view.setScreen(new GoodsScreen());
//    }
//
//    @Override
//    public void updateGoodsBuffer(boolean adding, GoodsType type) {
//        runAndInterceptIOE(()->model.updateGoodsBuffer(adding, type));
//    }
//
//    // set current player for any action that involves a decision

    @Override
    public void updateLobbyPlayers(Map<String, GameColor> playerToColor) {

    }

    @Override
    public void notifyStashComponent(String playerName, List<Integer> stashComponentIds) {

    }

    @Override
    public void notifyGrabFromStash(String playerName, int componentId, List<Integer> stashComponentIds) {

    }

    @Override
    public void notifyComponentPositioning(String nickname, int componentId, int direction, Point position) {

    }

    @Override
    public void notifyComponentRejection(String playerName, int componentId) {

    }

    @Override
    public void notifyFaceDownComponentRequest(String playerName, int componentId) {

    }

    @Override
    public void notifyFaceUpComponentRequest(String playerName, int componentId) {

    }

    @Override
    public void notifyPeekForecast(String playerName, int deckIndex) {

    }

    @Override
    public void notifyReleaseForecast(String playerName, int deckIndex) {

    }

    @Override
    public void sendForecastDeck(List<Integer> deckCardIds) {

    }

    @Override
    public void notifyHourglassFlipped(String playerName, boolean isLast) {

    }

    @Override
    public void notifyHourglassEnd() {

    }

    @Override
    public void notifyCabinUpdate(String nickname, Point position, int crew, CrewType crewType) {

    }

    @Override
    public void notifyPlayerPosition(String playerName, int position) {

    }

    @Override
    public void notifyShipPieceRemoval(String nickname, List<Point> positionPoints) {

    }

    @Override
    public void notifyComponentRemoval(String playerName, Point position) {

    }

    @Override
    public void showShipPieces(Map<String, List<Set<Point>>> brokenShips) {

    }

    @Override
    public void notifyInvalidShipsUpdate(List<String> invalidPlayers) {

    }

    @Override
    public void notifyShipStatusUpdate(String nickname, StatType statType, int value) {

    }

    @Override
    public void notifyNewCard(int cardId) {

    }

    @Override
    public void notifySelection(String nickname, List<Point> cannonsPositions, List<Point> batteryPositions) {

    }

    @Override
    public void notifyComponentActivation(String playerName, Point position) {

    }

    @Override
    public void changeBatteriesOnComponent(String nickname, Point batteryComponent, int batteries) {

    }

    @Override
    public void notifyGrabGoodsState(String nickname, Map<GoodsType, Integer> goods, List<Point> cargoPositions) {

    }

    @Override
    public void notifyCrewInitialization(Map<String, Map<CrewType, List<Point>>> playerToCabin) {

    }

    @Override
    public void notifyCargoHoldUpdate(String nickname, Point position, Map<GoodsType, Integer> goods) {

    }

    @Override
    public void notifyLandOnPlanet(String nickname, int planetId) {

    }

    @Override
    public void updateGoodsBuffer(boolean adding, GoodsType type) {

    }

    @Override
    public void showProjectile(String nickname, ProjectileType projectileType, int direction, int roll, List<Point> selectablePoints, List<Point> batteries) {

    }

    @Override
    public void showFinalStats() {

    }

    /// /    @Override // TODO: restore
    /// /    public void setCurrentPlayer(String nickname) {
    /// /        model.setCurrentPlayerNickname(nickname);
    /// /    }
//
//    // called for each projectile
//    @Override
//    public void showProjectile(String nickname, ProjectileType projectileType, int direction, int roll, List<Point> selectablePoints, List<Point> batteries) {
//        model.setProjectile(projectileType, direction, roll);
//        model.setCurrentPlayerNickname(nickname);
//        view.setScreen(new ProjectileScreen());
//    }
//
//    // UPDATE FOR ENDGAME
//
//    @Override
//    public void showFinalStats() {
//        // model.update
//        // view.show(ChosenStrategy)
//    }


//--------------------------------------------SERVER CALLS-------------------------------------------------------------------
    @Override
    public void reportError(String details) {
        System.out.println("Error: " + details);
        // view.show(ChosenStrategy)
    }

    @Override
    public void notifyNewGame(Level level, int i) {

    }

    @Override
    public void goNext() {
        try {
            server.goNext();
        } catch (IllegalArgumentException e) {
            reportError("could not go on with card");
        }
    }

    @Override
    public void chooseShipPiece(int choice) {
        try {
            server.chooseShipPiece(choice);
        } catch (IllegalArgumentException e) {
            reportError("couldn't choose ship piece");
        }
    }

    @Override
    public void activateComponent(Point point) {
        try {
            server.activateComponent(point);
        } catch (IllegalArgumentException e) {
            reportError("couldn't activate component");
        }
    }


    @Override
    public void removeComponent(Point point) {
        try {
            server.removeComponent(point);
        } catch (IllegalArgumentException e) {
            reportError("couldn't remove ship piece");
        }
    }

    @Override
    public void useBattery(Point point) {
        try {
            server.useBattery(point);
        } catch (IllegalArgumentException e) {
            reportError("couldn't spend battery");
        }
    }

    @Override
    public void initializeCabin(Point point, CrewType crewType) {
        try {
            server.initializeCabin(point, crewType);
        } catch (IllegalArgumentException e) {
            reportError("couldn't initialize cabin");
        }
    }

    @Override
    public void placeShipOnFlightboard(int startingPosition) {
        try {
            server.placeShipOnFlightBoard(startingPosition);
        } catch (IllegalArgumentException e) {
            reportError("couldn't place on flightboard");
        }
    }

    @Override
    public void registerNickname(String nickname) throws IllegalArgumentException {
        server.registerNickname(nickname);
    }

    @Override
    public void flipHourglass() {
        try {
            server.flipHourglass();
        } catch (IllegalArgumentException e) {
            reportError("cannot flip hourglass");
        }
    }

    @Override
    public void requestRandComponent() {
        try {
            server.requestRandComponent();
        } catch (IllegalArgumentException e) {
            reportError("random component not available");
        }
    }

    @Override
    public void requestComponent(int componentId) {
        try {
            server.requestComponent(componentId);
        } catch (IllegalArgumentException e) {
            reportError("component of id " + componentId + " not available");
        }
    }

    @Override
    public void stashComponent() {
        try {
            server.stashComponent();
        } catch (IllegalArgumentException e) {
            reportError("cannot stash component");
        }
    }

    @Override
    public void grabStashedComponent(int index) {
        try {
            server.grabStashedComponent(index);
        } catch (IllegalArgumentException e) {
            reportError("cannot grab stashed component");
        }
    }

    @Override
    public void acquireForecast(int index) {
        try {
            server.acquireForecast(index);
        } catch (IllegalArgumentException e) {
            reportError("cannot acquire forecast");
        }
    }

    @Override
    public void releaseForecast() {
        try {
            server.releaseForecast();
        } catch (IllegalArgumentException e) {
            reportError("cannot release forecast");
        }
    }

    @Override
    public void rejectComponent() {
        try {
            server.rejectComponent();
        } catch (IllegalArgumentException e) {
            reportError("cannot reject component");
        }
    }

    @Override
    public void placeComponent(Point point, int orientation) {
        try {
            server.placeComponent(point, orientation);
        } catch (IllegalArgumentException e) {
            reportError("cannot place component");
        }
    }

    @Override
    public void drawCard(){
        try{
            server.drawCard();
        } catch(IllegalArgumentException e){
            reportError("Cannot draw new card");
        }
    }

    private void runAndInterceptIOE(RunnableWithIOE action) {
        try {
            action.run();
        } catch (IOException e) {
            reportError("IO Exception: " + e.getMessage());
        }
    }
}

@FunctionalInterface
interface RunnableWithIOE {
    void run() throws IOException;
}
