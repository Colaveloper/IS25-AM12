package it.polimi.ingsw.galaxytruckers.network.client.rmi;

import it.polimi.ingsw.galaxytruckers.client.controller.ServerToClientInterface;
import it.polimi.ingsw.galaxytruckers.shared.enums.GoodsType;
import it.polimi.ingsw.galaxytruckers.shared.enums.Level;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.network.client.ServerHandler;
import it.polimi.ingsw.galaxytruckers.network.server.rmi.RemoteServer;
import it.polimi.ingsw.galaxytruckers.network.server.rmi.RemoteController;
import it.polimi.ingsw.galaxytruckers.shared.enums.Direction;
import it.polimi.ingsw.galaxytruckers.server.controller.events.types.Event;

import java.awt.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * RMIClient is the client-side implementation of the RMI protocol for Galaxy Truckers.
 * It allows the client to connect to a remote server and interact with it through remote method calls.
 */
public class RmiClient extends UnicastRemoteObject implements RemoteClient, ServerHandler {
    private RemoteServer server;
    private RemoteController remoteController;
    private ServerToClientInterface clientController;
    private final ScheduledExecutorService pingScheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> pingTask;

    private String serverName;
    private String serverAddress;
    private int serverPort;

    boolean connected = false;
    private final Object connectionLock = new Object();

    public RmiClient() throws RemoteException {
        super();
    }

    /**
     * Starts the RMI client by connecting to the specified server.
     *
     * @param serverName    the name of the RMI server
     * @param serverAddress the address of the RMI server
     * @param serverPort    the port of the RMI server
     */
    public void start(String serverName, String serverAddress, int serverPort) {
        try {
            this.serverName = serverName;
            this.serverAddress = serverAddress;
            this.serverPort = serverPort;
            connect();
        } catch (RemoteException e) {
            throw new RuntimeException("Failed to locate RMI server", e);
        }
    }

    @Override
    public boolean reconnect() {
        synchronized (connectionLock) {
            try {
                connect();
                return true;
            } catch (RemoteException e) {
                System.out.println("Failed to reconnect: " + e.getMessage());
            }
            return false;
        }
    }

    @Override
    public void dropConnection() {
        handleNetworkError(new RemoteException("Simulated Network Exception"));
    }

    private void connect() throws RemoteException {
        try {
            Registry registry = LocateRegistry.getRegistry(serverAddress, serverPort);
            this.server = (RemoteServer) registry.lookup(serverName);
            connected = true;
        } catch (NotBoundException e) {
            throw new RuntimeException("Server not bound, config error");
        }
    }

    /**
     * Sets the client controller for handling client-side events.
     *
     * @param clientController the client controller interface to set
     */
    public void setClientController(ServerToClientInterface clientController) {
        this.clientController = clientController;
    }

    private void handleNetworkError(RemoteException e) {
        synchronized (connectionLock) {
            System.err.println("RemoteException: " + e.getMessage());
            if (connected) {
                connected = false;
                pingTask.cancel(true);
                clientController.signalDisconnection();
            }
        }
    }

    private void ping() {
        synchronized (connectionLock) {
            runRemoteMethod(() -> remoteController.ping());
        }
    }

    // VirtualServer

    @Override
    public void registerNickname(String myNickname) {
        runRemoteMethod(() -> {
            this.remoteController = server.registerNickname(this, myNickname);
            this.pingTask = this.pingScheduler.scheduleAtFixedRate(this::ping, 5, 5, TimeUnit.SECONDS);
        });
    }

    @Override
    public void requestNewGame(Level level, int playerN) {
        runRemoteMethod(() -> remoteController.newGame(level, playerN));
    }

    @Override
    public void drawCard() {
        runRemoteMethod(() -> remoteController.drawCard());
    }

    @Override
    public void joinLobby(UUID lobbyID) {
        runRemoteMethod(() -> remoteController.joinLobby(lobbyID));
    }

    @Override
    public void leaveLobby() {
        runRemoteMethod(() -> remoteController.leaveLobby());
    }

    @Override
    public void requestRandComponent() {
        runRemoteMethod(() -> remoteController.requestRandComponent());
    }

    @Override
    public void requestComponent(int componentID) {
        runRemoteMethod(() -> remoteController.requestComponent(componentID));
    }

    @Override
    public void rejectComponent() {
        runRemoteMethod(() -> remoteController.rejectComponent());
    }

    @Override
    public void stashComponent() {
        runRemoteMethod(() -> remoteController.stashComponent());
    }

    @Override
    public void grabPlacedComponent() {
        runRemoteMethod(() -> remoteController.grabPlacedComponent());
    }

    @Override
    public void grabStashedComponent(int index) {
        runRemoteMethod(() -> remoteController.grabStashedComponent(index));
    }

    @Override
    public void placeComponent(Point point, Direction orientation) {
        runRemoteMethod(() -> remoteController.placeComponent(point, orientation));
    }

    @Override
    public void flipHourglass() {
        runRemoteMethod(() -> remoteController.flipHourglass());
    }

    @Override
    public void placeShipOnFlightBoard(int startingPosition) {
        runRemoteMethod(() -> remoteController.placeShipOnFlightBoard(startingPosition));
    }

    @Override
    public void placeShipOnFlightBoard() {
        runRemoteMethod(() -> remoteController.placeShipOnFlightBoard());
    }

    @Override
    public void acquireForecast(int deckIndex) {
        runRemoteMethod(() -> remoteController.acquireForecast(deckIndex));
    }

    @Override
    public void releaseForecast() {
        runRemoteMethod(() -> remoteController.releaseForecast());
    }

    @Override
    public void removeComponent(Point point) {
        runRemoteMethod(() -> remoteController.removeComponent(point));
    }

    @Override
    public void chooseShipPiece(int pieceIndex) {
        runRemoteMethod(() -> remoteController.chooseShipPiece(pieceIndex));
    }

    @Override
    public void initializeCabin(Point point, CrewType crewType) {
        runRemoteMethod(() -> remoteController.initializeCabin(point, crewType));
    }

    @Override
    public void activateComponent(Point point) {
        runRemoteMethod(() -> remoteController.activateComponent(point));
    }

    @Override
    public void loseCrew(Point point) {
        runRemoteMethod(() -> remoteController.loseCrew(point));
    }

    @Override
    public void grabReward() {
        runRemoteMethod(() -> remoteController.grabReward());
    }

    @Override
    public void placeGoods(Point point, GoodsType goodsType) {
        runRemoteMethod(() -> remoteController.placeGoods(point, goodsType));
    }

    @Override
    public void removeGoods(Point point, GoodsType goodsType) {
        runRemoteMethod(() -> remoteController.removeGoods(point, goodsType));
    }

    @Override
    public void loseGoods(Point point) {
        runRemoteMethod(() -> remoteController.loseGoods(point));
    }

    @Override
    public void useBattery(Point point) {
        runRemoteMethod(() -> remoteController.useBattery(point));
    }

    @Override
    public void choosePlanet(int choice) {
        runRemoteMethod(() -> remoteController.choosePlanet(choice));
    }

    @Override
    public void goNext() {
        runRemoteMethod(() -> remoteController.goNext());
    }

    @Override
    public void giveUp() {
        runRemoteMethod(() -> remoteController.giveUp());
    }

    private void runRemoteMethod(RemoteRunnable remoteRunnable) {
        try {
            synchronized (connectionLock) {
                if (!connected) return;
                remoteRunnable.run();
            }
        } catch (RemoteException e) {
            handleNetworkError(e);
        }
    }

    // --- RemoteClient ---


    @Override
    public void notifyEvent(Event event) throws RemoteException {
        clientController.notifyEvent(event);
    }

}

@FunctionalInterface
interface RemoteRunnable {
    void run() throws RemoteException;
}
