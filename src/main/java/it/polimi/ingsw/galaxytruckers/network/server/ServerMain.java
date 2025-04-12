package it.polimi.ingsw.galaxytruckers.network.server;

import it.polimi.ingsw.galaxytruckers.network.server.rmi.RmiServer;

import java.rmi.RemoteException;
import java.util.Scanner;

/**
 * Questa classe rappresenta la logica del server implementata con tecnologia RMI.
 */
public class ServerMain {
    public static void main(String[] args) throws RemoteException {
        System.out.println("Congratulations!");
        System.out.println("You are now the proud owner of a Galaxy Truckers Server");

        // two threads are needed to handle both middlewares

        try {
            Thread rmiThread = new Thread(()-> {
                try {
                    RmiServer.main(args);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            });
            rmiThread.start();

//            socketThread = new Thread(()-> SocketServer.main(args)));
//            socketThread.start();

        } catch (Exception e) {
            System.out.println("The server crashed with the following excuse: " + e.getMessage());
        }
    }
}
