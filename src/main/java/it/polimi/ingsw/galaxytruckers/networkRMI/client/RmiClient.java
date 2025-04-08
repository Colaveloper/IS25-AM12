package it.polimi.ingsw.galaxytruckers.networkRMI.client;

import it.polimi.ingsw.galaxytruckers.networkRMI.server.VirtualViewRmi;
import it.polimi.ingsw.galaxytruckers.view.ClientGameModel;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

/**
 * Questa classe rappresenta la logica del client implementata con tecnologia RMI.
 */
public class RmiClient extends UnicastRemoteObject implements VirtualViewRmi {
    final VirtualServerRmi server;
    final ClientGameModel clientModel;

    public RmiClient(VirtualServerRmi server) throws RemoteException{
        super();
        this.server = server;
        clientModel = new ClientGameModel();
    }

    public static void main(String[] args) throws RemoteException, NotBoundException {
        final String serverName = "GalacticServer";
        Registry registry = LocateRegistry.getRegistry(args[0], 1234);
        VirtualServerRmi server = (VirtualServerRmi) registry.lookup(serverName);
        new RmiClient(server).run();
    }

    private void run() throws RemoteException {
        this.server.connect(this);
        this.runCli();
    }

    private void runCli() throws RemoteException {
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.print("Insert command: ");
            String command = scan.nextLine();
            if (command.equals("submit")) {
                server.drawCard();
            }
        }
    }

    @Override
    public void showNewCardUpdate(Integer cardId) throws RemoteException {
        System.out.println("new card! the id is:"+cardId);
        // use cardId to update the current card in the model
        // reprint the CLI
    }

    @Override
    public void reportError(String details) throws RemoteException {

    }
}
