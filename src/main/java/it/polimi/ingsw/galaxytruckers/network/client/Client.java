package it.polimi.ingsw.galaxytruckers.network.client;

import it.polimi.ingsw.galaxytruckers.network.client.rmi.RmiClient;
import it.polimi.ingsw.galaxytruckers.network.client.socket.SocketClient;
import it.polimi.ingsw.galaxytruckers.view.CliView;
import it.polimi.ingsw.galaxytruckers.view.GuiView;
import it.polimi.ingsw.galaxytruckers.view.JFXApp;
import it.polimi.ingsw.galaxytruckers.view.View;
import it.polimi.ingsw.galaxytruckers.view.cliScreens.CheatCodes;
import it.polimi.ingsw.galaxytruckers.view.cliScreens.CliScreen;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import javafx.application.Application;

import java.rmi.RemoteException;
import java.util.Scanner;

/**
 * Client class that initializes the connection to the server and launches the user interface.
 * It allows the user to choose between RMI or Socket communication methods and provides and to
 * select the preferred user interface (CLI or GUI).
 */
public class Client {
    private final ClientController controller = new ClientController();
    private final ClientModel model = new ClientModel();

    public static void main(String[] args) {
        Client client = new Client();
        if (args.length != 4) {
            System.out.println("Please enter the address of the server");
            return;
        }
        client.connect(args[0], args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]));
        client.launchUI();
    }

    /**
     * Connects to the server using either RMI or Socket communication.
     *
     * @param serverName    the name of the server exported to the RMI registry
     * @param serverAddress the address of the server to connect to
     * @param rmiPort       the port number for RMI communication
     * @param socketPort    the port number for Socket communication
     */
    public void connect(String serverName, String serverAddress, int rmiPort, int socketPort) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Do you wish to use RMI (0) or Socket (1) for communication with the server?");
        System.out.println("create and skip to building (2) or join and skip to building (3)");
        boolean chosen = false;
        controller.setModel(model);
        controller.initEventHandler();
        do {
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice > 1 && choice <= 3) {
                    model.activateCheats(choice);
                    choice = Integer.parseInt(CheatCodes.cheat());
                }
                ServerHandler server;
                if (choice == 0) {
                    chosen = true;
                    RmiClient rmiClient = new RmiClient();
                    server = rmiClient;
                    controller.setServer(server);
                    rmiClient.setClientController(controller);
                    rmiClient.start(serverName, serverAddress, rmiPort);
                } else if (choice == 1) {
                    chosen = true;
                    SocketClient socketClient = new SocketClient();
                    server = socketClient;
                    controller.setServer(server);
                    socketClient.setController(controller);
                    socketClient.start(serverAddress, socketPort);
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
        System.out.println("Successfully connected to the server");
    }

    /**
     * Launches the user interface for the client.
     * This method allows the user to choose between a graphical interface (GUI) or a command-line interface (CLI).
     */
    public void launchUI() {
        System.out.println("Enter \"G\" to switch to the Graphical Interface, or press any other key to continue here");

        String command;
        if (CheatCodes.isCheatOn()) {
            try {
                command = CheatCodes.cheat();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
                command = " ";
            }
        } else {
            Scanner scanner = new Scanner(System.in);
            command = scanner.nextLine();
        }

        View<?> view;
        if (command.trim().equalsIgnoreCase("G")) {
            GuiView guiView = new GuiView(controller, model);
            view = guiView;
            JFXApp.setGuiView(guiView);
            controller.setView(view);
            Application.launch(JFXApp.class);
        } else {
            view = new CliView(controller, model);
            controller.setView(view);
        }
        // views start rendering autonomously at creation
    }
}
