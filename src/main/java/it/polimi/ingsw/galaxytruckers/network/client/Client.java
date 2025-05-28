package it.polimi.ingsw.galaxytruckers.network.client;

import it.polimi.ingsw.galaxytruckers.network.client.rmi.RmiClient;
import it.polimi.ingsw.galaxytruckers.network.client.socket.SocketClient;
import it.polimi.ingsw.galaxytruckers.view.CliView;
import it.polimi.ingsw.galaxytruckers.view.GuiView;
import it.polimi.ingsw.galaxytruckers.view.cliScreens.CheatCodes;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import javafx.application.Application;

import java.rmi.RemoteException;
import java.util.Scanner;

public class Client {
    private static ClientController controller = new ClientController();
    private static ClientModel model = new ClientModel();
    private VirtualServer server;
    //TODO: add socket implementation

    public void start(String serverName, String serverAddress, int rmiPort, int socketPort) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Do you wish to use RMI (0) or Socket (1) for communication with the server?");
        System.out.println("create and skip to building (2) or join and skip to building (3)");
        boolean chosen = false;
        controller.setModel(model);
        controller.initEventHandler();
        do {
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if(choice > 1 && choice <= 3) {
                    model.activateCheats(choice);
                    choice = Integer.parseInt(CheatCodes.cheat());
                }
                if (choice == 0) {
                    chosen = true;
                    RmiClient rmiClient = new RmiClient();
                    server = rmiClient;
                    controller.setServer(server);
                    rmiClient.setClientController(controller);
                    rmiClient.start(serverName, serverAddress, rmiPort);
                    this.server = rmiClient;
                } else if (choice == 1) {
                    chosen = true;
                    SocketClient socketClient = new SocketClient();
                    server = socketClient;
                    controller.setServer(server);
                    socketClient.setController(controller);
                    socketClient.start(serverAddress, socketPort);
                    System.out.println("TODO: implement Socket communication");
                    return;
                } else {
                    System.out.println("Invalid choice!");
                }
            } catch (NumberFormatException e) {
                System.out.println("The choice is badly formatted!");
            } catch (RemoteException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        } while (!chosen);
    }

    public ClientController getClientController() {
        return controller;
    }


    public static void main(String[] args) throws InterruptedException {//todo togliere exc dopo aver risolto sync
        Client client = new Client();
        if (args.length != 4) {
            System.out.println("Please enter the address of the server");
            return;
        }
        client.start(args[0], args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]));
        System.out.println("Successfully connected to the server");

        System.out.println("Enter \"G\" to switch to the Graphical Interface, or press any other key to continue here");
        String command;
        if(model.isCheatOn()){
            command = CheatCodes.cheat();
        }
        else{
            Scanner scanner = new Scanner(System.in);
            command = scanner.nextLine();
        }

        if (command.trim().equalsIgnoreCase("G")) {
            controller.setView(new GuiView(controller, model));
//            Application.launch(GuiView.class); // calls view.updateScreen(...)
        } else {
            controller.setView(new CliView(controller, model));
        }
    }
}
