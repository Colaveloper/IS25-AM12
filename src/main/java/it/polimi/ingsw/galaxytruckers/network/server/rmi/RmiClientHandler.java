package it.polimi.ingsw.galaxytruckers.network.server.rmi;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.network.client.rmi.RemoteClient;
import it.polimi.ingsw.galaxytruckers.network.server.ClientHandler;
import it.polimi.ingsw.galaxytruckers.network.server.SessionManager;
import it.polimi.ingsw.galaxytruckers.serverController.ServerControllerInterface;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.Event;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.LobbyInterface;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;
import it.polimi.ingsw.galaxytruckers.view.Direction;

import java.awt.*;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.UUID;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class RmiClientHandler extends UnicastRemoteObject implements RemoteController, ClientHandler {
    private final RemoteClient remoteClient;
    private final ServerControllerInterface controller;
    private SessionManager sessionManager;
    private Runnable afterEach = () -> {};

    private Thread updateThread;
    private boolean running = false;
    private final BlockingDeque<Event> events;

    private final Player player;

    public RmiClientHandler(RemoteClient remoteClient, Player player, ServerControllerInterface controller) throws RemoteException{
        super();
        this.remoteClient = remoteClient;
        this.player = player;
        this.controller = controller;
        this.updateThread = null;
        this.events = new LinkedBlockingDeque<>();
        this.sessionManager = SessionManager.getInstance();
    }

    public void start() {
        running = true;
        updateThread = new Thread(this::runUpdateThread,"UpdateThread");
        updateThread.start();
    }

    @Override
    public void stop() {
        running = false;
        this.updateThread = null;
        try {
            System.out.println("Stopping RmiClientHandler");
            UnicastRemoteObject.unexportObject(this, true);
        } catch (NoSuchObjectException e) {
            System.out.println("The RMI client is not currently exported");
        }
    }

    private void handleNetworkError(RemoteException e) {
        System.out.println("WARNING: Failed to contact player " + player.getNickname() + "\n" +
                "A remote exception was thrown: " +  e.getMessage());
        controller.handlePlayerDisconnection(player);
        stop();
    }

    private void handleInternalError() {
        System.err.println("ERROR: The update queue for " + player.getNickname() +
                " has failed to handle all updates");
        stop();
    }

    protected void runUpdateThread() {
        Event event = null;
        while (running) {
            try {
                event = events.takeFirst();
                remoteClient.notifyEvent(event);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (RemoteException e) {
                events.offerFirst(event);
                handleNetworkError(e);
            } catch (RuntimeException e) {
                System.out.println("An error occurred on the client of " + player.getNickname());
                e.printStackTrace(System.err);
            } finally {
                afterEach.run();
            }
        }
    }

    @Override
    public void ping() throws RemoteException {
        sessionManager.ping(player);
    }

    // VirtualClient

    @Override
    public void notifyEvent(Event event) {
        events.offer(event);
    }

    // RemoteController

    @Override
    public void newGame(Level level, int numPlayers) throws RemoteException {
        controller.newGame(player, level, numPlayers);
    }

    @Override
    public void joinLobby(UUID lobbyID) throws RemoteException {
        controller.joinLobby(player, lobbyID);
    }

    @Override
    public void leaveLobby() throws RemoteException {
        if (player.getLobby().isPresent()) controller.leaveLobby(player);
        else throw new IllegalStateException("You are not in a lobby yet");
    }

    @Override
    public void requestRandComponent() throws RemoteException {
        getLobby().requestRandComponent(player);
    }

    @Override
    public void requestComponent(int componentID) throws RemoteException {
        getLobby().requestComponent(player, componentID);
    }

    @Override
    public void rejectComponent() throws RemoteException {
        getLobby().rejectComponent(player);
    }

    @Override
    public void stashComponent() throws RemoteException {
        getLobby().stashComponent(player);
    }

    @Override
    public void grabPlacedComponent() throws RemoteException {
        getLobby().grabPlacedComponent(player);
    }

    @Override
    public void grabStashedComponent(int index) throws RemoteException {
        getLobby().grabStashedComponent(player, index);
    }

    @Override
    public void placeComponent(Point point, Direction orientation) throws RemoteException {
        getLobby().placeComponent(player, point, orientation);
    }

    @Override
    public void flipHourglass() throws RemoteException {
        getLobby().flipHourglass(player);
    }

    @Override
    public void placeShipOnFlightBoard(int startingPosition) throws RemoteException {
        getLobby().placeShipOnFlightBoard(player, startingPosition);
    }

    @Override
    public void acquireForecast(int deckIndex) throws RemoteException {
        getLobby().acquireForecast(player, deckIndex);
    }

    @Override
    public void releaseForecast() throws RemoteException {
        getLobby().releaseForecast(player);
    }

    @Override
    public void removeComponent(Point point) throws RemoteException {
        getLobby().removeComponent(player, point);
    }

    @Override
    public void chooseShipPiece(int pieceIndex) throws RemoteException {
        getLobby().chooseShipPiece(player, pieceIndex);
    }

    @Override
    public void initializeCabin(Point point, CrewType crewType) throws RemoteException {
        getLobby().initializeCabin(player, point, crewType);
    }

    @Override
    public void drawCard() throws RemoteException {
        getLobby().drawCard(player);
    }

    @Override
    public void activateComponent(Point point) throws RemoteException {
        getLobby().activateComponent(player, point);
    }

    @Override
    public void loseCrew(Point point) throws RemoteException {
        getLobby().loseCrew(player, point);
    }

    @Override
    public void grabReward(boolean rewardGrabbed) throws RemoteException {
        getLobby().grabReward(player, rewardGrabbed);
    }

    @Override
    public void placeGoods(Point point, GoodsType goodsType) throws RemoteException {
        getLobby().placeGoods(player, point, goodsType);
    }

    @Override
    public void removeGoods(Point point, GoodsType goodsType) throws RemoteException {
        getLobby().removeGoods(player, point, goodsType);
    }

    @Override
    public void loseGoods(Point point) throws RemoteException {
        getLobby().loseGoods(player, point);
    }

    @Override
    public void useBattery(Point point) throws RemoteException {
        getLobby().useBattery(player, point);
    }

    @Override
    public void choosePlanet(int choice) throws RemoteException {
        getLobby().choosePlanet(player, choice);
    }

    @Override
    public void goNext() throws RemoteException {
        getLobby().goNext(player);
    }

    @Override
    public void giveUp() throws RemoteException {
        getLobby().giveUp(player);
    }

    private LobbyInterface getLobby() {
        return player.getLobby().orElseThrow(() -> new IllegalStateException("You are not in a lobby"));
    }

    @VisibleForTesting
    protected void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @VisibleForTesting
    protected boolean isRunning() {
        return running;
    }

    @VisibleForTesting
    protected void setAfterEach(Runnable afterEach) {
        this.afterEach = afterEach;
    }

    @VisibleForTesting
    protected Thread getUpdateThread() {
        return updateThread;
    }
}

