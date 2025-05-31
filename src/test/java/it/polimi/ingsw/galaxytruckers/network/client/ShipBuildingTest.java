package it.polimi.ingsw.galaxytruckers.network.client;

import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.GameModel;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.network.client.rmi.RemoteClient;
import it.polimi.ingsw.galaxytruckers.network.client.rmi.RmiClient;
import it.polimi.ingsw.galaxytruckers.network.server.rmi.RmiClientHandler;
import it.polimi.ingsw.galaxytruckers.network.server.rmi.RmiServer;
import it.polimi.ingsw.galaxytruckers.serverController.ServerController;
import it.polimi.ingsw.galaxytruckers.serverController.ServerControllerInterface;
import it.polimi.ingsw.galaxytruckers.serverController.dto.states.ShipBuildingDTO;
import it.polimi.ingsw.galaxytruckers.serverController.events.GameStateUpdateEvent;
import it.polimi.ingsw.galaxytruckers.serverController.events.JoinLobbyEvent;
import it.polimi.ingsw.galaxytruckers.serverController.events.LobbyDetailsEvent;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;
import it.polimi.ingsw.galaxytruckers.view.CliView;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;

import java.awt.*;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.*;

public class ShipBuildingTest {

    static VirtualServer server;
    static GameModel gameModel;
    static ClientController controller;
    static ClientModel model;
    static CliView cliView;

    static Map<String, GameColor> playerToColor;
    static Level level;

    static JoinLobbyEvent joinLobbyEvent;
    static LobbyDetailsEvent lobbyDetailsEvent;
    static GameStateUpdateEvent gameStateUpdateEvent;

    static void setUp() throws RemoteException {
        server = new VirtualServer() {
            @Override
            public void registerNickname(String myNickname) {

            }

            @Override
            public void requestNewGame(Level level, int playerN) {

            }

            @Override
            public void joinLobby(UUID lobbyID) {

            }

            @Override
            public void leaveLobby() {

            }

            @Override
            public void requestRandComponent() {

            }

            @Override
            public void requestComponent(int componentID) {

            }

            @Override
            public void rejectComponent() {

            }

            @Override
            public void stashComponent() {

            }

            @Override
            public void grabStashedComponent(int index) {

            }

            @Override
            public void placeComponent(Point point, int orientation) {

            }

            @Override
            public void flipHourglass() {

            }

            @Override
            public void placeShipOnFlightBoard(int startingPosition) {

            }

            @Override
            public void acquireForecast(int deckIndex) {

            }

            @Override
            public void releaseForecast() {

            }

            @Override
            public void removeComponent(Point point) {

            }

            @Override
            public void chooseShipPiece(int pieceIndex) {

            }

            @Override
            public void initializeCabin(Point point, CrewType crewType) {

            }

            @Override
            public void drawCard() {

            }

            @Override
            public void activateComponent(Point point) {

            }

            @Override
            public void loseCrew(Point point) {

            }

            @Override
            public void grabReward(boolean rewardGrabbed) {

            }

            @Override
            public void placeGoods(Point point, GoodsType goodsType) {

            }

            @Override
            public void removeGoods(Point point, GoodsType goodsType) {

            }

            @Override
            public void loseGoods(Point point) {

            }

            @Override
            public void useBattery(Point point) {

            }

            @Override
            public void choosePlanet(int choice) {

            }

            @Override
            public void goNext() {

            }

            @Override
            public void giveUp() {

            }
        };
        gameModel = new GameModel();
        model = new ClientModel();

        controller = new ClientController();
        controller.setServer(server);
        controller.setModel(model);
        cliView = new CliView(controller, model);
        controller.setView(cliView);
//        controller.registerNickname("Roborbio");
//        controller.requestNewGame(Level.SECOND, 1);
        controller.initEventHandler();

        level = Level.SECOND;
        playerToColor = new HashMap<>(Map.of("Roborbio", GameColor.RED, "imTired", GameColor.BLUE));
    }

    public static void main(String[] args) throws RemoteException {
        setUp();
        rushToBuilding();
    }

    public static void rushToBuilding() {
        controller.setMyNickname("Roborbio");
        controller.registerNickname("Roborbio");
        controller.showGameCreation();
        controller.requestNewGame(Level.SECOND, 2);
        lobbyDetailsEvent = new LobbyDetailsEvent("Roborbio", null, Map.of("Roborbio", GameColor.RED), level, 2);
        controller.notifyEvent(lobbyDetailsEvent);
//        joinLobbyEvent = new JoinLobbyEvent("Roborbio", GameColor.RED);
//        controller.notifyEvent(joinLobbyEvent);
        lobbyDetailsEvent = new LobbyDetailsEvent("Roborbio", null, playerToColor, level, 2);
        controller.notifyEvent(lobbyDetailsEvent);
        joinLobbyEvent = new JoinLobbyEvent("imTired", GameColor.BLUE);
        controller.notifyEvent(joinLobbyEvent);
        gameStateUpdateEvent = new GameStateUpdateEvent(new ShipBuildingDTO());
        controller.notifyEvent(gameStateUpdateEvent);
    }
}