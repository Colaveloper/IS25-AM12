package it.polimi.ingsw.galaxytruckers.network.client.rmi;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.StatType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.network.server.rmi.RemoteServer;
import it.polimi.ingsw.galaxytruckers.network.server.rmi.RemoteController;
import it.polimi.ingsw.galaxytruckers.network.client.VirtualServer;

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
    private RemoteController controller;

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

    private void handleNetworkError(RemoteException e) {
        //TODO: define a way to handle network errors
    }

    // VirtualServer

    @Override
    public void registerNickname(String myNickname) {
        try {
            this.controller = server.registerNickname(this, myNickname);
        } catch (RemoteException e) {
            handleNetworkError(e);
        }
    }

    @Override
    public void newGame(Level level, int playerN) {
        try {
            controller.newGame(level, playerN);
        } catch (RemoteException e) {
            handleNetworkError(e);
        }
    }

    @Override
    public void drawCard() {
        try {
            controller.drawCard();
        } catch (RemoteException e) {
            handleNetworkError(e);
        }
    }


    // RemoteClient
    //TODO: implement update methods (ref to ClientController)

    @Override
    public void setupLobby(UUID lobbyId, Map<String, Colors> playerColors) throws RemoteException {

    }

    @Override
    public void updateLobbyPlayers(Map<String, Colors> playerColors) throws RemoteException {

    }

    @Override
    public void notifyStartBuilding() throws RemoteException {

    }

    @Override
    public void notifyFaceDownComponentRequest(String playerName, int componentId, int numFaceDown) throws RemoteException {

    }

    @Override
    public void notifyFaceUpComponentRequest(String playerName, int componentId, Set<Integer> faceUpComponentIds) throws RemoteException {

    }

    @Override
    public void notifyComponentRejection(String playerName, int componentId, Set<Integer> faceUpComponentIds) throws RemoteException {

    }

    @Override
    public void notifyStashComponent(String playerName, List<Integer> stashComponentIds) throws RemoteException {

    }

    @Override
    public void notifyGrabFromStash(String playerName, int componentId, List<Integer> stashComponentIds) throws RemoteException {

    }

    @Override
    public void notifyHourglassFlipped(String playerName, boolean isLast) throws RemoteException {

    }

    @Override
    public void notifyShipMapUpdate(String playerName, Map<Point, Integer> componentIdMap) throws RemoteException {

    }

    @Override
    public void sendForecastDeck(int deckIndex, List<Integer> deckCardIds) throws RemoteException {

    }

    @Override
    public void notifyPeekForecast(String playerName, int deckIndex) throws RemoteException {

    }

    @Override
    public void notifyReleaseForecast(String playerName, int deckIndex) throws RemoteException {

    }

    @Override
    public void notifyPlaceShipOnFlightBoard(String playerName, Map<String, Integer> playerToPlace) throws RemoteException {

    }

    @Override
    public void showShipPieces(List<Set<Point>> shipPieces) throws RemoteException {

    }

    @Override
    public void notifyInvalidShipsUpdate(List<String> invalidPlayers) throws RemoteException {

    }

    @Override
    public void notifyCabinUpdate(String playerName, Point point, int numResidents, CrewType crewType) throws RemoteException {

    }

    @Override
    public void notifyBatteryUpdate(String playerName, Point point, int numBatteries) throws RemoteException {

    }

    @Override
    public void notifyCargoHoldUpdate(String playerName, Point point, Map<GoodsType, Integer> cargo) throws RemoteException {

    }

    @Override
    public void notifyShipStatUpdate(String playerName, StatType statType, int value) throws RemoteException {

    }

    @Override
    public void notifyNewCard(int cardId) throws RemoteException {

    }

    @Override
    public void notifySurrender(List<String> playerNames) throws RemoteException {

    }
}
