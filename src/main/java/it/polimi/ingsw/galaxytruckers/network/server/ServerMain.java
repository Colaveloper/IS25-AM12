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
        System.out.println("Two middlewares are available to use: Socket and RMI");

        Scanner scanner = new Scanner(System.in);
        String input;
        do {
            System.out.println("Input S for Socket or R for Rmi: ");
            input = scanner.nextLine();
        } while (!input.equalsIgnoreCase("S") && !input.equalsIgnoreCase("R"));

        try {
            if (input.equalsIgnoreCase("R")) {
                RmiServer.main(args);
            } else {
                //SocketServer.main(args) // TODO: implement SocketServer
            }
        } catch (Exception e) {
            System.out.println("The server crashed with the following excuse: " + e.getMessage());
        }
    }
}
