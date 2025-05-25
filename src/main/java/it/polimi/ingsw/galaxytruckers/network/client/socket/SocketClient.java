package it.polimi.ingsw.galaxytruckers.network.client.socket;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.network.client.ClientControllerInterface;
import it.polimi.ingsw.galaxytruckers.network.client.VirtualServer;
import it.polimi.ingsw.galaxytruckers.network.messages.*;
import it.polimi.ingsw.galaxytruckers.network.server.VirtualClient;
import it.polimi.ingsw.galaxytruckers.serverController.events.Event;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

public class SocketClient implements VirtualServer, VirtualClient {
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private ClientControllerInterface controller;
    private boolean isRunning;

    private final ScheduledExecutorService scheduler =  Executors.newScheduledThreadPool(1);

    private Thread inputThread;

    private final Map<UUID, CompletableFuture<Response>> responses = new HashMap<>();

    public void setController(ClientControllerInterface controller) {
        this.controller = controller;
    }

    public void start(String ip, int port) {
        try {
            socket = new Socket(ip, port);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.flush();
            inputStream = new ObjectInputStream(socket.getInputStream());
            isRunning = true;
            inputThread = new Thread(this::inputThreadTask);
            inputThread.start();
            System.out.println("Started Socket Client");
        } catch (IOException e) {
            handleIOException(e);
        }
    }

    public void stop() {
        isRunning = false;
    }

    private void inputThreadTask() {
        while (isRunning) {
            try {
                Message message = (Message) inputStream.readObject();
                switch (message) {
                    case EventMessage eventMessage -> {
                        notifyEvent(eventMessage.event());
                    }
                    case Response response -> {
                        responses.get(response.getUuid()).complete(response);
                    }
                    case Message m -> {
                        System.err.println("Invalid message was received on the client: " + m.getClass().getName());
                        stop();
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Message class not found, probable misconfiguration");
            }
        }
    }

    private void handleIOException(IOException e) {
        System.err.println("IOException: " + e.getMessage());
        //TODO: more elaborate exception handling
    }

    private void sendRequest(Request request) {
        responses.put(request.getUuid(), new CompletableFuture<>());
        try {
            outputStream.writeObject(request);
            outputStream.flush();
            Response response = responses.get(request.getUuid()).get();
            if (response.isError()) throw new RuntimeException(response.getError());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            handleIOException(e);
        }
    }

    private void ping() {
        try {
            outputStream.writeObject(new Ping());
            outputStream.flush();
        } catch (IOException e) {
            handleIOException(e);
        }
    }

    @Override
    public void registerNickname(String myNickname) {
        sendRequest(new RegisterNickname(myNickname));
        scheduler.scheduleAtFixedRate(this::ping, 5,5, TimeUnit.SECONDS);
    }


    @Override
    public void requestNewGame(Level level, int playerN) {
        sendRequest(new RequestNewGame(level, playerN));
    }

    @Override
    public void joinLobby(UUID lobbyID) {

    }

    @Override
    public void leaveLobby() {

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
    public void drawCard() {

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
    public void goNext() {

    }

    @Override
    public void giveUp() {

    }

    @Override
    public void notifyEvent(Event event) {

    }
}
