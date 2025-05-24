package it.polimi.ingsw.galaxytruckers.network.client.rmi;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.StatType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.network.client.ClientControllerInterface;
import it.polimi.ingsw.galaxytruckers.network.server.rmi.RemoteServer;
import it.polimi.ingsw.galaxytruckers.network.server.rmi.RemoteController;
import it.polimi.ingsw.galaxytruckers.network.client.VirtualServer;
import it.polimi.ingsw.galaxytruckers.serverController.events.Event;
import it.polimi.ingsw.galaxytruckers.view.enums.ProjectileType;

import java.awt.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
        } catch (RemoteException e) {
            throw new RuntimeException("Failed to locate RMI server", e);
            //TODO: define a better way to handle failed server connection
        } catch (NotBoundException e) {
            throw new RuntimeException("Failed to locate RMI server", e);
            //TODO: define a better way to handle NotBoundException
        }
    }

    public void setClientController(ClientControllerInterface clientController) {
        this.clientController =  clientController;
    }

    private void handleNetworkError(RemoteException e) {
        //TODO: define a way to handle network errors
        throw new RuntimeException("Failed to handle network exception", e);
    }

    private void ping() {
        try {
            remoteController.ping();
        } catch (RemoteException e) {
            handleNetworkError(e);
        }
    }

    // VirtualServer

    @Override
    public void registerNickname(String myNickname) {
        try {
            this.remoteController = server.registerNickname(this, myNickname);
            this.scheduler.scheduleAtFixedRate(this::ping,5,5, TimeUnit.SECONDS);
        } catch (RemoteException e) {
            handleNetworkError(e);
        }
    }

    @Override
    public void requestNewGame(Level level, int playerN) {
        try {
            remoteController.newGame(level, playerN);
        } catch (RemoteException e) {
            handleNetworkError(e);
        }
    }

    @Override
    public void drawCard() {
        try {
            remoteController.drawCard();
        } catch (RemoteException e) {
            handleNetworkError(e);
        }
    }

    @Override
    public void joinLobby(UUID lobbyID) {
        try {
            remoteController.joinLobby(lobbyID);
        } catch (RemoteException e) {
            handleNetworkError(e);
        }
    }

    @Override
    public void leaveLobby(String nickname) {
        try {
            remoteController.leaveLobby();
        } catch (RemoteException e) {
            handleNetworkError(e);
        }
    }

    @Override
    public void requestRandComponent() {
        try {
            remoteController.requestRandComponent();
        } catch (RemoteException e) {
            handleNetworkError(e);
        }
    }

    @Override
    public void requestComponent(int componentID) {
        try {
            remoteController.requestComponent(componentID);
        } catch (RemoteException e) {
            handleNetworkError(e);
        }
    }

    @Override
    public void rejectComponent() {
        try {
            remoteController.rejectComponent();
        } catch (RemoteException e) {
            handleNetworkError(e);
        }
    }

    @Override
    public void stashComponent() {
        try {
            remoteController.stashComponent();
        } catch (RemoteException e) {
            handleNetworkError(e);
        }
    }

    @Override
    public void grabStashedComponent(int index) {
        try {
            remoteController.grabStashedComponent(index);
        } catch (RemoteException e) {
            handleNetworkError(e);
        }
    }

    @Override
    public void placeComponent(Point point, int orientation) {
        try {
            remoteController.placeComponent(point, orientation);
        } catch (RemoteException e) {
            handleNetworkError(e);
        }
    }

    @Override
    public void flipHourglass() {
        try {
            remoteController.flipHourglass();
        } catch (RemoteException e) {
            handleNetworkError(e);
        }
    }

    @Override
    public void placeShipOnFlightBoard(int startingPosition) {
        try {
            remoteController.placeShipOnFlightBoard(startingPosition);
        } catch (RemoteException e) {
            handleNetworkError(e);
        }
    }

    @Override
    public void acquireForecast(int deckIndex) {
        try {
            remoteController.acquireForecast(deckIndex);
        } catch (RemoteException e) {
            handleNetworkError(e);
        }
    }

    @Override
    public void releaseForecast() {
        try {
            remoteController.releaseForecast();
        } catch (RemoteException e) {
            handleNetworkError(e);
        }
    }

    @Override
    public void removeComponent(Point point) {
        try {
            remoteController.removeComponent(point);
        } catch (RemoteException e) {
            handleNetworkError(e);
        }
    }

    @Override
    public void chooseShipPiece(int pieceIndex) {
        try {
            remoteController.chooseShipPiece(pieceIndex);
        } catch (RemoteException e) {
            handleNetworkError(e);
        }
    }

    @Override
    public void initializeCabin(Point point, CrewType crewType) {
        try {
            remoteController.initializeCabin(point, crewType);
        } catch (RemoteException e) {
            handleNetworkError(e);
        }
    }

    @Override
    public void activateComponent(Point point) {
        try {
            remoteController.activateComponent(point);
        } catch (RemoteException e) {
            handleNetworkError(e);
        }
    }

    @Override
    public void loseCrew(Point point) {
        try {
            remoteController.loseCrew(point);
        } catch (RemoteException e) {
            handleNetworkError(e);
        }
    }

    @Override
    public void grabReward(boolean rewardGrabbed) {
        try {
            remoteController.grabReward(rewardGrabbed);
        } catch (RemoteException e) {
            handleNetworkError(e);
        }
    }

    @Override
    public void placeGoods(Point point, GoodsType goodsType) {
        try {
            remoteController.placeGoods(point, goodsType);
        } catch (RemoteException e) {
            handleNetworkError(e);
        }
    }

    @Override
    public void removeGoods(Point point, GoodsType goodsType) {
        try {
            remoteController.removeGoods(point, goodsType);
        } catch (RemoteException e) {
            handleNetworkError(e);
        }
    }

    @Override
    public void loseGoods(Point point) {
        try {
            remoteController.loseGoods(point);
        } catch (RemoteException e) {
            handleNetworkError(e);
        }
    }

    @Override
    public void useBattery(Point point) {
        try {
            remoteController.useBattery(point);
        } catch (RemoteException e) {
            handleNetworkError(e);
        }
    }

    @Override
    public void choosePlanet(int choice) {
        try {
            remoteController.choosePlanet(choice);
        } catch (RemoteException e) {
            handleNetworkError(e);
        }
    }

    @Override
    public void goNext(String nickname) {
        try {
            remoteController.goNext();
        } catch (RemoteException e) {
            handleNetworkError(e);
        }
    }

    @Override
    public void giveUp(String nickname) {
        try {
            remoteController.giveUp();
        } catch (RemoteException e) {
            handleNetworkError(e);
        }
    }

    // --- RemoteClient ---


    @Override
    public void notifyEvent(Event event) throws RemoteException {
        //TODO: implement this method (create EventHandler ClientSide)
    }
}
