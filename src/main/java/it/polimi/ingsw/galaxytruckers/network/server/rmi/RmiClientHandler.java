package it.polimi.ingsw.galaxytruckers.network.server.rmi;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.StatType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.network.client.rmi.RemoteClient;
import it.polimi.ingsw.galaxytruckers.network.server.VirtualClient;
import it.polimi.ingsw.galaxytruckers.serverController.ServerControllerInterface;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class RmiClientHandler extends UnicastRemoteObject implements VirtualClient, RemoteController {
    private final RemoteClient remoteClient;
    private final ServerControllerInterface controller;

    private final Thread updateThread;
    private final BlockingQueue<HandlerTask> updateTasks;

    private final Player player;

    public RmiClientHandler(RemoteClient remoteClient, Player player, ServerControllerInterface controller) throws RemoteException{
        super();
        this.remoteClient = remoteClient;
        this.player = player;
        this.controller = controller;
        this.updateThread = new Thread(this::runUpdateThread);
        this.updateTasks = new LinkedBlockingQueue<>();
    }

    public void start() {
        updateThread.start();
    }

    public void stop() {
        updateThread.interrupt();
    }

    private void handleNetworkError(RemoteException e) {
        //TODO: define a way to handle exceptions
        throw new RuntimeException(e);
    }

    private void handleInternalError() {
        //TODO: define a way to handle errors related to BlockingQueue
        throw new RuntimeException("The update queue for " + player.getNickname() +
                " has failed to handle all updates");
    }

    private void submitUpdateTask(HandlerTask action) {
        boolean success = updateTasks.offer(action);
        if (!success) {
            handleInternalError();
        }
    }

    private void runUpdateThread() {
        while (true) {
            try {
                updateTasks.take().execute();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
                //TODO : handle shutting down connection
            } catch (RemoteException e) {
                handleNetworkError(e);
            }
        }
    }


    // VirtualClient

    @Override
    public void setupLobby(UUID lobbyId, Map<String, Colors> playerColors) {
        submitUpdateTask(() -> remoteClient.setupLobby(lobbyId, playerColors));
    }

    @Override
    public void updateLobbyPlayers(Map<String, Colors> playerColors) {
        submitUpdateTask(() -> remoteClient.updateLobbyPlayers(playerColors));
    }

    @Override
    public void notifyStartBuilding() {
        submitUpdateTask(remoteClient::notifyStartBuilding);
    }

    @Override
    public void notifyFaceDownComponentRequest(String playerName, int componentId, int numFaceDown) {
        submitUpdateTask(() -> remoteClient.notifyFaceDownComponentRequest(playerName, componentId, numFaceDown));
    }

    @Override
    public void notifyFaceUpComponentRequest(String playerName, int componentId, Set<Integer> faceUpComponentIds) {
        submitUpdateTask(() -> remoteClient.notifyFaceUpComponentRequest(playerName, componentId, faceUpComponentIds));
    }

    @Override
    public void notifyComponentRejection(String playerName, int componentId, Set<Integer> faceUpComponentIds) {
        submitUpdateTask(() -> remoteClient.notifyComponentRejection(playerName, componentId, faceUpComponentIds));
    }

    @Override
    public void notifyStashComponent(String playerName, List<Integer> stashComponentIds) {
        submitUpdateTask(() -> remoteClient.notifyStashComponent(playerName, stashComponentIds));
    }

    @Override
    public void notifyGrabFromStash(String playerName, int componentId, List<Integer> stashComponentIds) {
        submitUpdateTask(() -> remoteClient.notifyGrabFromStash(playerName, componentId, stashComponentIds));
    }

    @Override
    public void notifyHourglassFlipped(String playerName, boolean isLast) {
        submitUpdateTask(() -> remoteClient.notifyHourglassFlipped(playerName, isLast));
    }

    @Override
    public void notifyShipMapUpdate(String playerName, int componentId, int rotation, Point position) {
        submitUpdateTask(() -> remoteClient.notifyShipMapUpdate(playerName, componentId, rotation, position));
    }

    @Override
    public void sendForecastDeck(int deckIndex, List<Integer> deckCardIds) {
        submitUpdateTask(() -> remoteClient.sendForecastDeck(deckIndex, deckCardIds));
    }

    @Override
    public void notifyPeekForecast(String playerName, int deckIndex) {
        submitUpdateTask(() -> remoteClient.notifyPeekForecast(playerName, deckIndex));
    }

    @Override
    public void notifyReleaseForecast(String playerName, int deckIndex) {
        submitUpdateTask(() -> remoteClient.notifyReleaseForecast(playerName, deckIndex));
    }

    @Override
    public void notifyPlaceShipOnFlightBoard(String playerName, Map<String, Integer> playerToPlace) {
        submitUpdateTask(() -> remoteClient.notifyPlaceShipOnFlightBoard(playerName, playerToPlace));
    }

    @Override
    public void showShipPieces(List<Set<Point>> shipPieces) {
        submitUpdateTask(() -> remoteClient.showShipPieces(shipPieces));
    }

    @Override
    public void notifyInvalidShipsUpdate(List<String> invalidPlayers) {
        submitUpdateTask(() -> remoteClient.notifyInvalidShipsUpdate(invalidPlayers));
    }

    @Override
    public void notifyCabinUpdate(String playerName, Point point, int numResidents, CrewType crewType) {
        submitUpdateTask(() -> remoteClient.notifyCabinUpdate(playerName,point,numResidents,crewType));
    }

    @Override
    public void notifyBatteryUpdate(String playerName, Point point, int numBatteries) {
        submitUpdateTask(() -> remoteClient.notifyBatteryUpdate(playerName,point,numBatteries));
    }

    @Override
    public void notifyCargoHoldUpdate(String playerName, Point point, Map<GoodsType, Integer> cargo) {
        submitUpdateTask(() -> remoteClient.notifyCargoHoldUpdate(playerName,point,cargo));
    }

    @Override
    public void notifyShipStatUpdate(String playerName, StatType statType, int value) {
        submitUpdateTask(() -> remoteClient.notifyShipStatUpdate(playerName,statType,value));
    }

    @Override
    public void notifyNewCard(int cardId) {
        submitUpdateTask(() -> remoteClient.notifyNewCard(cardId));
    }

    @Override
    public void notifySurrender(List<String> playerNames) {
        submitUpdateTask(() -> remoteClient.notifySurrender(playerNames));
    }

    // RemoteController

    @Override
    public void newGame(Level level, int numPlayers) throws RemoteException {
        controller.newGame(player.getNickname(), level, numPlayers);
    }

    @Override
    public void joinLobby(UUID lobbyID) throws RemoteException {
        controller.joinLobby(player.getNickname(), lobbyID);
    }

    @Override
    public void leaveLobby() throws RemoteException {
        controller.leaveLobby(player.getNickname());
    }

    @Override
    public void requestRandComponent() throws RemoteException {
        controller.requestRandComponent(player.getNickname());
    }

    @Override
    public void requestComponent(int componentID) throws RemoteException {
        controller.requestComponent(player.getNickname(), componentID);
    }

    @Override
    public void rejectComponent() throws RemoteException {
        controller.rejectComponent(player.getNickname());
    }

    @Override
    public void stashComponent() throws RemoteException {
        controller.stashComponent(player.getNickname());
    }

    @Override
    public void grabStashedComponent(int index) throws RemoteException {
        controller.grabStashedComponent(player.getNickname(), index);
    }

    @Override
    public void placeComponent(Point point, int orientation) throws RemoteException {
        controller.placeComponent(player.getNickname(), point, orientation);
    }

    @Override
    public void flipHourglass() throws RemoteException {
        controller.flipHourglass(player.getNickname());
    }

    @Override
    public void placeShipOnFlightBoard(int startingPosition) throws RemoteException {
        controller.placeShipOnFlightBoard(player.getNickname(), startingPosition);
    }

    @Override
    public void acquireForecast(int deckIndex) throws RemoteException {
        controller.acquireForecast(player.getNickname(), deckIndex);
    }

    @Override
    public void releaseForecast() throws RemoteException {
        controller.releaseForecast(player.getNickname());
    }

    @Override
    public void removeComponent(Point point) throws RemoteException {
        controller.removeComponent(player.getNickname(), point);
    }

    @Override
    public void chooseShipPiece(int pieceIndex) throws RemoteException {
        controller.chooseShipPiece(player.getNickname(), pieceIndex);
    }

    @Override
    public void initializeCabin(Point point, CrewType crewType) throws RemoteException {
        controller.initializeCabin(player.getNickname(), point, crewType);
    }

    @Override
    public void drawCard() throws RemoteException {
        controller.drawCard(player.getNickname());
    }

    @Override
    public void activateComponent(Point point) throws RemoteException {
        controller.activateComponent(player.getNickname(), point);
    }

    @Override
    public void loseCrew(Point point) throws RemoteException {
        controller.loseCrew(player.getNickname(), point);
    }

    @Override
    public void grabReward(boolean rewardGrabbed) throws RemoteException {
        controller.grabReward(player.getNickname(), rewardGrabbed);
    }

    @Override
    public void placeGoods(Point point, GoodsType goodsType) throws RemoteException {
        controller.placeGoods(player.getNickname(), point, goodsType);
    }

    @Override
    public void removeGoods(Point point, GoodsType goodsType) throws RemoteException {
        controller.removeGoods(player.getNickname(), point, goodsType);
    }

    @Override
    public void loseGoods(Point point) throws RemoteException {
        controller.loseGoods(player.getNickname(), point);
    }

    @Override
    public void useBattery(Point point) throws RemoteException {
        controller.useBattery(player.getNickname(), point);
    }

    @Override
    public void choosePlanet(int choice) throws RemoteException {
        controller.choosePlanet(player.getNickname(), choice);
    }

    @Override
    public void goNext() throws RemoteException {
        controller.goNext(player.getNickname());
    }

    @Override
    public void giveUp() throws RemoteException {
        controller.giveUp(player.getNickname());
    }
}

@FunctionalInterface
interface HandlerTask {
    public void execute() throws RemoteException;
}
