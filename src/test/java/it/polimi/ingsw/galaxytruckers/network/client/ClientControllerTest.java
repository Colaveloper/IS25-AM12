package it.polimi.ingsw.galaxytruckers.network.client;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.network.shared.EventHandler;
import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;

import java.awt.*;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.List;

class ClientControllerTest {

    static VirtualServer server;
    static ClientController controller;

    static Map<String, Colors> nicknames;
    static int loopLength;
    static List<Integer> startingPositions;
    static Set<Point> shipArea;
    static int coveredComponentsN;
    static List<Integer> faceUpComponents;

    static void setUp() {
        nicknames = new HashMap<>();
        loopLength = 22;
        startingPositions = new ArrayList<>(List.of(2, 4, 5));
        faceUpComponents = new ArrayList<>();
        shipArea = Set.of(
                new Point(4, 7), new Point(4, 8), new Point(4, 9),
                new Point(5, 6), new Point(5, 7), new Point(5, 8), new Point(5, 9),
                new Point(6, 5), new Point(6, 6), new Point(6, 7), new Point(6, 8), new Point(6, 9),
                new Point(7, 6), new Point(7, 7), new Point(7, 8),
                new Point(8, 5), new Point(8, 6), new Point(8, 7), new Point(8, 8), new Point(8, 9),
                new Point(9, 6), new Point(9, 7), new Point(9, 8), new Point(9, 9),
                new Point(10, 7),new Point(10, 8),new Point(10, 9));
        coveredComponentsN = 44;

        server = new VirtualServer() {
            @Override
            public void registerNickname(String myNickname) throws IOException {
                System.out.println("FAKE SERVER EVENT: successfully registered nickname " + myNickname);
                nicknames.put("qwe", Colors.RED);
                controller.setMyNickname("qwe");

                controller.showGameCreation(); // granting the rights to create a new game
            }

            @Override
            public void newGame(Level level, int playerN) throws IOException {
                // Simulated server update thread
                new Thread(() -> {
                    try {
                        System.out.println("FAKE SERVER EVENT: successfully created lvl "+level+" game for "+playerN+" players");
                        controller.updateLobbyPlayers(nicknames);

                        Thread.sleep(1000);
                        System.out.println("FAKE SERVER EVENT: another player joined, lobby is complete");
                        nicknames.put("OtherPlayer1", Colors.BLUE);
                        controller.updateLobbyPlayers(nicknames);
                        controller.setupGame(loopLength, startingPositions, shipArea, coveredComponentsN);

                        // TODO: SHOW
                        Thread.sleep(1000);
                        System.out.println("FAKE SERVER EVENT: OtherPlayer1 took a covered component");
                        controller.notifyFaceDownComponentRequest("qwe", 5, --coveredComponentsN);

                        Thread.sleep(1000);
                        System.out.println("FAKE SERVER EVENT: OtherPlayer1 rejected component");
                        faceUpComponents.add(5);
                        controller.notifyComponentRejection("OtherPlayer1",5, faceUpComponents);

                        Thread.sleep(1000);
                        System.out.println("FAKE SERVER EVENT: OtherPlayer1 took a covered component");
                        controller.notifyFaceDownComponentRequest("qwe", 6, --coveredComponentsN);

                        // TODO: REMOVE COMPONENT IN HAND
                        Thread.sleep(1000);
                        System.out.println("FAKE SERVER EVENT: OtherPlayer1's current component was successfully positioned where requested");
                        controller.showComponentPositioning("OtherPlayer1", 6, 4, new Point(5, 7));


                        System.out.println("FAKE SERVER EVENT: OtherPlayer1's was successfully positioned where requested");
                        controller.notifyPlaceShipOnFlightBoard("OtherPlayer1", Map.of("OtherPlayer1", 4));

                    } catch (InterruptedException | IOException e) {
                        Thread.currentThread().interrupt();
                    }
                }).start();
            }

            @Override
            public void requestRandComponent() throws IOException {
                System.out.println("FAKE SERVER EVENT: you took a covered component");
                controller.notifyFaceDownComponentRequest("qwe", 6, --coveredComponentsN);
            }

            @Override
            public void requestComponent(int componentID) throws IOException { // TODO: MAKE THIS METHOD CALLABLE
                faceUpComponents.remove(1);
                System.out.println("FAKE SERVER EVENT: the requested face up component was successfully taken");
                controller.notifyFaceUpComponentRequest("qwe", componentID, faceUpComponents);
            }

            @Override
            public void placeComponent(Point point) throws IOException {
                System.out.println("FAKE SERVER EVENT: the current component was successfully positioned where requested");
                controller.showComponentPositioning("qwe", 5, 4, point);
            }

            @Override
            public void grabStashedComponent(int componentID) {

            }

            @Override
            public void placeShipOnFlightBoard(int startingPosition) {
                System.out.println("FAKE SERVER EVENT: your ship was successfully positioned where requested");
                controller.notifyPlaceShipOnFlightBoard("OtherPlayer1", Map.of("qwe", 2, "OtherPlayer1", 4));
            }

            @Override
            public void flipHourglass() {

            }

            @Override
            public void acquireForecast(int deckIndex) {

            }

            @Override
            public void rejectComponent() {

            }

            @Override
            public void stashComponent() {

            }

            @Override
            public void releaseForecast() {

            }

            @Override
            public void drawCard() throws IOException {}

            @Override
            public void reportError(String error) throws RemoteException {}

            @Override
            public void registerHandler(EventHandler handler) throws RemoteException {}

            @Override
            public void processEvents() throws IOException {}
        };

        // the following anonymous class is used to force GUI
        controller = new ClientController(server);
    }

    public static void main(String[] args) throws IOException {
        setUp();
        System.out.println("use this nickname or it breaks: qwe");
        controller.showInterfaceChoice(server);
    }
}