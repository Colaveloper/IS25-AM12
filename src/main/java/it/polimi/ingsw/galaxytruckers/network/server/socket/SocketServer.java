package it.polimi.ingsw.galaxytruckers.network.server.socket;

import it.polimi.ingsw.galaxytruckers.serverController.ServerControllerInterface;

import java.io.*;
import java.net.*;
import java.util.HashSet;
import java.util.Set;

public class SocketServer {
    private final ServerControllerInterface serverController;

    private boolean running;
    private ServerSocket serverSocket;
    private Thread listenThread;

    private final Set<NotRegisteredSocketHandler> unregisteredSocketHandlers = new HashSet<>();

    public SocketServer(ServerControllerInterface serverController) {
        this.running = false;
        this.listenThread = null;
        this.serverController = serverController;
    }

    public void start(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.running = true;
        this.listenThread = new Thread(this::listen, "SocketServer");
        this.listenThread.start();
        System.out.println("Socket server: started ✅");
    }

    private void listen() {
        while (running) {
            try {
                Socket clientSocket = serverSocket.accept();
                ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
                NotRegisteredSocketHandler handler = new NotRegisteredSocketHandler(
                        inputStream,
                        outputStream,
                        this::removeHandler,
                        serverController);
                addHandler(handler);
                handler.start();
                System.out.println("Accepted new unregistered client connection");
            } catch (IOException e) {
                handleIOException(e);
            }
        }
    }

    public void addHandler(NotRegisteredSocketHandler handler) {
        synchronized (unregisteredSocketHandlers) {
            unregisteredSocketHandlers.add(handler);
        }
    }

    public void removeHandler(NotRegisteredSocketHandler handler) {
        synchronized (unregisteredSocketHandlers) {
            unregisteredSocketHandlers.remove(handler);
        }
        System.out.println("Removed unregistered socket handler");
    }

    public Set<NotRegisteredSocketHandler> getUnregisteredSocketHandlers() {
        return unregisteredSocketHandlers;
    }

    private void handleIOException(IOException e) {
        System.err.println("Socket server: an IO exception occurred");
        e.printStackTrace(System.err);
        running = false;
        this.listenThread = null;
    }
}