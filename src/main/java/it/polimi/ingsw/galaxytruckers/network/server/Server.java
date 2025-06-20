package it.polimi.ingsw.galaxytruckers.network.server;

import it.polimi.ingsw.galaxytruckers.model.GameModel;
import it.polimi.ingsw.galaxytruckers.network.server.rmi.RmiServer;
import it.polimi.ingsw.galaxytruckers.network.server.socket.SocketServer;
import it.polimi.ingsw.galaxytruckers.serverController.ServerController;

import java.io.IOException;
import java.rmi.RemoteException;

public class Server {
    private final String name;
    private final int rmiPort;
    private final int socketPort;

    public Server(String name, int rmiPort, int socketPort) {
        this.name = name;
        this.rmiPort = rmiPort;
        this.socketPort = socketPort;
    }

    public void start() {
        ServerController controller = new ServerController(new GameModel()); // TODO: consider making ServerControllerInterface
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
        //SessionManager.getInstance().shutDown();
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            throw new IllegalArgumentException("Wrong number of arguments");
        }
        Server server = new Server(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]));
        server.start();
        System.out.println("The server has been started...");
    }
}
