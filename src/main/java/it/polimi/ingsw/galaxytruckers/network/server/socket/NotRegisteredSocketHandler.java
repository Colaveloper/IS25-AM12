package it.polimi.ingsw.galaxytruckers.network.server.socket;

import it.polimi.ingsw.galaxytruckers.network.messages.*;
import it.polimi.ingsw.galaxytruckers.network.server.SessionManager;
import it.polimi.ingsw.galaxytruckers.serverController.ServerControllerInterface;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.function.Consumer;

public class NotRegisteredSocketHandler {
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;

    private final ServerControllerInterface controller;

    private final Consumer<NotRegisteredSocketHandler> removeHandler;

    private Thread listenThread = null;
    private boolean listening = false;

    public NotRegisteredSocketHandler(ObjectInputStream inputStream, ObjectOutputStream outputStream, Consumer<NotRegisteredSocketHandler> removeHandler, ServerControllerInterface controller) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.removeHandler = removeHandler;
        this.controller = controller;
    }

    public void start() {
        if (!listening) {
            listening = true;
            listenThread = new Thread(this::listenTask, "ListenThread");
            listenThread.start();
            System.out.println("A not registered socket handler has started");
        }
    }

    public void stop() {
        listening = false;
        removeHandler.accept(this);
        listenThread.interrupt();
    }

    private void listenTask() {
        while (listening) {
            try {
                Message message = (Message) inputStream.readObject();
                switch (message) {
                    case RegisterNickname registerNickname -> {
                        //registerNickname.getNickname();
                        this.registerNickname(registerNickname);
                    }
                    case RegisteredRequest req -> {
                        Response response = new Response(req.getUuid(), new IllegalStateException("You must register first"));
                        outputStream.writeObject(response);
                        outputStream.flush();
                    }
                    default -> {
                        System.err.println("ERROR: the server received a message of type "  + message.getClass().getName());
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Message class not found, config error", e);
            }
        }
    }

    private void registerNickname(RegisterNickname message) throws IOException{
        try {
            Player player = Player.addPlayer(message.getNickname());
            SocketClientHandler clientHandler = new SocketClientHandler(inputStream,outputStream,player,controller);
            SessionManager.getInstance().registerClient(player, clientHandler);
            clientHandler.start();
            controller.registerPlayer(player);
            Response response = new Response(message.getUuid());
            outputStream.writeObject(response);
            outputStream.flush();
            stop();
        } catch (RuntimeException e) {
            outputStream.writeObject(new Response(message.getUuid(),e));
        }
    }

    private void handleIOException(IOException e) {
        System.err.println("An IOException occurred while trying to communicate with the server.");
        removeHandler.accept(this);
        e.printStackTrace(System.err);
        stop();
    }
}
