package it.polimi.ingsw.galaxytruckers;

import it.polimi.ingsw.galaxytruckers.network.client.rmi.RmiClient;

import java.util.*;

/**
 * Questa classe rappresenta la logica del client implementata con tecnologia RMI.
 */
public class ClientMain {
    public static void main(String[] args) {
        System.out.println("Welcome! 👋");
        System.out.println("In order to play Galaxy Truckers choose a middleware between Socket and RMI");

        Scanner scanner = new Scanner(System.in);
        String input;
        do {
            System.out.println("Input S for Socket or R for Rmi: ");
            input = scanner.nextLine();
        } while (!input.equalsIgnoreCase("S") && !input.equalsIgnoreCase("R"));

        try {
            if (input.equalsIgnoreCase("R")) {
                RmiClient.main(args);
            } else {
                //SocketServer.main(args) // TODO: implement SocketServer
            }
        } catch (Exception e) {
            System.out.println("The whole server crashed ⚠️: " + e.getMessage());
        }
    }
}
