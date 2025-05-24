package it.polimi.ingsw.galaxytruckers.network.server.socket;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.network.client.VirtualServer;
import it.polimi.ingsw.galaxytruckers.network.messages.Request;
import it.polimi.ingsw.galaxytruckers.network.messages.Response;
import it.polimi.ingsw.galaxytruckers.network.server.VirtualClient;
import it.polimi.ingsw.galaxytruckers.serverController.events.Event;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.UUID;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

class SocketClientHandler implements VirtualClient, VirtualServer {
    private Player player;
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;

    private Thread updateThread = null;
    private boolean isUpdating = false;
    private Thread requestThread = null;
    private boolean isRunning = false;

    private final BlockingDeque<Event> eventQueue = new LinkedBlockingDeque<>();

    public SocketClientHandler(ObjectInputStream inputStream, ObjectOutputStream outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    public void start() {
        if (!isRunning) {
            requestThread = new Thread(this::requestTask, "RequestThread");
            requestThread.start();
        }
        if (!isUpdating) {
            updateThread = new Thread(this::updateTask, "UpdateThread");
            updateThread.start();
        }
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
                Request request = (Request) inputStream.readObject();
                try {
                    request.execute(this);
                    outputStream.writeObject(new Response(request.getUuid()));
                } catch (RuntimeException e) {
                    outputStream.writeObject(new Response(request.getUuid(), e));
                }
            } catch (IOException e) {
                handleIOException(e);
            } catch (ClassNotFoundException e) {
                System.err.println("Class not found, check that the socket classes are configured correctly");
                stopRequestThread();
            }
        }
    }

    @Override
    public void notifyEvent(Event event) {
        eventQueue.offer(event);
    }


    //TODO: implement VirtualServer methods
    @Override
    public void registerNickname(String myNickname) {

    }

    @Override
    public void requestNewGame(Level level, int playerN) {

    }

    @Override
    public void drawCard() {

    }

    @Override
    public void joinLobby(UUID lobbyID) {

    }

    @Override
    public void leaveLobby(String nickname) {

    }

    @Override
    public void requestRandComponent() {

    }

    @Override
    public void requestComponent(int componentID) {

    }

    @Override
    public void rejectComponent() {

    }

    @Override
    public void stashComponent() {

    }

    @Override
    public void grabStashedComponent(int index) {

    }

    @Override
    public void placeComponent(Point point, int orientation) {

    }

    @Override
    public void flipHourglass() {

    }

    @Override
    public void placeShipOnFlightBoard(int startingPosition) {

    }

    @Override
    public void acquireForecast(int deckIndex) {

    }

    @Override
    public void releaseForecast() {

    }

    @Override
    public void removeComponent(Point point) {

    }

    @Override
    public void chooseShipPiece(int pieceIndex) {

    }

    @Override
    public void initializeCabin(Point point, CrewType crewType) {

    }

    @Override
    public void activateComponent(Point point) {

    }

    @Override
    public void loseCrew(Point point) {

    }

    @Override
    public void grabReward(boolean rewardGrabbed) {

    }

    @Override
    public void placeGoods(Point point, GoodsType goodsType) {

    }

    @Override
    public void removeGoods(Point point, GoodsType goodsType) {

    }

    @Override
    public void loseGoods(Point point) {

    }

    @Override
    public void useBattery(Point point) {

    }

    @Override
    public void choosePlanet(int choice) {

    }

    @Override
    public void goNext(String nickname) {

    }

    @Override
    public void giveUp(String nickname) {

    }

    public void handleIOException(IOException e) {
        System.err.println("An IOException occurred while trying to communicate with the server.");
        e.printStackTrace(System.err);
        stopRequestThread();
        stopUpdateThread();
    }
}
