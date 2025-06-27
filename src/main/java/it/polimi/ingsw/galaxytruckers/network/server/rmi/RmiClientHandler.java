package it.polimi.ingsw.galaxytruckers.network.server.rmi;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.shared.enums.GoodsType;
import it.polimi.ingsw.galaxytruckers.shared.enums.Level;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.network.client.rmi.RemoteClient;
import it.polimi.ingsw.galaxytruckers.network.server.ClientEventQueue;
import it.polimi.ingsw.galaxytruckers.network.server.ClientHandler;
import it.polimi.ingsw.galaxytruckers.network.server.SessionManager;
import it.polimi.ingsw.galaxytruckers.server.controller.ServerControllerInterface;
import it.polimi.ingsw.galaxytruckers.server.controller.events.types.Event;
import it.polimi.ingsw.galaxytruckers.server.controller.lobby.LobbyInterface;
import it.polimi.ingsw.galaxytruckers.server.controller.lobby.Player;
import it.polimi.ingsw.galaxytruckers.shared.enums.Direction;

import java.awt.*;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.UUID;

/**
 * Handles communication with a client over RMI.
 * It processes requests from the client and sends events back to the client.
 */
public class RmiClientHandler extends UnicastRemoteObject implements RemoteController, ClientHandler {
    private final RemoteClient remoteClient;
    private final ServerControllerInterface controller;
    private Runnable afterEach = () -> {
    };

    private final Thread updateThread;
    private boolean running = false;
    private final ClientEventQueue eventQueue = new ClientEventQueue();

    private Player player;

    private final Object requestLock = new Object();
    private final Object eventLock = new Object();

    /**
     * Creates a new RmiClientHandler that handles communication with a client.
     *
     * @param remoteClient the remote client interface for sending events
     * @param controller   the controller that manages server operations
     * @throws RemoteException if there is an error during RMI export
     */
    public RmiClientHandler(RemoteClient remoteClient, ServerControllerInterface controller) throws RemoteException {
        super();
        this.remoteClient = remoteClient;
        this.controller = controller;
        this.updateThread = new Thread(this::runUpdateThread, "UpdateThread");
    }

    /**
     * Starts the update thread to send events to the client.
     * This method should be called after setting the player.
     */
    public void start() {
        synchronized (eventLock) {
            if (!running) {
                running = true;
                updateThread.start();
            }
        }
    }

    @Override
    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public void pauseEvents() {
        eventQueue.pause();
    }

    @Override
    public void stop() {
        try {
            System.out.println("Stopping RmiClientHandler");
            synchronized (requestLock) {
                UnicastRemoteObject.unexportObject(this, false);
            }
            running = false;
            synchronized (eventLock) {
                updateThread.interrupt();
            }
        } catch (NoSuchObjectException e) {
            System.out.println("The RMI client is not currently exported");
        }
    }

    private void handleNetworkError(RemoteException e) {
        System.out.println("WARNING: Failed to contact player " + player.getNickname() + "\n" +
                "A remote exception was thrown: " + e.getMessage());
        controller.handlePlayerDisconnection(player);
    }

    private void runUpdateThread() {
        while (running) {
            try {
                Event event = eventQueue.poll();
                synchronized (eventLock) {
                    remoteClient.notifyEvent(event);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (RemoteException e) {
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
        SessionManager.getInstance().ping(player);
    }

    // VirtualClient

    @Override
    public void notifyEvent(Event event) {
        eventQueue.notifyEvent(event);
    }

    // RemoteController

    @Override
    public void newGame(Level level, int numPlayers) throws RemoteException {
        synchronized (requestLock) {
            controller.newGame(player, level, numPlayers);
        }
    }

    @Override
    public void joinLobby(UUID lobbyID) throws RemoteException {
        synchronized (requestLock) {
            controller.joinLobby(player, lobbyID);
        }
    }

    @Override
    public void leaveLobby() throws RemoteException {
        synchronized (requestLock) {
            if (player.getLobby().isPresent()) controller.leaveLobby(player);
            else throw new IllegalStateException("You are not in a lobby yet");
        }
    }

    @Override
    public void requestRandComponent() throws RemoteException {
        synchronized (requestLock) {
            getLobby().requestRandComponent(player);
        }
    }

    @Override
    public void requestComponent(int componentID) throws RemoteException {
        synchronized (requestLock) {
            getLobby().requestComponent(player, componentID);
        }
    }

    @Override
    public void rejectComponent() throws RemoteException {
        synchronized (requestLock) {
            getLobby().rejectComponent(player);
        }
    }

    @Override
    public void stashComponent() throws RemoteException {
        synchronized (requestLock) {
            getLobby().stashComponent(player);
        }
    }

    @Override
    public void grabPlacedComponent() throws RemoteException {
        synchronized (requestLock) {
            getLobby().grabPlacedComponent(player);
        }
    }

    @Override
    public void grabStashedComponent(int index) throws RemoteException {
        synchronized (requestLock) {
            getLobby().grabStashedComponent(player, index);
        }
    }

    @Override
    public void placeComponent(Point point, Direction orientation) throws RemoteException {
        synchronized (requestLock) {
            getLobby().placeComponent(player, point, orientation);
        }
    }

    @Override
    public void flipHourglass() throws RemoteException {
        synchronized (requestLock) {
            getLobby().flipHourglass(player);
        }
    }

    @Override
    public void placeShipOnFlightBoard(int startingPosition) throws RemoteException {
        synchronized (requestLock) {
            getLobby().placeShipOnFlightBoard(player, startingPosition);
        }
    }

    @Override
    public void placeShipOnFlightBoard() throws RemoteException {
        synchronized (requestLock) {
            getLobby().placeShipOnFlightBoard(player);
        }
    }

    @Override
    public void acquireForecast(int deckIndex) throws RemoteException {
        synchronized (requestLock) {
            getLobby().acquireForecast(player, deckIndex);
        }
    }

    @Override
    public void releaseForecast() throws RemoteException {
        synchronized (requestLock) {
            getLobby().releaseForecast(player);
        }
    }

    @Override
    public void removeComponent(Point point) throws RemoteException {
        synchronized (requestLock) {
            getLobby().removeComponent(player, point);
        }
    }

    @Override
    public void chooseShipPiece(int pieceIndex) throws RemoteException {
        synchronized (requestLock) {
            getLobby().chooseShipPiece(player, pieceIndex);
        }
    }

    @Override
    public void initializeCabin(Point point, CrewType crewType) throws RemoteException {
        synchronized (requestLock) {
            getLobby().initializeCabin(player, point, crewType);
        }
    }

    @Override
    public void drawCard() throws RemoteException {
        synchronized (requestLock) {
            getLobby().drawCard(player);
        }
    }

    @Override
    public void activateComponent(Point point) throws RemoteException {
        synchronized (requestLock) {
            getLobby().activateComponent(player, point);
        }
    }

    @Override
    public void loseCrew(Point point) throws RemoteException {
        synchronized (requestLock) {
            getLobby().loseCrew(player, point);
        }
    }

    @Override
    public void grabReward() throws RemoteException {
        synchronized (requestLock) {
            getLobby().grabReward(player);
        }
    }

    @Override
    public void placeGoods(Point point, GoodsType goodsType) throws RemoteException {
        synchronized (requestLock) {
            getLobby().placeGoods(player, point, goodsType);
        }
    }

    @Override
    public void removeGoods(Point point, GoodsType goodsType) throws RemoteException {
        synchronized (requestLock) {
            getLobby().removeGoods(player, point, goodsType);
        }
    }

    @Override
    public void loseGoods(Point point) throws RemoteException {
        synchronized (requestLock) {
            getLobby().loseGoods(player, point);
        }
    }

    @Override
    public void useBattery(Point point) throws RemoteException {
        synchronized (requestLock) {
            getLobby().useBattery(player, point);
        }
    }

    @Override
    public void choosePlanet(int choice) throws RemoteException {
        synchronized (requestLock) {
            getLobby().choosePlanet(player, choice);
        }
    }

    @Override
    public void goNext() throws RemoteException {
        synchronized (requestLock) {
            getLobby().goNext(player);
        }
    }

    @Override
    public void giveUp() throws RemoteException {
        synchronized (requestLock) {
            getLobby().giveUp(player);
        }
    }

    private LobbyInterface getLobby() {
        return player.getLobby().orElseThrow(() -> new IllegalStateException("You are not in a lobby"));
    }

    @VisibleForTesting
    protected boolean isRunning() {
        return running;
    }

    @VisibleForTesting
    protected void setAfterEach(Runnable afterEach) {
        this.afterEach = afterEach;
    }

}

