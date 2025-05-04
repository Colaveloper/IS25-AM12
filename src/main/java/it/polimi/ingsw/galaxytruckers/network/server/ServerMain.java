package it.polimi.ingsw.galaxytruckers.network.server;

import it.polimi.ingsw.galaxytruckers.network.server.rmi.RmiServer;
import it.polimi.ingsw.galaxytruckers.network.server.socket.SocketServer;
import it.polimi.ingsw.galaxytruckers.serverController.ServerController;
import it.polimi.ingsw.galaxytruckers.serverController.ServerControllerInterface;

import java.rmi.RemoteException;

/**
 * Questa classe rappresenta la logica del server implementata con tecnologia RMI.
 */
public class ServerMain {
    public static void main(String[] args) throws RemoteException {
        System.out.println("Congratulations!");
        System.out.println("You are now the proud owner of a Galaxy Truckers Server");

        // two threads are needed to handle both middlewares

        try {

            ServerControllerInterface serverController = new ServerController();

            Thread rmiThread = new Thread(()-> {
                try {
                    RmiServer.start(serverController);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            });
            Thread socketThread = new Thread(()-> SocketServer.start(serverController));

            socketThread.start();
            rmiThread.start();

        } catch (Exception e) {
            System.out.println("The server crashed with the following excuse: " + e.getMessage());
        }
    }
}
