package it.polimi.ingsw.galaxytruckers.network.client;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.network.shared.EventHandler;
import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import it.polimi.ingsw.galaxytruckers.view.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.GuiView;
import javafx.application.Application;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.awt.*;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

class ClientControllerTest {

    static List<String> nicknames;
    static VirtualServer server;
    static ClientController controller;
    static ClientModel model;

    static void setUp() {
        nicknames = new ArrayList<>();

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
                System.out.println("FAKE SERVER EVENT: successfully created lvl "+level+" game for "+playerN+" players");
                controller.showLobbyUpdate(nicknames);
                nicknames.add("OtherPlayer1");
                controller.showLobbyUpdate(nicknames);
                nicknames.add("OtherPlayer2");
                controller.showLobbyUpdate(nicknames);
                nicknames.add("OtherPlayer3");
                controller.showLobbyUpdate(nicknames);
            }

            @Override
            public void requestRandComponent() {

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