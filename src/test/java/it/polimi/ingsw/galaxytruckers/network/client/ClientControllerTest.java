package it.polimi.ingsw.galaxytruckers.network.client;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.network.shared.EventHandler;
import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

class ClientControllerTest {

    static List<String> nicknames;
    static VirtualServer server;
    static ClientController controller;

    static void setUp() {
        nicknames = new ArrayList<>();

        server = new VirtualServer() {
            @Override
            public void registerNickname(String myNickname) throws IOException {
                nicknames.add(myNickname);
                controller.setNickname(myNickname);
                controller.showGameCreation(); // granting the rights to create a new game
            }

            @Override
            public void newGame(Level level, int playerN) throws IOException {
                controller.showLobbyUpdate(nicknames);
                nicknames.add("OtherPlayer1");
                controller.showLobbyUpdate(nicknames);
                nicknames.add("OtherPlayer2");
                controller.showLobbyUpdate(nicknames);
                nicknames.add("OtherPlayer3");
                controller.showLobbyUpdate(nicknames);
            }

            @Override
            public void drawCard(String nickname) throws IOException {}

            @Override
            public void reportError(String error) throws RemoteException {}

            @Override
            public void registerHandler(EventHandler handler) throws RemoteException {}

            @Override
            public void processEvents() throws IOException {}
        };

        controller = new ClientController(server);
    }

    public static void main(String[] args) throws IOException {
        setUp();
        controller.showInterfaceChoice(server);
    }
}