package it.polimi.ingsw.galaxytruckers.network.server.socket;

import it.polimi.ingsw.galaxytruckers.shared.enums.GoodsType;
import it.polimi.ingsw.galaxytruckers.shared.enums.Level;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.network.SafeSocket;
import it.polimi.ingsw.galaxytruckers.network.VirtualServer;
import it.polimi.ingsw.galaxytruckers.network.messages.*;
import it.polimi.ingsw.galaxytruckers.network.server.ClientEventQueue;
import it.polimi.ingsw.galaxytruckers.network.server.ClientHandler;
import it.polimi.ingsw.galaxytruckers.network.server.SessionManager;
import it.polimi.ingsw.galaxytruckers.server.controller.ServerControllerInterface;
import it.polimi.ingsw.galaxytruckers.server.controller.events.types.Event;
import it.polimi.ingsw.galaxytruckers.server.controller.lobby.LobbyInterface;
import it.polimi.ingsw.galaxytruckers.server.controller.lobby.Player;
import it.polimi.ingsw.galaxytruckers.shared.enums.Direction;

import java.awt.*;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Handles communication with a client over a socket connection.
 * It processes requests from the client and sends events back to the client.
 */
class SocketClientHandler implements VirtualServer, ClientHandler {
    private Player player;
    private final ServerControllerInterface controller;
    private final SafeSocket socket;

    private final Thread updateThread;
    private final Thread requestThread;
    private final AtomicBoolean isRunning = new AtomicBoolean(false);

    private final Object requestLock = new Object();

    private final ClientEventQueue eventQueue = new ClientEventQueue();

    /**
     * Creates a new SocketClientHandler that handles communication with a client
     *
     * @param socket     the socket through which the client communicates
     * @param controller the controller that manages server operations
     */
    public SocketClientHandler(SafeSocket socket, ServerControllerInterface controller) {
        this.socket = socket;
        this.controller = controller;
        this.requestThread = new Thread(this::requestTask, "RequestThread");
        this.updateThread = new Thread(this::updateTask, "UpdateThread");
    }

    /**
     * Starts the request and update threads to handle client communication.
     * This method should be called after setting the player.
     */
    public void start() {
        isRunning.set(true);
        requestThread.start();
        updateThread.start();
        System.out.println("Started Socket Client Handler");
    }

    @Override
    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public void pauseEvents() {
        eventQueue.pause();
    }

    /**
     * {@inheritDoc}
     *
     * <p>The socket is closed and the threads are interrupted</p>
     */
    @Override
    public void stop() {
        if (isRunning.compareAndSet(true, false)) {
            synchronized (requestLock) {
                requestThread.interrupt();
            }
            updateThread.interrupt();
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Error closing socket");
                e.printStackTrace(System.err);
            }
        }
    }

    private void updateTask() {
        while (isRunning.get()) {
            try {
                Event event = eventQueue.poll();
                socket.write(new EventMessage(event));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (IOException e) {
                eventQueue.clear();
                handleIOException(e);
            }
        }
    }

    private void requestTask() {
        try {
            while (isRunning.get()) {
                Message message = socket.read();
                synchronized (requestLock) {
                    switch (message) {
                        case RegisterNickname request -> {
                            Response response = runRequest(request);
                            socket.write(response);
                        }
                        case RegisteredRequest request -> {
                            Response response;
                            if (player == null) {
                                response = new Response(request.getUuid(),
                                        new IllegalStateException("You have to register first"));
                            } else {
                                response = runRequest(request);
                            }
                            socket.write(response);
                        }
                        case Ping ping -> {
                            if (player != null) {
                                SessionManager.getInstance().ping(player);
                                socket.write(new Response(ping.id()));
                            }
                        }
                        case EventMessage _, Response _ ->
                                System.err.println("The SocketHandler received invalid request " + message.getClass());
                    }
                }
            }
        } catch (IOException e) {
            handleIOException(e);
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found, check that the socket classes are configured correctly");
            stop();
        }
    }

    private Response runRequest(Request request) {
        try {
            request.execute(this);
            return new Response(request.getUuid());
        } catch (RuntimeException e) {
            return new Response(request.getUuid(), e);
        }
    }

    private LobbyInterface getLobby() {
        return player.getLobby().orElseThrow(() -> new IllegalStateException("You are not in a lobby"));
    }

    private void handleIOException(IOException e) {
        System.err.println("An IOException occurred while trying to communicate with the server.");
        e.printStackTrace(System.err);
        controller.handlePlayerDisconnection(player);
    }

    @Override
    public void notifyEvent(Event event) {
        eventQueue.notifyEvent(event);
    }

    @Override
    public void registerNickname(String myNickname) {
        controller.registerNickname(myNickname, this);
    }

    @Override
    public void requestNewGame(Level level, int playerN) {
        controller.newGame(player, level, playerN);
    }

    @Override
    public void drawCard() {
        getLobby().drawCard(player);
    }

    @Override
    public void joinLobby(UUID lobbyID) {
        controller.joinLobby(player, lobbyID);
    }

    @Override
    public void leaveLobby() {
        controller.leaveLobby(player);
    }

    @Override
    public void requestRandComponent() {
        getLobby().requestRandComponent(player);
    }

    @Override
    public void requestComponent(int componentID) {
        getLobby().requestComponent(player, componentID);
    }

    @Override
    public void rejectComponent() {
        getLobby().rejectComponent(player);
    }

    @Override
    public void stashComponent() {
        getLobby().stashComponent(player);
    }

    @Override
    public void grabPlacedComponent() {
        getLobby().grabPlacedComponent(player);
    }

    @Override
    public void grabStashedComponent(int index) {
        getLobby().grabStashedComponent(player, index);
    }

    @Override
    public void placeComponent(Point point, Direction orientation) {
        getLobby().placeComponent(player, point, orientation);
    }

    @Override
    public void flipHourglass() {
        getLobby().flipHourglass(player);
    }

    @Override
    public void placeShipOnFlightBoard(int startingPosition) {
        getLobby().placeShipOnFlightBoard(player, startingPosition);
    }

    @Override
    public void placeShipOnFlightBoard() {
        getLobby().placeShipOnFlightBoard(player);
    }

    @Override
    public void acquireForecast(int deckIndex) {
        getLobby().acquireForecast(player, deckIndex);
    }

    @Override
    public void releaseForecast() {
        getLobby().releaseForecast(player);
    }

    @Override
    public void removeComponent(Point point) {
        getLobby().removeComponent(player, point);
    }

    @Override
    public void chooseShipPiece(int pieceIndex) {
        getLobby().chooseShipPiece(player, pieceIndex);
    }

    @Override
    public void initializeCabin(Point point, CrewType crewType) {
        getLobby().initializeCabin(player, point, crewType);
    }

    @Override
    public void activateComponent(Point point) {
        getLobby().activateComponent(player, point);
    }

    @Override
    public void loseCrew(Point point) {
        getLobby().loseCrew(player, point);
    }

    @Override
    public void grabReward() {
        getLobby().grabReward(player);
    }

    @Override
    public void placeGoods(Point point, GoodsType goodsType) {
        getLobby().placeGoods(player, point, goodsType);
    }

    @Override
    public void removeGoods(Point point, GoodsType goodsType) {
        getLobby().removeGoods(player, point, goodsType);
    }

    @Override
    public void loseGoods(Point point) {
        getLobby().loseGoods(player, point);
    }

    @Override
    public void useBattery(Point point) {
        getLobby().useBattery(player, point);
    }

    @Override
    public void choosePlanet(int choice) {
        getLobby().choosePlanet(player, choice);
    }

    @Override
    public void goNext() {
        getLobby().goNext(player);
    }

    @Override
    public void giveUp() {
        getLobby().giveUp(player);
    }
}
