package it.polimi.ingsw.galaxytruckers.network.client;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.network.client.rmi.RmiClient;
import it.polimi.ingsw.galaxytruckers.network.client.rmi.VirtualServerRmi;
import it.polimi.ingsw.galaxytruckers.network.server.rmi.RmiServer;
import it.polimi.ingsw.galaxytruckers.network.server.rmi.VirtualClientRmi;
import it.polimi.ingsw.galaxytruckers.view.CliView;
import it.polimi.ingsw.galaxytruckers.view.ClientGameModel;
import it.polimi.ingsw.galaxytruckers.view.ProjectileType;
import it.polimi.ingsw.galaxytruckers.view.View;
import it.polimi.ingsw.galaxytruckers.view.visualizationStrategy.NewCardVisualization;

import java.awt.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.List;

/**
 * Questa classe rappresenta la logica del client implementata con tecnologia RMI.
 */
public class ClientMain {
    public static void main(String[] args) {
        System.out.println("Welcome!");
        System.out.println("In order to play Galaxy Truckers choose a middleware");
        System.out.println("Two are available to use: Socket and RMI");

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
            System.out.println("The server crashed with the following excuse: " + e.getMessage());
        }
    }
}
