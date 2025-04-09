package it.polimi.ingsw.galaxytruckers.networkRMI.server;

import it.polimi.ingsw.galaxytruckers.controller.Controller;
import it.polimi.ingsw.galaxytruckers.networkRMI.client.VirtualServerRmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Questa classe rappresenta la logica del server implementata con tecnologia RMI.
 */
public class RmiServer extends UnicastRemoteObject implements VirtualServerRmi {
    final Controller controller;
    final List<VirtualClientRmi> clients = new ArrayList<>();

    public RmiServer() throws RemoteException {
        super();
        this.controller = new Controller();
    }

    public static void main(String[] args) throws RemoteException {
        final String serverName = "GalacticServer";
        VirtualServerRmi server = new RmiServer();
        Registry registry = LocateRegistry.createRegistry(1234);
        registry.rebind(serverName, server);
        System.out.println("Server bound");
    }


    @Override
    public void connect(VirtualClientRmi client) throws RemoteException {
        //TODO. Attenzione, più client possono invocare questo metodo simultaneamente!
        synchronized (this.clients) {
            this.clients.add(client);
        }
    }

    @Override
    public void drawCard() throws RemoteException {
        System.out.println("new card request received");
        // TODO: get cardId from controller
        int cardId = (int) (Math.random()*100);
        synchronized (this.clients){
            for(VirtualClientRmi client: clients){
                client.showNewCard(cardId);
            }
        }
    }

    @Override
    public void reportError(String error) throws Exception {
        synchronized (this.clients){
            for(VirtualClientRmi client: clients){
                client.reportError(error);
            }
        }
    }
}
