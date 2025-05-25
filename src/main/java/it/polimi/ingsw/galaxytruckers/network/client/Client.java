package it.polimi.ingsw.galaxytruckers.network.client;

import it.polimi.ingsw.galaxytruckers.network.client.rmi.RmiClient;
import it.polimi.ingsw.galaxytruckers.network.client.socket.SocketClient;

import java.rmi.RemoteException;
import java.util.Scanner;

public class Client {
    private ClientController clientController;
    private VirtualServer server;
    //TODO: add socket implementation

    public void start(String serverName, String serverAddress, int rmiPort, int socketPort) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Do you wish to use RMI (0) or Socket (1) for communication with the server?");
        boolean chosen = false;
        this.clientController = new ClientController();
        do {
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice == 0) {
                    chosen = true;
                    RmiClient rmiClient = new RmiClient();
                    server = rmiClient;
                    clientController.setServer(server);
                    rmiClient.setClientController(clientController);
                    rmiClient.start(serverName, serverAddress, rmiPort);
                    this.server = rmiClient;
                } else if (choice == 1) {
                    chosen = true;
                    SocketClient socketClient = new SocketClient();
                    server = socketClient;
                    clientController.setServer(server);
                    socketClient.setController(clientController);
                    socketClient.start(serverAddress, socketPort);
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
    }

    public ClientController getClientController() {
        return clientController;
    }


    public static void main(String[] args) {
        Client client = new Client();
        if (args.length != 4) {
            System.out.println("Please enter the address of the server");
            return;
        }
        client.start(args[0], args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]));
        System.out.println("Successfully connected to the server");
        client.clientController.showInterfaceChoice();
    }
}
