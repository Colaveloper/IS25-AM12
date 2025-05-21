package it.polimi.ingsw.galaxytruckers.network.server.rmi;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.FourColors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.StatType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.network.client.rmi.RemoteClient;
import it.polimi.ingsw.galaxytruckers.network.server.VirtualClient;
import it.polimi.ingsw.galaxytruckers.serverController.ServerControllerInterface;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.LobbyInterface;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;
import it.polimi.ingsw.galaxytruckers.view.viewEnums.ProjectileType;

import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class RmiClientHandler extends UnicastRemoteObject implements VirtualClient, RemoteController {
    private final RemoteClient remoteClient;
    private final ServerControllerInterface controller;
    private LobbyInterface lobby;

    private Thread updateThread;
    private boolean running = false;
    private final BlockingDeque<HandlerTask> updateTasks;

    private final Player player;

    public RmiClientHandler(RemoteClient remoteClient, Player player, ServerControllerInterface controller) throws RemoteException{
        super();
        this.remoteClient = remoteClient;
        this.player = player;
        this.controller = controller;
        this.updateThread = null;
        this.updateTasks = new LinkedBlockingDeque<>();
        startUpdateThread();
    }

    public void startUpdateThread() {
        running = true;
        updateThread = new Thread(this::runUpdateThread,"UpdateThread");
        updateThread.start();
    }

    public void stopUpdateThread() {
        running = false;
        this.updateThread = null;
    }

    private void handleNetworkError(RemoteException e) {
        System.out.println("WARNING: Player " + player.getNickname() + " has disconnected \n" +
                "A remote exception was thrown: " +  e.getMessage());
        controller.handlePlayerDisconnection(player);
        stopUpdateThread();
    }

    private void handleInternalError() {
        System.err.println("ERROR: The update queue for " + player.getNickname() +
                " has failed to handle all updates");
        stopUpdateThread();
    }

    private void submitUpdateTask(HandlerTask action) {
        boolean success = updateTasks.offer(action);
        if (!success) {
            handleInternalError();
        }
    }

    private void runUpdateThread() {
        HandlerTask task = null;
        while (running) {
            try {
                task = updateTasks.takeFirst();
                task.execute();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (RemoteException e) {
                updateTasks.offerFirst(task);
                handleNetworkError(e);
            }
        }
    }


    // VirtualClient

    @Override
    public void setupLobby(UUID lobbyId, Map<String, FourColors> playerColors) {
        submitUpdateTask(() -> remoteClient.setupLobby(lobbyId, playerColors));
    }

    @Override
    public void updateLobbyPlayers(Map<String, FourColors> playerColors) {
        submitUpdateTask(() -> remoteClient.updateLobbyPlayers(playerColors));
    }

    @Override
    public void notifyStartBuilding(Level level, int playersN) {
        submitUpdateTask(() -> remoteClient.notifyStartBuilding(level, playersN));
    }

    @Override
    public void notifyFaceDownComponentRequest(String playerName, int componentId) {
        submitUpdateTask(() -> remoteClient.notifyFaceDownComponentRequest(playerName, componentId));
    }

    @Override
    public void notifyFaceUpComponentRequest(String playerName, int componentId) {
        submitUpdateTask(() -> remoteClient.notifyFaceUpComponentRequest(playerName, componentId));
    }

    @Override
    public void notifyComponentRejection(String playerName, int componentId) {
        submitUpdateTask(() -> remoteClient.notifyComponentRejection(playerName, componentId));
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
    public void notifyComponentPositioning(String playerName, int componentId, int rotation, Point position) {
        submitUpdateTask(() -> remoteClient.notifyShipMapUpdate(playerName, componentId, rotation, position));
    }

    @Override
    public void sendForecastDeck(List<Integer> deckCardIds) {
        submitUpdateTask(() -> remoteClient.sendForecastDeck(deckCardIds));
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
    public void notifyPlayerPosition(String playerName, int position) {
        submitUpdateTask(() -> remoteClient.notifyPlaceShipOnFlightBoard(playerName, position));
    }

    @Override
    public void showShipPieces(String playerName, List<Set<Point>> shipPieces) {
        submitUpdateTask(() -> remoteClient.showShipPieces(playerName, shipPieces));
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

    @Override
    public void notifyHourglassEnd() {
        submitUpdateTask(remoteClient::notifyHourglassEnd);
    }

    @Override
    public void notifyComponentRemoval(String playerName, Point position) {
        submitUpdateTask(() -> remoteClient.notifyComponentRemoval(playerName, position));
    }

    @Override
    public void notifyShipPieceRemoval(String playerName, List<Point> positions) {
        submitUpdateTask(() -> remoteClient.notifyShipPieceRemoval(playerName, positions));
    }

    @Override
    public void notifySelection(String playerName, List<Point> selectablePoints) {
        submitUpdateTask(() -> remoteClient.notifySelection(playerName, selectablePoints));
    }

    @Override
    public void notifyPlanetChoice(String playerName, int planetId, List<Point> cargoPositions) {
        submitUpdateTask(() -> remoteClient.notifyPlanetChoice(playerName,planetId,cargoPositions));
    }

    @Override
    public void updateGoodsBuffer(GoodsType type) {
        submitUpdateTask(() -> remoteClient.updateGoodsBuffer(type));
    }

    @Override
    public void showProjectile(ProjectileType projectileType, int direction, int roll, List<Point> selectablePoints, List<Point> batteries) {
        submitUpdateTask(() -> remoteClient.showProjectile(projectileType,direction,roll,selectablePoints,batteries));
    }

    @Override
    public void showFinalScores(Map<String, Integer> playerToScore) {
        submitUpdateTask(() -> remoteClient.showFinalScores(playerToScore));
    }

    @Override
    public void notifyPlayerDisconnection(String playerName) {
        submitUpdateTask(() -> remoteClient.notifyPlayerDisconnection(playerName));
        this.lobby = null;
    }

    // RemoteController

    private void checkLobby() {
        if (lobby == null) {
            throw new IllegalStateException("You are not in a lobby yet");
        }
    }

    @Override
    public void newGame(Level level, int numPlayers) throws RemoteException {
        this.lobby = controller.newGame(player, level, numPlayers);
    }

    @Override
    public void joinLobby(UUID lobbyID) throws RemoteException {
        this.lobby = controller.joinLobby(player, lobbyID);
    }

    @Override
    public void leaveLobby() throws RemoteException {
        checkLobby();
        controller.leaveLobby(player);
    }

    @Override
    public void requestRandComponent() throws RemoteException {
        checkLobby();
        lobby.requestRandComponent(player);
    }

    @Override
    public void requestComponent(int componentID) throws RemoteException {
        checkLobby();
        lobby.requestComponent(player, componentID);
    }

    @Override
    public void rejectComponent() throws RemoteException {
        checkLobby();
        lobby.rejectComponent(player);
    }

    @Override
    public void stashComponent() throws RemoteException {
        checkLobby();
        lobby.stashComponent(player);
    }

    @Override
    public void grabStashedComponent(int index) throws RemoteException {
        checkLobby();
        lobby.grabStashedComponent(player, index);
    }

    @Override
    public void placeComponent(Point point, int orientation) throws RemoteException {
        checkLobby();
        lobby.placeComponent(player, point, orientation);
    }

    @Override
    public void flipHourglass() throws RemoteException {
        checkLobby();
        lobby.flipHourglass(player);
    }

    @Override
    public void placeShipOnFlightBoard(int startingPosition) throws RemoteException {
        checkLobby();
        lobby.placeShipOnFlightBoard(player, startingPosition);
    }

    @Override
    public void acquireForecast(int deckIndex) throws RemoteException {
        checkLobby();
        lobby.acquireForecast(player, deckIndex);
    }

    @Override
    public void releaseForecast() throws RemoteException {
        checkLobby();
        lobby.releaseForecast(player);
    }

    @Override
    public void removeComponent(Point point) throws RemoteException {
        checkLobby();
        lobby.removeComponent(player, point);
    }

    @Override
    public void chooseShipPiece(int pieceIndex) throws RemoteException {
        checkLobby();
        lobby.chooseShipPiece(player, pieceIndex);
    }

    @Override
    public void initializeCabin(Point point, CrewType crewType) throws RemoteException {
        checkLobby();
        lobby.initializeCabin(player, point, crewType);
    }

    @Override
    public void drawCard() throws RemoteException {
        checkLobby();
        lobby.drawCard(player);
    }

    @Override
    public void activateComponent(Point point) throws RemoteException {
        checkLobby();
        lobby.activateComponent(player, point);
    }

    @Override
    public void loseCrew(Point point) throws RemoteException {
        checkLobby();
        lobby.loseCrew(player, point);
    }

    @Override
    public void grabReward(boolean rewardGrabbed) throws RemoteException {
        checkLobby();
        lobby.grabReward(player, rewardGrabbed);
    }

    @Override
    public void placeGoods(Point point, GoodsType goodsType) throws RemoteException {
        checkLobby();
        lobby.placeGoods(player, point, goodsType);
    }

    @Override
    public void removeGoods(Point point, GoodsType goodsType) throws RemoteException {
        checkLobby();
        lobby.removeGoods(player, point, goodsType);
    }

    @Override
    public void loseGoods(Point point) throws RemoteException {
        checkLobby();
        lobby.loseGoods(player, point);
    }

    @Override
    public void useBattery(Point point) throws RemoteException {
        checkLobby();
        lobby.useBattery(player, point);
    }

    @Override
    public void choosePlanet(int choice) throws RemoteException {
        checkLobby();
        lobby.choosePlanet(player, choice);
    }

    @Override
    public void goNext() throws RemoteException {
        checkLobby();
        lobby.goNext(player);
    }

    @Override
    public void giveUp() throws RemoteException {
        checkLobby();
        lobby.giveUp(player);
    }
}

@FunctionalInterface
interface HandlerTask {
    void execute() throws RemoteException;
}
