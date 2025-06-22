package it.polimi.ingsw.galaxytruckers.network;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.network.client.ClientControllerInterface;
import it.polimi.ingsw.galaxytruckers.network.client.socket.SocketClient;
import it.polimi.ingsw.galaxytruckers.network.server.ClientHandler;
import it.polimi.ingsw.galaxytruckers.network.server.socket.SocketServer;
import it.polimi.ingsw.galaxytruckers.serverController.ServerControllerInterface;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.LobbyInterface;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.UUID;
import java.util.function.Function;

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
                System.out.println("TEST: server startup failed");
                throw new RuntimeException(e);
            }
        }, "Server-starter");
        thread.start();
        thread.join();

        socketClient.start(address, port);
    }

    @Test
    void registerNickname() {
//        socketClient.registerNickname("x");
//        System.out.println("Server controller: " + serverController);
//        verify(serverController).requestActiveLobbies(Player.getPlayer("x"));
//        System.out.println("VERIFIED");
    }

    @Test
    void requestNewGame() {
        socketClient.registerNickname("x");
        socketClient.requestNewGame(Level.SECOND, 2);
        verify(serverController).newGame(Player.getPlayer("x"),Level.SECOND,2);
    }

    @Test
    void joinLobby() {
        UUID id = UUID.randomUUID();
        socketClient.registerNickname("x");
        socketClient.joinLobby(id);
        verify(serverController).joinLobby(Player.getPlayer("x"),id);
    }

    @AfterEach
    void cleanup() {
        socketClient.stop();
        socketServer.stop();
        Player.removePlayer("x");
    }
}

class ServerControllerStub implements ServerControllerInterface {

    @Override
    public <T extends ClientHandler> T registerNickname(String nickname, Function<Player, T> handlerFunction) {
        return null;
    }

    /**
     * Creates a new lobby for a game of the chosen level and with
     * the specified number of players, adding the creator to it
     *
     * @param creator    nickname of the lobby creator
     * @param level      level of the game
     * @param numPlayers number of players in the game
     * @return the {@link LobbyInterface} of the created lobby
     * @throws IllegalArgumentException if {@code numPlayers} is < 2
     */
    @Override
    public LobbyInterface newGame(Player creator, Level level, int numPlayers) {
        return null;
    }

    /**
     * Adds the player with the specified nickname to the lobby
     * with the given lobby ID
     *
     * @param player  the nickname of the player joining a lobby
     * @param lobbyID the ID of the lobby the player wants to join
     * @return the {@link LobbyInterface} of the lobby the player has just entered
     * @throws IllegalArgumentException if there is no registered
     *                                  player with the given nickname or if there is no lobby with
     *                                  the given ID
     * @throws IllegalStateException    if the specified lobby is not
     *                                  in preparation phase
     */
    @Override
    public LobbyInterface joinLobby(Player player, UUID lobbyID) {
        return null;
    }

    /**
     * Removes the given player from the active players in the server and, if
     * the player was in a lobby, notifies other players in the lobby of the
     * disconnection and interrupts the game
     *
     * @param player the player who has disconnected
     */
    @Override
    public void handlePlayerDisconnection(Player player) {

    }

    /**
     * Notifies other players in the lobby of the player's exit and interrupts
     * the game
     *
     * @param player the player who left
     */
    @Override
    public void leaveLobby(Player player) {

    }

}