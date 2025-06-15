package it.polimi.ingsw.galaxytruckers.network.server.socket;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.network.client.VirtualServer;
import it.polimi.ingsw.galaxytruckers.network.messages.Message;
import it.polimi.ingsw.galaxytruckers.network.messages.Ping;
import it.polimi.ingsw.galaxytruckers.network.messages.Request;
import it.polimi.ingsw.galaxytruckers.network.messages.Response;
import it.polimi.ingsw.galaxytruckers.network.server.ClientHandler;
import it.polimi.ingsw.galaxytruckers.network.server.SessionManager;
import it.polimi.ingsw.galaxytruckers.serverController.ServerControllerInterface;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.Event;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.LobbyInterface;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;
import it.polimi.ingsw.galaxytruckers.view.Direction;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.UUID;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

class SocketClientHandler implements VirtualServer, ClientHandler {
    private Player player;
    private final ServerControllerInterface controller;
    private LobbyInterface lobby;
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;

    private Thread updateThread = null;
    private boolean isUpdating = false;
    private Thread requestThread = null;
    private boolean isRunning = false;

    private final BlockingDeque<Event> eventQueue = new LinkedBlockingDeque<>();

    public SocketClientHandler(ObjectInputStream inputStream, ObjectOutputStream outputStream, Player player, ServerControllerInterface controller) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.player = player;
        this.controller = controller;
    }

    public void start() {
        isRunning = true;
        requestThread = new Thread(this::requestTask, "RequestThread");
        requestThread.start();
        isUpdating = true;
        updateThread = new Thread(this::updateTask, "UpdateThread");
        updateThread.start();
        System.out.println("Started Socket Client Handler");
    }

    @Override
    public void stop() {
        isRunning = false;
        isUpdating = false;
        requestThread.interrupt();
        updateThread.interrupt();
    }


    public void stopUpdateThread() {
        isUpdating = false;
        updateThread = null;
    }

    public void stopRequestThread() {
        isRunning = false;
        requestThread = null;
    }

    private void updateTask() {
        while (isUpdating) {
            Event event = null;
            try {
                event = eventQueue.take();
                outputStream.writeObject(event);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (IOException e) {
                eventQueue.offerFirst(event);
                handleIOException(e);
            }
        }
    }

    private void requestTask() {
        while (isRunning) {
            try {
                Message message = (Message) inputStream.readObject();
                switch (message) {
                    case Request request -> {
                        Response response = runRequest(request);
                        outputStream.writeObject(response);
                        outputStream.flush();
                    }
                    case Ping ping -> {
                        SessionManager.getInstance().ping(player);
                    }
                    default -> {
                        System.err.println("ERROR: the server received a message of type " + message.getClass().getName());
                        stopRequestThread();
                        stopUpdateThread();
                    }
                }
            } catch (IOException e) {
                handleIOException(e);
            } catch (ClassNotFoundException e) {
                System.err.println("Class not found, check that the socket classes are configured correctly");
                stopRequestThread();
            }
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

    private void handleIOException(IOException e) {
        System.err.println("An IOException occurred while trying to communicate with the server.");
        e.printStackTrace(System.err);
        stopRequestThread();
        stopUpdateThread();
    }

    private void checkInLobby() {
        if (lobby == null) {
            throw new IllegalStateException("You have not joined a lobby yet");
        }
    }

    private void checkNotInLobby() {
        if  (lobby != null) {
            throw new IllegalStateException("You already joined a lobby");
        }
    }

    @Override
    public void notifyEvent(Event event) {
        eventQueue.offer(event);
    }

    @Override
    public void registerNickname(String myNickname) {
        throw new IllegalStateException("You have already registered nickname");
    }

    @Override
    public void requestNewGame(Level level, int playerN) {
        checkNotInLobby();
        lobby = controller.newGame(player,level,playerN);
    }

    @Override
    public void drawCard() {
        checkInLobby();
        lobby.drawCard(player);
    }

    @Override
    public void joinLobby(UUID lobbyID) {
        checkNotInLobby();
        lobby =  controller.joinLobby(player,lobbyID);
    }

    @Override
    public void leaveLobby() {
        checkInLobby();
        controller.leaveLobby(player);
        lobby = null;
    }

    @Override
    public void requestRandComponent() {
        checkInLobby();
        lobby.requestRandComponent(player);
    }

    @Override
    public void requestComponent(int componentID) {
        checkInLobby();
        lobby.requestComponent(player,componentID);
    }

    @Override
    public void rejectComponent() {
        checkInLobby();
        lobby.rejectComponent(player);
    }

    @Override
    public void stashComponent() {
        checkInLobby();
        lobby.stashComponent(player);
    }

    @Override
    public void grabPlacedComponent() {
        checkInLobby();
        lobby.grabPlacedComponent(player);
    }

    @Override
    public void grabStashedComponent(int index) {
        checkInLobby();
        lobby.grabStashedComponent(player,index);
    }

    @Override
    public void placeComponent(Point point, Direction orientation) {
        checkInLobby();
        lobby.placeComponent(player, point, orientation);
    }

    @Override
    public void flipHourglass() {
        checkInLobby();
        lobby.flipHourglass(player);
    }

    @Override
    public void placeShipOnFlightBoard(int startingPosition) {
        checkInLobby();
        lobby.placeShipOnFlightBoard(player,startingPosition);
    }

    @Override
    public void acquireForecast(int deckIndex) {
        checkInLobby();
        lobby.acquireForecast(player,deckIndex);
    }

    @Override
    public void releaseForecast() {
        checkInLobby();
        lobby.releaseForecast(player);
    }

    @Override
    public void removeComponent(Point point) {
        checkInLobby();
        lobby.removeComponent(player,point);
    }

    @Override
    public void chooseShipPiece(int pieceIndex) {
        checkInLobby();
        lobby.chooseShipPiece(player,pieceIndex);
    }

    @Override
    public void initializeCabin(Point point, CrewType crewType) {
        checkInLobby();
        lobby.initializeCabin(player,point,crewType);
    }

    @Override
    public void activateComponent(Point point) {
        checkInLobby();
        lobby.activateComponent(player,point);
    }

    @Override
    public void loseCrew(Point point) {
        checkInLobby();
        lobby.loseCrew(player,point);
    }

    @Override
    public void grabReward(boolean rewardGrabbed) {
        checkInLobby();
        lobby.grabReward(player,rewardGrabbed);
    }

    @Override
    public void placeGoods(Point point, GoodsType goodsType) {
        checkInLobby();
        lobby.placeGoods(player,point,goodsType);
    }

    @Override
    public void removeGoods(Point point, GoodsType goodsType) {
        checkInLobby();
        lobby.removeGoods(player,point,goodsType);
    }

    @Override
    public void loseGoods(Point point) {
        checkInLobby();
        lobby.loseGoods(player,point);
    }

    @Override
    public void useBattery(Point point) {
        checkInLobby();
        lobby.useBattery(player,point);
    }

    @Override
    public void choosePlanet(int choice) {
        checkInLobby();
        lobby.choosePlanet(player,choice);
    }

    @Override
    public void goNext() {
        checkInLobby();
        lobby.goNext(player);
    }

    @Override
    public void giveUp() {
        checkInLobby();
        lobby.giveUp(player);
    }
}
