package it.polimi.ingsw.galaxytruckers.network.server.socket;

import java.io.*;
import java.net.*;

public class SocketServer {
    private boolean running;
    private ServerSocket serverSocket;
    private Thread listenThread;

    public SocketServer() {
        this.running = false;
        this.listenThread = null;
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
                ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
                ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                //TODO: create handler for each client
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