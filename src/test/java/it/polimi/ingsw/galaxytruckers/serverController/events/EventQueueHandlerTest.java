package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.model.GameModel;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.FourColors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.StatType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.network.server.SessionManager;
import it.polimi.ingsw.galaxytruckers.network.server.VirtualClient;
import it.polimi.ingsw.galaxytruckers.serverController.ServerController;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Lobby;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.LobbyInterface;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;
import it.polimi.ingsw.galaxytruckers.view.viewEnums.ProjectileType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EventQueueHandlerTest {
    Lobby lobby;
    EventQueueHandler eventQueueHandler;
    EventQueue eventQueue;
    VirtualClientStub client1;
    VirtualClientStub client2;

    Object queueLock;

    @BeforeEach
    void setup() {
        client1 = new VirtualClientStub();
        client2 = new VirtualClientStub();
        Player p1 = Player.addPlayer("p1");
        Player p2 = Player.addPlayer("p2");
        SessionManager.getInstance().registerClient(p1,client1);
        SessionManager.getInstance().registerClient(p2,client2);
        lobby = new Lobby(new GameModel(), new ServerControllerStub(), p1, Level.SECOND,2);
        lobby.addPlayer(p2);
        eventQueue = lobby.getEventQueue();
        eventQueueHandler = lobby.getEventQueueHandler();
    }

    @Test
    void setupLobbyIsCalled() throws InterruptedException {
        long time = 0;
        while(!eventQueue.isEmpty() && time < 1000) {
            Thread.sleep(100);
            time += 100;
        }
        eventQueueHandler.stop();
        assertEquals("setupLobby", client1.methods.getFirst());
    }
}

class VirtualClientStub implements VirtualClient {
    List<String> methods = new ArrayList<>();

    @Override
    public void setupLobby(UUID lobbyId, Map<String, FourColors> playerColors) {
        methods.add("setupLobby");
    }

    @Override
    public void updateLobbyPlayers(Map<String, FourColors> playerColors) {
        methods.add("updateLobbyPlayers");
    }

    @Override
    public void notifyStashComponent(String playerName, List<Integer> stashComponentIds) {

    }

    @Override
    public void notifyGrabFromStash(String playerName, int componentId, List<Integer> stashComponentIds) {

    }

    @Override
    public void notifyComponentPositioning(String playerName, int componentId, int rotation, Point position) {

    }

    @Override
    public void notifyComponentRejection(String playerName, int componentId) {

    }

    @Override
    public void notifyFaceDownComponentRequest(String playerName, int componentId) {

    }

    @Override
    public void notifyFaceUpComponentRequest(String playerName, int componentId) {

    }

    @Override
    public void notifyPeekForecast(String playerName, int deckIndex) {

    }

    @Override
    public void notifyReleaseForecast(String playerName, int deckIndex) {

    }

    @Override
    public void sendForecastDeck(List<Integer> deckCardIds) {

    }

    @Override
    public void notifyHourglassFlipped(String playerName, boolean isLast) {

    }

    @Override
    public void notifyHourglassEnd() {

    }

    @Override
    public void notifyStartBuilding(Level level, int playersN) {

    }

    @Override
    public void notifyCabinUpdate(String playerName, Point point, int numResidents, CrewType crewType) {

    }

    @Override
    public void notifyPlayerPosition(String playerName, int position) {

    }

    @Override
    public void notifyComponentRemoval(String playerName, Point position) {

    }

    @Override
    public void notifyShipPieceRemoval(String playerName, List<Point> positions) {

    }

    @Override
    public void showShipPieces(String playerName, List<Set<Point>> shipPieces) {

    }

    @Override
    public void notifyInvalidShipsUpdate(List<String> invalidPlayers) {

    }

    @Override
    public void notifyShipStatUpdate(String playerName, StatType statType, int value) {

    }

    @Override
    public void notifyNewCard(int cardId) {

    }

    @Override
    public void notifySelection(String playerName, List<Point> selectablePoints) {

    }

    @Override
    public void notifyCargoHoldUpdate(String playerName, Point point, Map<GoodsType, Integer> cargo) {

    }

    @Override
    public void notifyBatteryUpdate(String playerName, Point point, int numBatteries) {

    }

    @Override
    public void notifyPlanetChoice(String playerName, int planetId, List<Point> cargoPositions) {

    }

    @Override
    public void updateGoodsBuffer(GoodsType type) {

    }

    @Override
    public void showProjectile(ProjectileType projectileType, int direction, int roll, List<Point> selectablePoints, List<Point> batteries) {

    }

    @Override
    public void showFinalScores(Map<String, Integer> playerToScore) {

    }

    @Override
    public void notifySurrender(List<String> playerNames) {

    }

    @Override
    public void notifyPlayerDisconnection(String playerName) {

    }
}

class ServerControllerStub extends ServerController {
    public ServerControllerStub() {
        super(null);
    }

    @Override
    public Player registerNickname(String nickname) {
        return null;
    }

    @Override
    public LobbyInterface newGame(Player creator, Level level, int numPlayers) {
        return null;
    }

    @Override
    public LobbyInterface joinLobby(Player player, UUID lobbyID) {
        return null;
    }

    @Override
    public void removeLobby(UUID lobbyID) {
    }
}