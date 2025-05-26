package it.polimi.ingsw.galaxytruckers.network.server.rmi;

import it.polimi.ingsw.galaxytruckers.network.client.rmi.RemoteClient;
import it.polimi.ingsw.galaxytruckers.serverController.ServerController;
import it.polimi.ingsw.galaxytruckers.serverController.events.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

class RmiServerTest {
    RmiServer server;

    @BeforeEach
    void setUp() throws RemoteException {
        server = new RmiServer(new ServerController(null));
    }

    @AfterEach
    void tearDown() throws RemoteException {
        server.stop();
    }

    @Test
    void start() throws RemoteException {
        assertDoesNotThrow(() -> server.start("x",12345));

    }

    @Test
    void registerNickname() throws RemoteException {
        server.start("x",12345);
        RemoteController controller = server.registerNickname(new PlainClientStub(),"x");
        assertNotNull(controller);
    }
}

class PlainClientStub implements RemoteClient {
    @Override
    public void notifyEvent(Event event) throws RemoteException {

    }
}