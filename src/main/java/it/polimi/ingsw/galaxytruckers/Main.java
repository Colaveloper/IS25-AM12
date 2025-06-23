package it.polimi.ingsw.galaxytruckers;

import it.polimi.ingsw.galaxytruckers.network.client.Client;
import it.polimi.ingsw.galaxytruckers.network.server.Server;

import java.util.Scanner;

public class Main {
    private final static int RMI_PORT = 12345; // Default RMI port
    private final static int SOCKET_PORT = 12346; // Default socket port
    private final static String SERVER_ADDRESS = "localhost"; // Default server address
    private final static String SERVER_NAME = "Galaxy-Truckers-Server"; // Default server name

    public static void main(String[] args) {
        // This is the entry point of the Galaxy Truckers game application.
        // You can initialize your game logic here, set up the server, or start the client interface.
        System.out.println("Welcome to Galaxy Truckers!");
        // Additional initialization code can go here.
        System.out.println("Do you want to start the server or client? (server/client)");
        Scanner input = new Scanner(System.in);
        boolean badInput;
        do {
            badInput = false;
            String choice = input.nextLine().trim().toLowerCase();
            if (choice.equals("server")) {
                startServer();
            } else if (choice.equals("client")) {
                startClient();
            } else {
                badInput = true;
                System.out.println("Invalid choice. Please enter 'server' or 'client'.");
            }
        } while (badInput);
    }

    private static void startServer() {
        Server server = new Server(SERVER_NAME, RMI_PORT, SOCKET_PORT);
        server.start();
    }

    private static void startClient() {
        System.out.println("Insert the server address (default: " + SERVER_ADDRESS + "):");
        Scanner input = new Scanner(System.in);
        String serverAddress = input.nextLine().trim();
        if (serverAddress.isEmpty()) {
            serverAddress = SERVER_ADDRESS;
        }
        System.out.println("Connecting to server at " + serverAddress + " on ports RMI: " + RMI_PORT + " and Socket: " + SOCKET_PORT);
        Client client = new Client();
        client.connect(SERVER_NAME, serverAddress, RMI_PORT, SOCKET_PORT);
        client.launchUI();
    }
}
