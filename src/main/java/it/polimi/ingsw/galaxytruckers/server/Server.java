package it.polimi.ingsw.galaxytruckers.server;

import it.polimi.ingsw.galaxytruckers.network.server.SessionManager;
import it.polimi.ingsw.galaxytruckers.server.model.GameModel;
import it.polimi.ingsw.galaxytruckers.network.server.rmi.RmiServer;
import it.polimi.ingsw.galaxytruckers.network.server.socket.SocketServer;
import it.polimi.ingsw.galaxytruckers.server.controller.ServerController;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Scanner;

/**
 * The Server class initializes and starts the RMI and socket servers for the Galaxy Trucker game.
 * It sets up the game model and the server controller, allowing clients to connect and interact with the app.
 */
public class Server {
    private final String name;
    private final int rmiPort;
    private final int socketPort;
    private final boolean demoMode;
    private final boolean editScenario;

    /**
     * Constructor for the Server class.
     *
     * @param name name of the server (used for RMI binding)
     * @param rmiPort the port for the RMI server
     * @param socketPort the port for the socket server
     * @param demoMode true if the server should run in demo mode, false otherwise
     * @param editScenario true if the server should allow editing the scenario, false otherwise
     */
    public Server(String name, int rmiPort, int socketPort, boolean demoMode, boolean editScenario) {
        this.name = name;
        this.rmiPort = rmiPort;
        this.socketPort = socketPort;
        this.demoMode = demoMode;
        this.editScenario = editScenario;
    }

    /**
     * Starts the server by initializing the ServerController with a new GameModel,
     * and then starting both the RMI and socket servers.
     */
    public void start() {
        ServerController controller = new ServerController(new GameModel(), demoMode, editScenario);
        SessionManager.getInstance().setServerController(controller);
        try {
            RmiServer rmiServer = new RmiServer(controller);
            rmiServer.start(name, rmiPort);
        } catch (RemoteException e) {
            System.err.println("Failed to connect RMI server: ");
            e.printStackTrace(System.err);
        }
        try {
            SocketServer socketServer = new SocketServer(controller);
            socketServer.start(socketPort);
        } catch (IOException e) {
            System.err.println("Failed to connect socket server: ");
            e.printStackTrace(System.err);
        }
        do {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Type 'q' to stop the server.");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("q")) {
                System.out.println("Stopping the server...");
                System.exit(0);
                break;
            } else {
                System.out.println("Unknown command. Type 'q' to stop the server.");
            }
        } while (true);
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            throw new IllegalArgumentException("Wrong number of arguments");
        }
        Server server = new Server(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]), false, false);
        server.start();
        System.out.println("The server has been started...");
    }
}
