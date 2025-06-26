package it.polimi.ingsw.galaxytruckers.network.server.rmi;

import it.polimi.ingsw.galaxytruckers.network.client.rmi.RemoteClient;
import it.polimi.ingsw.galaxytruckers.serverController.ServerControllerInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * RMI server implementation that allows clients to register with a nickname.
 * It binds the server to a specific port and provides methods for client registration.
 */
public class RmiServer extends UnicastRemoteObject implements RemoteServer {
    private final ServerControllerInterface controller;
    private Registry registry;
    private String name;

    /**
     * Constructs an RmiServer with the specified controller.
     *
     * @param controller the server controller that manages game logic and client interactions
     * @throws RemoteException if there is an error during remote object creation
     */
    public RmiServer(ServerControllerInterface controller) throws RemoteException {
        super();
        this.controller = controller;
    }

    /**
     * Starts the RMI server, binding it to the specified name and port.
     *
     * @param name the name to bind the RMI server to
     * @param port the port on which the RMI server will listen for incoming connections
     * @throws RemoteException if there is an error during remote object creation or binding
     */
    public void start(String name, int port) throws RemoteException {
        RemoteServer boundInterface = this;

        registry = LocateRegistry.createRegistry(port);
        registry.rebind(name, boundInterface);

        this.name = name;
    }

    @Override
    public RemoteController registerNickname(RemoteClient client, String nickname) throws RemoteException {
        RmiClientHandler handler = new RmiClientHandler(client, controller);
        controller.registerNickname(nickname, handler);
        handler.start();
        return handler;
    }

    /**
     * Stops the RMI server, unbinding it from the registry and unexporting the remote object.
     * @throws RemoteException if there is an error during unbinding or unexporting
     */
    public void stop() throws RemoteException {
        try {
            registry.unbind(name);
            UnicastRemoteObject.unexportObject(registry, true);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
    }
}
