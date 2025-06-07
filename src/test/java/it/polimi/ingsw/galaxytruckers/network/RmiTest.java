package it.polimi.ingsw.galaxytruckers.network;

import it.polimi.ingsw.galaxytruckers.model.GameModel;
import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.network.client.rmi.RmiClient;
import it.polimi.ingsw.galaxytruckers.network.server.SessionManager;
import it.polimi.ingsw.galaxytruckers.network.server.rmi.RmiServer;
import it.polimi.ingsw.galaxytruckers.serverController.ServerController;
import it.polimi.ingsw.galaxytruckers.serverController.ServerControllerInterface;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

public class RmiTest {
    Thread serverThread;
    RmiServer server;

    RmiClient rmiClient;
    ClientController clientController;
    String name = "Galaxy-Truckers";
    int port = 12345;

    @BeforeEach
    void setUp() throws RemoteException, NotBoundException {
        rmiClient = new RmiClient();
    }

    @Test
    void noServerThrowsException(){
        assertThrows(RuntimeException.class, () -> rmiClient.start("", "127.0.0.1", 12345));
    }

    @Test
    void setClientController() {
        rmiClient.setClientController(clientController);
        assertEquals(clientController, rmiClient.getClientController());
    }

    @Nested
    class ClientTest {
        @BeforeEach
        public void setUp() throws Exception {

            serverThread = new Thread(() -> {
                ServerControllerInterface serverController = new ServerController(new GameModel());
                try {
                    server = new RmiServer(serverController);
                    server.start(name, port);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            });
            serverThread.start();
            serverThread.join();
        }

        @Test
        public void connectRmiClientTest() {
            assertDoesNotThrow(() -> rmiClient.start(name, "127.0.0.1",port));
        }

        @Test
        public void registerClientTest() {
            rmiClient.start(name, "127.0.0.1",port);
            rmiClient.registerNickname("nickname");
            assertNotNull(SessionManager.getInstance().getClient(Player.getPlayer("nickname")));
        }

        @AfterEach
        public void tearDown() {
            try {
                server.stop();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
