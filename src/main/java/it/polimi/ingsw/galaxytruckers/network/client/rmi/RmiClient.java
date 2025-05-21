package it.polimi.ingsw.galaxytruckers.network.client.rmi;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.FourColors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.StatType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.network.client.ClientControllerInterface;
import it.polimi.ingsw.galaxytruckers.network.server.rmi.RemoteServer;
import it.polimi.ingsw.galaxytruckers.network.server.rmi.RemoteController;
import it.polimi.ingsw.galaxytruckers.network.client.VirtualServer;
import it.polimi.ingsw.galaxytruckers.view.viewEnums.ProjectileType;

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

public class RmiClient extends UnicastRemoteObject implements RemoteClient, VirtualServer {
    private RemoteServer server;
    private RemoteController remoteController;
    private ClientControllerInterface clientController;

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

    // VirtualServer

    @Override
    public void registerNickname(String myNickname) {
        try {
            this.remoteController = server.registerNickname(this, myNickname);
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
    public void setupLobby(UUID lobbyId, Map<String, FourColors> playerColors) throws RemoteException {
        clientController.updateLobbyPlayers(playerColors);
    }

    @Override
    public void updateLobbyPlayers(Map<String, FourColors> playerColors) throws RemoteException {
        clientController.updateLobbyPlayers(playerColors);
    }

    @Override
    public void notifyStartBuilding(Level level, int playersN) throws RemoteException {
        clientController.notifyNewGame(level, playersN);
    }

    @Override
    public void notifyFaceDownComponentRequest(String playerName, int componentId) throws RemoteException {
        clientController.notifyFaceDownComponentRequest(playerName,componentId);
    }

    @Override
    public void notifyFaceUpComponentRequest(String playerName, int componentId) throws RemoteException {
        clientController.notifyFaceUpComponentRequest(playerName,componentId);
    }

    @Override
    public void notifyComponentRejection(String playerName, int componentId) throws RemoteException {
        clientController.notifyComponentRejection(playerName,componentId);
    }

    @Override
    public void notifyStashComponent(String playerName, List<Integer> stashComponentIds) throws RemoteException {
        clientController.notifyStashComponent(playerName,stashComponentIds);
    }

    @Override
    public void notifyGrabFromStash(String playerName, int componentId, List<Integer> stashComponentIds) throws RemoteException {
        clientController.notifyGrabFromStash(playerName,componentId,stashComponentIds);
    }

    @Override
    public void notifyHourglassFlipped(String playerName, boolean isLast) throws RemoteException {
        clientController.notifyHourglassFlipped(playerName,isLast);
    }

    @Override
    public void notifyShipMapUpdate(String playerName, int componentId, int rotation, Point position) throws RemoteException {
        clientController.notifyComponentPositioning(playerName,componentId,rotation,position);
    }

    @Override
    public void sendForecastDeck(List<Integer> deckCardIds) throws RemoteException {
        clientController.sendForecastDeck(deckCardIds);
    }

    @Override
    public void notifyPeekForecast(String playerName, int deckIndex) throws RemoteException {
        clientController.notifyPeekForecast(playerName,deckIndex);
    }

    @Override
    public void notifyReleaseForecast(String playerName, int deckIndex) throws RemoteException {
        clientController.notifyReleaseForecast(playerName,deckIndex);
    }

    @Override
    public void notifyPlaceShipOnFlightBoard(String playerName, int position) throws RemoteException {
        clientController.notifyPlayerPosition(playerName, position);
    }

    @Override
    public void showShipPieces(String playerName, List<Set<Point>> shipPieces) throws RemoteException {
        clientController.showShipPieces(playerName, shipPieces);
    }

    @Override
    public void notifyInvalidShipsUpdate(List<String> invalidPlayers) throws RemoteException {
        clientController.notifyInvalidShipsUpdate(invalidPlayers);
    }

    @Override
    public void notifyCabinUpdate(String playerName, Point point, int numResidents, CrewType crewType) throws RemoteException {
        clientController.notifyCabinUpdate(playerName, point, numResidents, crewType);
    }

    @Override
    public void notifyBatteryUpdate(String playerName, Point point, int numBatteries) throws RemoteException {
        clientController.changeBatteriesOnComponent(playerName,point,numBatteries);
    }

    @Override
    public void notifyCargoHoldUpdate(String playerName, Point point, Map<GoodsType, Integer> cargo) throws RemoteException {
        clientController.notifyCargoHoldUpdate(playerName,point,cargo);
    }

    @Override
    public void notifyShipStatUpdate(String playerName, StatType statType, int value) throws RemoteException {
        clientController.notifyShipStatusUpdate(playerName,statType,value);
    }

    @Override
    public void notifyNewCard(int cardId) throws RemoteException {
        clientController.notifyNewCard(cardId);
    }

    @Override
    public void notifySurrender(List<String> playerNames) throws RemoteException {
        //TODO : notify surrender
    }

    @Override
    public void notifyHourglassEnd() throws RemoteException {
        clientController.notifyHourglassEnd();
    }

    @Override
    public void notifyComponentRemoval(String playerName, Point position) throws RemoteException {
        clientController.notifyComponentRemoval(playerName, position);
    }

    @Override
    public void notifyShipPieceRemoval(String playerName, List<Point> positions) throws RemoteException {
        clientController.notifyShipPieceRemoval(playerName,positions);
    }

    @Override
    public void notifySelection(String playerName, List<Point> selectablePoints, List<Point> batteries) throws RemoteException {
        clientController.notifySelection(playerName,selectablePoints,batteries);
    }

    @Override
    public void notifyLandOnPlanet(String playerName, int planetId) throws RemoteException {
        clientController.notifyLandOnPlanet(playerName, planetId);
    }

    @Override
    public void updateGoodsBuffer(boolean adding, GoodsType type) throws RemoteException {
        clientController.updateGoodsBuffer(adding, type);
    }

    //todo add nickname and split [notify selection for cannon and engines] (open space, slavers, smugglers, pirates first part) and [show projectile] (meteors, warzone, pirates second part)
    @Override
    public void showProjectile(String nickname, ProjectileType projectileType, int direction, int roll, List<Point> selectablePoints, List<Point> batteries) throws RemoteException {
        clientController.showProjectile(nickname, projectileType,direction,roll,selectablePoints,batteries);
    }

    @Override
    public void showFinalScores(Map<String, Integer> playerToScore) throws RemoteException {
        //TODO: fix controller methods
        clientController.showFinalStats();
    }

    @Override
    public void notifyComponentActivation(String playerName, Point point) throws RemoteException {
        clientController.notifyComponentActivation(playerName,point);
    }

    @Override
    public void notifyAddGoods(String playerName, Map<GoodsType, Integer> goods, List<Point> cargos) {

    }
}
