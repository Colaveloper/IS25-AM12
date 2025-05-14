package it.polimi.ingsw.galaxytruckers.network.client;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import it.polimi.ingsw.galaxytruckers.serverController.events.Event;

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
        coveredComponentsN = 44;

        server = new VirtualServer() {
            @Override
            public void notify(Event event) {

            }

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

                        Thread.sleep(2000);
                        System.out.println("FAKE SERVER EVENT: another player joined, lobby is complete");
                        nicknames.put("OtherPlayer1", Colors.BLUE);
                        controller.updateLobbyPlayers(nicknames);
                        controller.setupGame(Level.SECOND);
                        Thread.sleep(2000);
                        System.out.println("FAKE SERVER EVENT: OtherPlayer1 took a covered component");
                        controller.notifyFaceDownComponentRequest("OtherPlayer1", 10);

                        Thread.sleep(2000);
                        System.out.println("FAKE SERVER EVENT: OtherPlayer1 rejected component");
                        faceUpComponents.add(10);
                        controller.notifyComponentRejection("OtherPlayer1",10);

                        Thread.sleep(2000);
                        System.out.println("FAKE SERVER EVENT: OtherPlayer1 took the component back");
                        faceUpComponents.add(10);
                        controller.notifyFaceUpComponentRequest("OtherPlayer1",10);

                        Thread.sleep(2000);
                        System.out.println("FAKE SERVER EVENT: OtherPlayer1's current component was successfully positioned where requested");
                        controller.showComponentPositioning("OtherPlayer1", 10, 0, new Point(5, 7));


                        System.out.println("FAKE SERVER EVENT: OtherPlayer1's ship was successfully positioned where requested");
                        controller.notifyPlayerPosition("OtherPlayer1", 1);

                    } catch (InterruptedException | IOException e) {
                        Thread.currentThread().interrupt();
                    }
                }).start();
            }

            @Override
            public void requestRandComponent() throws IOException {
                System.out.println("FAKE SERVER EVENT: you took a covered component");
                controller.notifyFaceDownComponentRequest("qwe", (int) (Math.random()*50));
            }

            @Override
            public void requestComponent(int componentID) throws IOException { // TODO: MAKE THIS METHOD CALLABLE
                faceUpComponents.remove(1);
                System.out.println("FAKE SERVER EVENT: the requested face up component was successfully taken");
                controller.notifyFaceUpComponentRequest("qwe", componentID);
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
            public void placeShipOnFlightBoard(int startingPosition) throws IOException {
                System.out.println("FAKE SERVER EVENT: your ship was successfully positioned where requested");
                controller.notifyPlayerPosition("OtherPlayer1", 4);
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