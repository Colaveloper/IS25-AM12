package it.polimi.ingsw.galaxytruckers.network.client;

import it.polimi.ingsw.galaxytruckers.network.client.rmi.RmiClient;
import it.polimi.ingsw.galaxytruckers.network.client.socket.SocketClient;

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
                SocketClient.main(args);
            }
        } catch (Exception e) {
            System.out.println("Server error ⚠️: " + e.getMessage());
        }
    }
}
