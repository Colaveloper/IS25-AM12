package it.polimi.ingsw.galaxytruckers;

import it.polimi.ingsw.galaxytruckers.network.client.Client;
import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.network.client.rmi.RmiClient;
import it.polimi.ingsw.galaxytruckers.network.server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SystemTest {
    Thread serverThread;
    Server server;

    RmiClient rmiClient;
    ClientController clientController;

    @BeforeEach
    public void setUp() throws Exception {

        serverThread = new Thread(() -> {
            server = new Server();
            server.start();
        });
        serverThread.start();

        RmiClient rmiClient = new RmiClient();
        rmiClient.start(Server.name, "127.0.0.1",Server.port);

        clientController = new ClientController(rmiClient);
    }

    @Test
    public void run() {

    }
}
