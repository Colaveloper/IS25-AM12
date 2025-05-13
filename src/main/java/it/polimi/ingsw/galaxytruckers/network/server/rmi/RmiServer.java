package it.polimi.ingsw.galaxytruckers.network.server.rmi;

import it.polimi.ingsw.galaxytruckers.network.server.SessionManager;
import it.polimi.ingsw.galaxytruckers.network.client.rmi.RemoteClient;
import it.polimi.ingsw.galaxytruckers.serverController.ServerControllerInterface;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RmiServer extends UnicastRemoteObject implements RemoteServer {
    private final ServerControllerInterface controller;

    public RmiServer(ServerControllerInterface controller) throws RemoteException {
        super();
        this.controller = controller;
    }

    public void start(String name, int port) throws RemoteException {
        RemoteServer boundInterface = this;
        Registry registry = LocateRegistry.createRegistry(port);
        registry.rebind(name, boundInterface);
    }

    @Override
    public RemoteController registerNickname(RemoteClient client, String nickname) throws RemoteException {
        Player player = controller.registerNickname(nickname);
        RmiClientHandler clientHandler = new RmiClientHandler(client, player, controller);
        SessionManager.getInstance().registerClient(player, clientHandler);
        return clientHandler;
    }
}
