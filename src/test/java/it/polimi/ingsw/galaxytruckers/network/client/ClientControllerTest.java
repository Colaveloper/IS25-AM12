package it.polimi.ingsw.galaxytruckers.network.client;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.network.shared.EventHandler;
import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import it.polimi.ingsw.galaxytruckers.view.ClientModel;

import java.awt.*;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

class ClientControllerTest {

    static VirtualServer server;
    static ClientController controller;

    static List<String> nicknames;
    static int loopLength;
    static List<Integer> startingPositions;
    static Set<Point> shipArea;
    static int coveredComponentsN;

    static void setUp() {
        nicknames = new ArrayList<>();
        loopLength = 22;
        startingPositions = new ArrayList<>(List.of(2, 4, 5));
        shipArea = Set.of(new Point(8,7), new Point(7,8), new Point(6,7), new Point(7,6));
        coveredComponentsN = 44;

        server = new VirtualServer() {
            @Override
            public void registerNickname(String myNickname) throws IOException {
                nicknames.add(myNickname);

                System.out.println("FAKE SERVER EVENT: successfully registered nickname " + myNickname);
                controller.setNickname(myNickname);

                controller.showGameCreation(); // granting the rights to create a new game
            }

            @Override
            public void newGame(Level level, int playerN) throws IOException {
                // Simulated server update thread
                new Thread(() -> {
                    try {
                        System.out.println("FAKE SERVER EVENT: successfully created lvl "+level+" game for "+playerN+" players");
                        controller.showLobbyUpdate(nicknames);
                        Thread.sleep(1000);
                        nicknames.add("OtherPlayer1");
                        controller.showLobbyUpdate(nicknames);
                        Thread.sleep(1000);
                        nicknames.add("OtherPlayer2");
                        controller.showLobbyUpdate(nicknames);
                        Thread.sleep(1000);
                        nicknames.add("OtherPlayer3");
                        controller.showLobbyUpdate(nicknames);
                        controller.setupGame(loopLength, startingPositions, shipArea);
                        controller.setCoveredComponents(coveredComponentsN);
                        for (int i = 0; i<2; i++) {
                            Thread.sleep(3000);
                            controller.setCoveredComponents(--coveredComponentsN);
                        }
                    } catch (InterruptedException | IOException e) {
                        Thread.currentThread().interrupt();
                    }
                }).start();
            }

            @Override
            public void requestRandComponent() throws IOException {
                controller.setCoveredComponents(--coveredComponentsN);
                controller.setCurrentComponent(45);
            }

            @Override
            public void requestComponent(int componentID) {

            }

            @Override
            public void grabStashedComponent(int componentID) {

            }

            @Override
            public void flipHourglass() {

            }

            @Override
            public void placeShipOnFlightBoard(int startingPosition) {

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
            public void placeComponent(Point point) {

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
        controller.showInterfaceChoice(server);
    }
}