package it.polimi.ingsw.galaxytruckers.network;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.network.client.ClientControllerInterface;
import it.polimi.ingsw.galaxytruckers.network.client.socket.SocketClient;
import it.polimi.ingsw.galaxytruckers.network.server.socket.SocketServer;
import it.polimi.ingsw.galaxytruckers.serverController.ServerControllerInterface;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.UUID;

import static org.mockito.Mockito.*;

public class SocketTest {
    SocketClient socketClient;
    SocketServer socketServer;
    ClientControllerInterface clientController;
    ServerControllerInterface serverController;
    String address = "127.0.0.1";
    int port = 12346;

    @BeforeEach
    public void setup() throws InterruptedException {
        clientController = mock(ClientControllerInterface.class);
        serverController = mock(ServerControllerInterface.class);
        socketClient = new SocketClient();
        socketClient.setController(clientController);
        socketServer = new SocketServer(serverController);

        Thread thread = new Thread(() -> {
            try {
                socketServer.start(port);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, "Server-starter");
        thread.start();
        thread.join();

        socketClient.start(address, port);
    }

    @Test
    void registerNickname() {
        socketClient.registerNickname("x");
        verify(serverController).registerNickname("x");
    }

    @Test
    void requestNewGame() {
        socketClient.registerNickname("x");
        socketClient.requestNewGame(Level.SECOND, 2);
        verify(serverController).newGame(null,Level.SECOND,2);
    }

    @Test
    void joinLobby() {
        UUID userUUID = UUID.randomUUID();
        socketClient.registerNickname("x");
        socketClient.joinLobby(userUUID);
        verify(serverController).joinLobby(null,userUUID);
    }

    @AfterEach
    void cleanup() {
        socketServer.stop();
    }
}
