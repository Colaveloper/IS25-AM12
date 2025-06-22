package it.polimi.ingsw.galaxytruckers.network.client;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.*;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;

import java.awt.*;
import java.util.*;
import java.util.List;

class ClientControllerTest {

    static VirtualServer server;
    static ClientController controller;

    static String myNickname;
    static Map<String, GameColor> nicknames;
    static int loopLength;
    static List<Integer> startingPositions;
    static Set<Point> shipArea;
    static int coveredComponentsN;
    static List<Integer> faceUpComponents;
    static int userHandComponentId;

    static void setUp() {
        nicknames = new HashMap<>();
        loopLength = 22;
        startingPositions = new ArrayList<>(List.of(2, 4, 5));
        faceUpComponents = new ArrayList<>();
        coveredComponentsN = 44;

        server = new VirtualServer() {
            @Override
            public void registerNickname(String myNickname) {
                System.out.println("FAKE SERVER EVENT: successfully registered nickname " + myNickname);
                nicknames.put(myNickname, GameColor.RED);
                controller.setMyNickname(myNickname);
                ClientControllerTest.myNickname = myNickname;
            }

            @Override
            public void requestNewGame(Level level, int playerN) {
                // TODO: rn these parameters aren't really used!!! define initial game behavior
                lobbySequence();
                buildingSequence();
            }

            @Override
            public void requestRandComponent() {
                userHandComponentId = (int) (Math.random()*33);
                System.out.println("FAKE SERVER EVENT: you took a covered component");
                controller.notifyEvent(new RequestFaceDownComponentEvent(myNickname, userHandComponentId));
            }

            @Override
            public void requestComponent(int componentId) {
//                faceUpComponents.removeIf(c -> c.getId() == componentId); // can't be properly mocked up here
                userHandComponentId = componentId;
                System.out.println("FAKE SERVER EVENT: the requested face up component was successfully taken");
                controller.notifyEvent(new RequestFaceDownComponentEvent(myNickname, componentId));
            }


            @Override
            public void grabStashedComponent(int stashIndex) {
                System.out.println("FAKE SERVER EVENT: grab stashed component");
                controller.notifyEvent(new GrabStashedComponentEvent(myNickname, 0));
            }

            @Override
            public void stashComponent() {
                System.out.println("FAKE SERVER EVENT: stashed component");
                controller.notifyEvent(new StashComponentEvent(myNickname));
            }

            @Override
            public void grabPlacedComponent() {
                System.out.println("FAKE SERVER EVENT: grab placed component");
                controller.notifyEvent(new GrabPlacedComponentEvent(myNickname));
            }

            @Override
            public void placeComponent(Point point, Direction orientation) {
                System.out.println("FAKE SERVER EVENT: the current component was successfully positioned where requested");
                controller.notifyEvent(new PlaceComponentEvent(myNickname, point, orientation));
            }

            @Override
            public void placeShipOnFlightBoard(int startingPosition) {
                System.out.println("FAKE SERVER EVENT: your ship was successfully positioned where requested");
                controller.notifyEvent(new FlightBoardUpdateEvent("OtherPlayer1", 4));
            }

            @Override
            public void placeShipOnFlightBoard() {
                System.out.println("FAKE SERVER EVENT: your ship was successfully positioned on the FlightBoard");
                controller.notifyEvent(new FlightBoardUpdateEvent("OtherPlayer1", 4));
            }

            @Override
            public void flipHourglass() {

            }

            @Override
            public void rejectComponent() {
                System.out.println("FAKE SERVER EVENT: rejected component");
                controller.notifyEvent(new RejectComponentEvent(myNickname));
            }

            @Override
            public void releaseForecast() {
                System.out.println("FAKE SERVER EVENT: forecast unlocked");
                controller.notifyEvent(new ReleaseForecastEvent(myNickname, 2));
            }

            @Override
            public void acquireForecast(int deckIndex) {
                System.out.println("FAKE SERVER EVENT: forecast acquired");
                controller.notifyEvent(new ForecastDetailsEvent(myNickname,List.of(3, 4, 5)));
            }

            @Override
            public void removeComponent(Point point) {
                System.out.println("FAKE SERVER EVENT: single component removed");
                controller.notifyEvent(new RemoveComponentEvent(myNickname, point));
            }

            @Override
            public void chooseShipPiece(int pieceIndex) {
                System.out.println("FAKE SERVER EVENT: one ship piece kept, others removed");
                controller.notifyEvent(new ShipPieceRemoveEvent(myNickname, pieceIndex));
            }

            @Override
            public void initializeCabin(Point point, CrewType crewType) {

            }

            @Override
            public void activateComponent(Point point) {

            }

            @Override
            public void loseCrew(Point point) {

            }

            @Override
            public void grabReward() {

            }

            @Override
            public void placeGoods(Point point, GoodsType goodsType) {

            }

            @Override
            public void removeGoods(Point point, GoodsType goodsType) {

            }

            @Override
            public void loseGoods(Point point) {

            }

            @Override
            public void useBattery(Point point) {
                controller.notifyEvent(new UseBatteryEvent(myNickname, point));//todo add num batteries?
            }

            @Override
            public void choosePlanet(int choice) {

            }

            @Override
            public void goNext() {

            }

            @Override
            public void giveUp() {

            }

            @Override
            public void drawCard() {
                controller.notifyEvent(new NewCardEvent(7));
            }

            @Override
            public void joinLobby(UUID lobbyID) {
                lobbySequence();
                buildingSequence();
            }

            @Override
            public void leaveLobby() {

            }
        };

        ClientModel model = new ClientModel();

        controller = new ClientController();
        controller.setServer(server);
        controller.setModel(model);
//        controller.setView(new CliView(controller, model));
    }

    public static void lobbySequence() {
//        controller.updateLobbyPlayers(nicknames);
//
//        System.out.println("FAKE SERVER EVENT: another player joined, lobby is complete");
//        nicknames.put("OtherPlayer1", GameColor.BLUE);
//        controller.updateLobbyPlayers(nicknames);
//
//        controller.notifyNewGame(Level.TEST, 2);
    }

    public static void buildingSequence() {
        // Simulated server update thread
        new Thread(() -> {
            try {
//                System.out.println("FAKE SERVER EVENT: OtherPlayer1 took a covered component");
//                controller.notifyFaceDownComponentRequest("OtherPlayer1", 10);
//
//                Thread.sleep(1000);
//                System.out.println("FAKE SERVER EVENT: OtherPlayer1 rejected component");
//                faceUpComponents.add(10);
//                controller.notifyComponentRejection("OtherPlayer1",10);
//
//                Thread.sleep(1000);
//                System.out.println("FAKE SERVER EVENT: OtherPlayer1 rejected component");
//                faceUpComponents.add(11);
//                controller.notifyComponentRejection("OtherPlayer1",11);
//
//                //Thread.sleep(1000);
//                System.out.println("FAKE SERVER EVENT: OtherPlayer1 took the component back");
//                faceUpComponents.add(10);
//                controller.notifyFaceUpComponentRequest("OtherPlayer1",10);
//
//                //Thread.sleep(1000);
//                System.out.println("FAKE SERVER EVENT: OtherPlayer1's current component was successfully positioned where requested");
//                controller.notifyComponentPositioning("OtherPlayer1", 10, 0, new Point(5, 7));
//                controller.notifyComponentPositioning("OtherPlayer1", 10, 0, new Point(5, 8));
//                controller.notifyComponentPositioning("OtherPlayer1", 10, 0, new Point(6, 7));
//                controller.notifyComponentPositioning("OtherPlayer1", 10, 0, new Point(6, 8));
//                controller.notifyComponentPositioning("OtherPlayer1", 120, 0, new Point(6, 9));
//
//                //Thread.sleep(1000);
//                System.out.println("FAKE SERVER EVENT: your current component was successfully positioned where requested");
//                controller.notifyComponentPositioning(myNickname, 10, 0, new Point(5, 7));
//                controller.notifyComponentPositioning(myNickname, 10, 0, new Point(5, 8));
//                controller.notifyComponentPositioning(myNickname, 10, 0, new Point(6, 7));
//                controller.notifyComponentPositioning(myNickname, 10, 0, new Point(6, 8));
//                controller.notifyComponentPositioning(myNickname, 120, 0, new Point(6, 9));
//                //Thread.sleep(1000);
//                System.out.println("FAKE SERVER EVENT: OtherPlayer1 stashed component");
//                controller.notifyStashComponent("OtherPlayer1", List.of(5, 0));
//
//                //Thread.sleep(1000);
//                System.out.println("FAKE SERVER EVENT: OtherPlayer1 grabbed from stashed component");
//                controller.notifyGrabFromStash("OtherPlayer1", 5, List.of(0, 0));
//
//                //Thread.sleep(1000);
//                System.out.println("FAKE SERVER EVENT: OtherPlayer1 is watching forecast");
//                controller.notifyOtherPeekForecast("OtherPlayer1", 2);
//
//                //Thread.sleep(1000);
//                System.out.println("FAKE SERVER EVENT: OtherPlayer1 stopped watching forecast");
//                controller.notifyReleaseForecast("OtherPlayer1", 2);

                //Thread.sleep(1000);
//                System.out.println("FAKE SERVER EVENT: you picked up a forecast deck");
//                controller.sendForecastDeck(List.of(2, 4, 5));

                //Thread.sleep(1000);
//                System.out.println("FAKE SERVER EVENT: yours and OtherPlayer1's ships are invalid");
//                controller.notifyInvalidShipsUpdate(List.of("OtherPlayer1", myNickname));

                //Thread.sleep(1000);
//                System.out.println("FAKE SERVER EVENT: OtherPlayer1's ship is invalid");
//                controller.notifyInvalidShipsUpdate(List.of("OtherPlayer1"));

                //Thread.sleep(1000);
//                System.out.println("FAKE SERVER EVENT: OtherPlayer1 ship is not connected");
//                controller.showShipPieces("OtherPlayer1", List.of(
//                        Set.of(new Point(5, 7), new Point(5, 8)),
//                        Set.of(new Point(6, 7), new Point(6, 8), new Point(6, 9))
//                ));

                //Thread.sleep(1000);
//                System.out.println("FAKE SERVER EVENT: OtherPlayer1's ship was successfully positioned where requested");
//                controller.notifyPlayerPosition("OtherPlayer1", 1);
//
//                //Thread.sleep(1000);
//                System.out.println("FAKE SERVER EVENT: OtherPlayer1's spent a battery");
//                controller.changeBatteriesOnComponent("OtherPlayer1", new Point(6, 9), 1);

                //Thread.sleep(1000);
//                System.out.println("FAKE SERVER EVENT: your ship is not connected");
//                controller.showShipPieces(myNickname, List.of(
//                        Set.of(new Point(5, 7), new Point(5, 8)),
//                        Set.of(new Point(6, 7), new Point(6, 8), new Point(6, 9))
//                ));

                Thread.sleep(1000);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    public static void main(String[] args) {
        setUp();
        System.out.println("use this nickname or it breaks: qwe");
    }
}