package it.polimi.ingsw.galaxytruckers.network.server.socket;
import it.polimi.ingsw.galaxytruckers.serverController.ServerControllerInterface;

import java.io.*;
import java.net.*;

public class SocketServer {
    private static final int PORT = 1235;

    public static void start(ServerControllerInterface serverController) {
        System.out.println("Socket server: started ✅");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket, serverController)).start(); // one thread per client
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}