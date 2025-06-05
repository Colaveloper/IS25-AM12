package it.polimi.ingsw.galaxytruckers.network.server.rmi;

import it.polimi.ingsw.galaxytruckers.network.server.SessionManager;
import it.polimi.ingsw.galaxytruckers.network.client.rmi.RemoteClient;
import it.polimi.ingsw.galaxytruckers.serverController.ServerControllerInterface;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RmiServer extends UnicastRemoteObject implements RemoteServer {
    private final ServerControllerInterface controller;
    private Registry registry;
    private String name;

    public RmiServer(ServerControllerInterface controller) throws RemoteException {
        super();
        this.controller = controller;
    }

    public void start(String name, int port) throws RemoteException{
        RemoteServer boundInterface = this;

        registry = LocateRegistry.createRegistry(port);
        registry.rebind(name, boundInterface);

        this.name = name;
    }

    @Override
    public RemoteController registerNickname(RemoteClient client, String nickname) throws RemoteException {
        Player player = Player.addPlayer(nickname);
        RmiClientHandler clientHandler = new RmiClientHandler(client, player, controller);
        SessionManager.getInstance().registerClient(player, clientHandler);
        clientHandler.start();
        controller.registerPlayer(player);
        return clientHandler;
    }

    public void stop() throws RemoteException {
        try {
            registry.unbind(name);
            UnicastRemoteObject.unexportObject(registry, true);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
    }
}
