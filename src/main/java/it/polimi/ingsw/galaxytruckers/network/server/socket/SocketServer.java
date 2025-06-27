package it.polimi.ingsw.galaxytruckers.network.server.socket;

import it.polimi.ingsw.galaxytruckers.network.SafeSocket;
import it.polimi.ingsw.galaxytruckers.server.controller.ServerControllerInterface;

import java.io.*;
import java.net.*;

/**
 * A simple socket server that listens for incoming client connections and handles them using
 * {@link SocketClientHandler}. It is designed to run in a separate thread and can be started
 * and stopped as needed.
 */
public class SocketServer {
    private final ServerControllerInterface serverController;

    private boolean running;
    private ServerSocket serverSocket;
    private Thread listenThread;

    /**
     * Constructs a new SocketServer with the specified server controller.
     *
     * @param serverController the controller that manages server operations
     */
    public SocketServer(ServerControllerInterface serverController) {
        this.running = false;
        this.listenThread = null;
        this.serverController = serverController;
    }

    /**
     * Starts the socket server on the specified port.
     *
     * @param port the port on which the server will listen for incoming connections
     * @throws IOException if an I/O error occurs when opening the socket
     */
    public void start(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.running = true;
        this.listenThread = new Thread(this::listen, "SocketServer");
        this.listenThread.start();
        System.out.println("Socket server: started ✅");
    }

    /**
     * Stops the socket server, closing the server socket and interrupting the listening thread.
     */
    public void stop() {
        try {
            this.running = false;
            this.serverSocket.close();
            this.listenThread.interrupt();
        } catch (IOException e) {
            System.err.println("Closing socket server: " + e.getMessage());
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