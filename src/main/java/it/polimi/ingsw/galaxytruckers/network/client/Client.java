package it.polimi.ingsw.galaxytruckers.network.client;

import it.polimi.ingsw.galaxytruckers.network.client.rmi.RmiClient;
import it.polimi.ingsw.galaxytruckers.network.server.Server;

import java.rmi.RemoteException;
import java.util.Scanner;

public class Client {
    private ClientController clientController;
    private RmiClient rmiClient;
    //TODO: add socket implementation

    public void start(String serverName, String serverAddress, int port) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Do you wish to use RMI (0) or Socket (1) for communication with the server?");
        boolean chosen = false;
        do {
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice == 0) {
                    chosen = true;
                    this.rmiClient = new RmiClient();
                    rmiClient.start(serverName, serverAddress, port);
                } else if (choice == 1) {
                    //TODO: set virtual server to SocketImplementation
                    System.out.println("TODO: implement Socket communication");
                    return;
                } else {
                    System.out.println("Invalid choice!");
                }
            } catch (NumberFormatException e) {
                System.out.println("The choice is badly formatted!");
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        } while (!chosen);

        this.clientController = new ClientController(this.rmiClient);
        this.rmiClient.setClientController(this.clientController);
    }

    public ClientController getClientController() {
        return clientController;
    }

    public RmiClient getRmiClient() {
        return rmiClient;
    }

    public static void main(String[] args) {
        Client client = new Client();
        if (args.length == 0) {
            System.out.println("Please enter the address of the server");
            return;
        }
        client.start(Server.name, args[0], Server.port);
        System.out.println("Successfully connected to the server");
        client.clientController.showInterfaceChoice();
    }
}
