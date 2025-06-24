package it.polimi.ingsw.galaxytruckers.network.client;

import it.polimi.ingsw.galaxytruckers.model.GameModel;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.serverController.dto.LobbyDetailsDTO;
import it.polimi.ingsw.galaxytruckers.serverController.dto.states.ShipBuildingDTO;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.GameStateUpdateEvent;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.JoinLobbyEvent;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.LobbyDetailsEvent;
import it.polimi.ingsw.galaxytruckers.view.CliView;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;

import java.awt.*;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ShipBuildingTest {

    static ServerHandler server;
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
        server = new ServerHandlerStub();
        gameModel = new GameModel();
        model = new ClientModel();

        controller = new ClientController();
        controller.setServer(server);
        controller.setModel(model);
        cliView = new CliView(controller, model);
//        controller.setView(cliView);
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
        lobbyDetailsEvent = new LobbyDetailsEvent("Roborbio", new LobbyDetailsDTO(null, Map.of("Roborbio", GameColor.RED), level, 2));
        controller.notifyEvent(lobbyDetailsEvent);
//        joinLobbyEvent = new JoinLobbyEvent("Roborbio", GameColor.RED);
//        controller.notifyEvent(joinLobbyEvent);
        lobbyDetailsEvent = new LobbyDetailsEvent("Roborbio", new LobbyDetailsDTO(null, playerToColor, level, 2));
        controller.notifyEvent(lobbyDetailsEvent);
        joinLobbyEvent = new JoinLobbyEvent("imTired", GameColor.BLUE);
        controller.notifyEvent(joinLobbyEvent);
        gameStateUpdateEvent = new GameStateUpdateEvent(new ShipBuildingDTO());
        controller.notifyEvent(gameStateUpdateEvent);
    }
}