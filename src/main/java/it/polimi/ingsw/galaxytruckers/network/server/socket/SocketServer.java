package it.polimi.ingsw.galaxytruckers.network.server.socket;

import it.polimi.ingsw.galaxytruckers.network.SafeSocket;
import it.polimi.ingsw.galaxytruckers.serverController.ServerControllerInterface;

import java.io.*;
import java.net.*;

public class SocketServer {
    private final ServerControllerInterface serverController;

    private boolean running;
    private ServerSocket serverSocket;
    private Thread listenThread;

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

    public void stop() {
        try {
            this.running = false;
            this.serverSocket.close();
            this.listenThread.interrupt();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void listen() {
        while (running) {
            try {
                Socket clientSocket = serverSocket.accept();
                SocketClientHandler handler = new SocketClientHandler(
                        new SafeSocket(clientSocket),
                        serverController);
                handler.start();
                System.out.println("Accepted new unregistered client connection");
            } catch (IOException e) {
                handleIOException(e);
            }
        }
    }

    private void handleIOException(IOException e) {
        System.err.println("Socket server: an IO exception occurred");
        e.printStackTrace(System.err);
        running = false;
        this.listenThread = null;
    }
}