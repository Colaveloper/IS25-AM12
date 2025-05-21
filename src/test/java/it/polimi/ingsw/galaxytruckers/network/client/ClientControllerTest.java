package it.polimi.ingsw.galaxytruckers.network.client;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.FourColors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;

import java.awt.*;
import java.util.*;
import java.util.List;

class ClientControllerTest {

    static VirtualServer server;
    static ClientController controller;

    static String myNickname;
    static Map<String, FourColors> nicknames;
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
                nicknames.put(myNickname, FourColors.RED);
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
                controller.notifyFaceDownComponentRequest(myNickname, userHandComponentId);
            }

            @Override
            public void requestComponent(int componentId) {
//                faceUpComponents.removeIf(c -> c.getId() == componentId); // can't be properly mocked up here
                userHandComponentId = componentId;
                System.out.println("FAKE SERVER EVENT: the requested face up component was successfully taken");
                controller.notifyFaceUpComponentRequest(myNickname, componentId);
            }


            @Override
            public void grabStashedComponent(int stashIndex) {
                System.out.println("FAKE SERVER EVENT: grab stashed component");
                controller.notifyGrabFromStash(myNickname, 5, List.of(0, 0));
            }

            @Override
            public void stashComponent() {
                System.out.println("FAKE SERVER EVENT: stashed component");
                controller.notifyStashComponent(myNickname, List.of(4, 0));
            }

            @Override
            public void placeComponent(Point point, int orientation) {
                System.out.println("FAKE SERVER EVENT: the current component was successfully positioned where requested");
                controller.notifyComponentPositioning(myNickname, 5, orientation, point);
            }

            @Override
            public void placeShipOnFlightBoard(int startingPosition) {
                System.out.println("FAKE SERVER EVENT: your ship was successfully positioned where requested");
                controller.notifyPlayerPosition("OtherPlayer1", 4);
            }

            @Override
            public void flipHourglass() {

            }

            @Override
            public void rejectComponent() {
                System.out.println("FAKE SERVER EVENT: rejected component");
                controller.notifyComponentRejection(myNickname, userHandComponentId);
            }

            @Override
            public void releaseForecast() {
                System.out.println("FAKE SERVER EVENT: forecast unlocked");
                controller.notifyReleaseForecast(myNickname, 2);
            }

            @Override
            public void acquireForecast(int deckIndex) {
                System.out.println("FAKE SERVER EVENT: forecast acquired");
                controller.sendForecastDeck(List.of(3, 4, 5));
            }

            @Override
            public void removeComponent(Point point) {
                System.out.println("FAKE SERVER EVENT: single component removed");
                controller.notifyComponentsRemoval(myNickname, List.of(point));
            }

            @Override
            public void chooseShipPiece(int pieceIndex) {
                System.out.println("FAKE SERVER EVENT: one ship piece kept, others removed");
                controller.notifyComponentsRemoval(myNickname, List.of(new Point(6, 7), new Point(6, 8)));
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
            public void grabReward(boolean rewardGrabbed) {

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
                controller.changeBatteriesOnComponent(myNickname, point, 2);//todo add num batteries?
            }

            @Override
            public void choosePlanet(int choice) {

            }

            @Override
            public void goNext(String nickname) {

            }

            @Override
            public void giveUp(String nickname) {

            }

            @Override
            public void drawCard() {
                controller.notifyNewCard(7);
            }

            @Override
            public void joinLobby(UUID lobbyID) {
                lobbySequence();
                buildingSequence();
            }

            @Override
            public void leaveLobby(String nickname) {

            }
        };

        controller = new ClientController(server);
    }

    public static void lobbySequence() {
        controller.updateLobbyPlayers(nicknames);

        System.out.println("FAKE SERVER EVENT: another player joined, lobby is complete");
        nicknames.put("OtherPlayer1", FourColors.BLUE);
        controller.updateLobbyPlayers(nicknames);

        controller.notifyNewGame(Level.SECOND, 2);
    }

    public static void buildingSequence() {
        // Simulated server update thread
        new Thread(() -> {
            try {
//                System.out.println("FAKE SERVER EVENT: OtherPlayer1 took a covered component");
//                controller.notifyFaceDownComponentRequest("OtherPlayer1", 10);
//
//                //Thread.sleep(1000);
//                System.out.println("FAKE SERVER EVENT: OtherPlayer1 rejected component");
//                faceUpComponents.add(10);
//                controller.notifyComponentRejection("OtherPlayer1",10);
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
//                controller.notifyPeekForecast("OtherPlayer1", 2);
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
                System.out.println("FAKE SERVER EVENT: OtherPlayer1's ship was successfully positioned where requested");
                controller.notifyPlayerPosition("OtherPlayer1", 1);

                //Thread.sleep(1000);
                System.out.println("FAKE SERVER EVENT: OtherPlayer1's spent a battery");
                controller.changeBatteriesOnComponent("OtherPlayer1", new Point(6, 9), 1);

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
        controller.showInterfaceChoice();
    }
}