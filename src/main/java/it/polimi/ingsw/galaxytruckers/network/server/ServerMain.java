package it.polimi.ingsw.galaxytruckers.network.server;

import it.polimi.ingsw.galaxytruckers.model.GameModel;
import it.polimi.ingsw.galaxytruckers.network.server.rmi.RmiServer;
import it.polimi.ingsw.galaxytruckers.serverController.ServerController;
import it.polimi.ingsw.galaxytruckers.serverController.ServerControllerInterface;

import java.io.IOException;
import java.rmi.RemoteException;

public class ServerMain {
    public static void main(String[] args) {
        ServerController controller = new ServerController(new GameModel());
        RmiServer rmiServer;
        try {
            rmiServer = new RmiServer(controller);
            rmiServer.start("Galaxy-Truckers-Server",1234);
        } catch (RemoteException e) {
            System.err.println("Could not start RMI server because of " + e.getMessage());
            return;
        }
        System.out.println("RMI server has been started...");
    }
}
