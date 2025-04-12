package it.polimi.ingsw.galaxytruckers.network.server.rmi;

import it.polimi.ingsw.galaxytruckers.network.shared.EventHandler;
import it.polimi.ingsw.galaxytruckers.network.shared.VirtualClient;
import it.polimi.ingsw.galaxytruckers.serverController.ServerController;
import it.polimi.ingsw.galaxytruckers.network.client.rmi.VirtualServerRmi;
import it.polimi.ingsw.galaxytruckers.serverController.events.Event;
import it.polimi.ingsw.galaxytruckers.serverController.events.EventQueue;

import java.io.IOException;
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
    private EventQueue eventQueue;
    private EventHandler handler;

    final ServerController controller;
    final List<VirtualClientRmi> clients = new ArrayList<>();

    public RmiServer() throws RemoteException {
        super();
        this.controller = new ServerController();
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
    public void registerNickname(VirtualClient client, String nickname) throws RemoteException {
        // TODO: make check on saved nicknames
        VirtualClientRmi rmiClient = (VirtualClientRmi) client;
        System.out.println("request accepted: registering"+nickname);
        rmiClient.showNicknameRegistration(nickname);
    }

    @Override
    public void drawCard() throws IOException {
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
    public void reportError(String error) throws RemoteException {
        synchronized (this.clients){
            for(VirtualClientRmi client: clients){
                client.reportError(error);
            }
        }
    }

    @Override
    public void registerHandler(EventHandler handler) throws RemoteException {
        this.handler = handler;
    }

    @Override
    public void processEvents() throws IOException {
        while (!eventQueue.isEmpty()) {
            Event event = eventQueue.dequeue();
            event.accept(handler);
        }
    }

    public void addEvent(Event event) {
        eventQueue.enqueue(event);
    }
}
