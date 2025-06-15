package it.polimi.ingsw.galaxytruckers.network.client.rmi;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.network.client.ClientControllerInterface;
import it.polimi.ingsw.galaxytruckers.network.server.rmi.RemoteServer;
import it.polimi.ingsw.galaxytruckers.network.server.rmi.RemoteController;
import it.polimi.ingsw.galaxytruckers.network.client.VirtualServer;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.Event;

import java.awt.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RmiClient extends UnicastRemoteObject implements RemoteClient, VirtualServer {
    private RemoteServer server;
    private RemoteController remoteController;
    private ClientControllerInterface clientController;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public RmiClient() throws RemoteException {
        super();
    }

    public void start(String serverName, String serverAddress, int serverPort) {
        try {
            Registry registry = LocateRegistry.getRegistry(serverAddress, serverPort);
            this.server = (RemoteServer) registry.lookup(serverName);
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException("Failed to locate RMI server", e);
        }
    }

    public void setClientController(ClientControllerInterface clientController) {
        this.clientController = clientController;
    }

    private void handleNetworkError(RemoteException e) {
        //TODO: define a way to handle network errors
        throw new RuntimeException("Failed to handle network exception", e);
    }

    private void ping() {
        runRemoteMethod(() -> remoteController.ping());
    }

    // VirtualServer

    @Override
    public void registerNickname(String myNickname) {
        runRemoteMethod(() -> {
            this.remoteController = server.registerNickname(this, myNickname);
            this.scheduler.scheduleAtFixedRate(this::ping,50,50, TimeUnit.SECONDS); //todo set to 5 later
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
    public void grabReward(boolean rewardGrabbed) {
        runRemoteMethod(() -> remoteController.grabReward(rewardGrabbed));
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
            remoteRunnable.run();
        } catch (RemoteException e) {
            handleNetworkError(e);
        }
    }

    // --- RemoteClient ---


    @Override
    public void notifyEvent(Event event) throws RemoteException {
        clientController.notifyEvent(event);
        
    }

    @VisibleForTesting
    public ClientControllerInterface getClientController() {
        return clientController;
    }

    @VisibleForTesting
    public void setServerController(RemoteServer serverController) {
        this.server = serverController;
    }

    @VisibleForTesting
    public RemoteServer getServer() {
        return server;
    }

    @VisibleForTesting
    public RemoteController getRemoteController() {
        return remoteController;
    }

    @VisibleForTesting
    public void setRemoteController(RemoteController remoteController) {
        this.remoteController = remoteController;
    }
}

@FunctionalInterface
interface RemoteRunnable {
    void run() throws RemoteException;
}
