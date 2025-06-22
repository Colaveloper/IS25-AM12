package it.polimi.ingsw.galaxytruckers.network.server.socket;

import it.polimi.ingsw.galaxytruckers.network.SafeSocket;
import it.polimi.ingsw.galaxytruckers.network.messages.*;
import it.polimi.ingsw.galaxytruckers.serverController.ServerControllerInterface;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class NotRegisteredSocketHandler {
    private static final long ttl = 10;

    private final SafeSocket socket;
    private final ServerControllerInterface controller;
    private final Consumer<NotRegisteredSocketHandler> removeHandler;

    private final Object stopLock = new Object();
    private boolean stopped = false;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> scheduledFuture;

    private final Thread listenThread;
    private boolean listening = false;

    public NotRegisteredSocketHandler(SafeSocket socket, Consumer<NotRegisteredSocketHandler> removeHandler, ServerControllerInterface controller) {
        this.socket = socket;
        this.removeHandler = removeHandler;
        this.controller = controller;
        this.listenThread = new Thread(this::listenTask, "ListenThread");
    }

    public void start() {
        if (!listening) {
            scheduledFuture = scheduler.schedule(this::stop, ttl, TimeUnit.SECONDS);
            listening = true;
            listenThread.start();
            System.out.println("A not registered socket handler has started");
        }
    }

    public void stop() {
        synchronized (stopLock) {
            if (!stopped) {
                stopped = true;
                listening = false;
                removeHandler.accept(this);
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
                listenThread.interrupt();
                if (scheduledFuture != null) scheduledFuture.cancel(true);
            }
        }
    }

    private void listenTask() {
        while (listening) {
            try {
                Message message;
                message = socket.read();
                switch (message) {
                    case RegisterNickname registerNickname -> {
                        this.registerNickname(registerNickname);
                    }
                    case RegisteredRequest req -> {
                        Response response = new Response(req.getUuid(), new IllegalStateException("You must register first"));
                        socket.write(response);
                    }
                    case EventMessage _, Ping _, Response _ -> {
                        System.err.println("ERROR: the server received a message of type " + message.getClass().getName());
                    }
                }
            } catch (IOException e) {
                handleIOException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Message class not found, config error", e);
            }
        }
    }

    private void registerNickname(RegisterNickname message) throws IOException{
        synchronized (stopLock) {
            if (stopped) throw new IOException("Socket handler has been stopped");
            try {
                scheduledFuture.cancel(true);
                stopWithoutClosing();
                socket.write(new Response(message.getUuid()));
                controller.registerNickname(
                        message.getNickname(),
                        p -> new SocketClientHandler(socket, p, controller)
                );
            } catch (RuntimeException e) {
                socket.write(new Response(message.getUuid(), e));
            }
        }
    }

    private void stopWithoutClosing() {
        synchronized (stopLock) {
            listening = false;
            removeHandler.accept(this);
            listenThread.interrupt();
        }
    }

    private void handleIOException(IOException e) {
        System.err.println("IOException in UnregisteredSocketHandler:");
        e.printStackTrace(System.err);
        stop();
    }
}
