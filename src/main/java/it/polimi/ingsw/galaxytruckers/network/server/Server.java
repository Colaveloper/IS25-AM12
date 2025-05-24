package it.polimi.ingsw.galaxytruckers.network.server;

import it.polimi.ingsw.galaxytruckers.model.GameModel;
import it.polimi.ingsw.galaxytruckers.network.server.rmi.RmiServer;
import it.polimi.ingsw.galaxytruckers.serverController.ServerController;

import java.rmi.RemoteException;

public class Server {
    public static final String name = "Galaxy-Truckers-Server";
    public static final int port = 12345;

    public void start() {
        ServerController controller = new ServerController(new GameModel()); // TODO: consider making ServerControllerInterface
        SessionManager.getInstance().setServerController(controller);
        try {
            RmiServer rmiServer = new RmiServer(controller);
            rmiServer.start(name, port);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
        System.out.println("The server has been started...");
    }
}
