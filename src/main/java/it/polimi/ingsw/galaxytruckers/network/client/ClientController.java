package it.polimi.ingsw.galaxytruckers.network.client;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.utils.Logger;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.Event;
import it.polimi.ingsw.galaxytruckers.view.*;
import it.polimi.ingsw.galaxytruckers.view.controller.ClientEventHandler;
import it.polimi.ingsw.galaxytruckers.view.controller.PlayerRegistry;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.MetaState;
import it.polimi.ingsw.galaxytruckers.view.model.Player;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ClientController implements ClientControllerInterface, ControllerToServer {
    private ClientModel model;
    private ServerHandler server;
    private ErrorReporter view;
    private final PlayerRegistry playerRegistry = new PlayerRegistry();

    private boolean connected = true;
    private final Object connectionLock = new Object();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> connectionFuture;

    private ClientEventHandler eventHandler;

    public void setModel(ClientModel model) {
        this.model = model;
    }

    public void setServer(ServerHandler server) {
        this.server = server;
    }

    public void setView(ErrorReporter view) {
        this.view = view;
    }

    public void initEventHandler() {
        this.eventHandler = new ClientEventHandler(model, playerRegistry);
    }

    //---------------------------------------INTERNAL CALLS------------------------------------------

    public void showGameCreation() {
        model.setMetaState(MetaState.CREATION);
    }

    public void setMyNickname(String nickname) {
        Player player = playerRegistry.addPlayer(nickname);
        model.setPlayer(player);
    }

    public void clearNickname() {
        Player player = model.getClientPlayer();
        playerRegistry.removePlayer(player);
        model.setPlayer(null);
    }

    public void dropConnection() {
        server.dropConnection();
    }

    public void signalDisconnection() {
        synchronized (connectionLock) {
            if (connected) {
                view.reportError("You have been disconnected, trying to reconnect...");
                connected = false;
                connectionFuture = scheduler.scheduleAtFixedRate(this::reconnect,3,3, TimeUnit.SECONDS);
            }
        }
    }

    private void reconnect() {
        synchronized (connectionLock) {
            try {
                if (connected || server.reconnect()) {
                    connected = true;
                    if (model.getClientPlayer() != null) {
                        server.registerNickname(model.getClientPlayer().getNickname());
                    }
                    connectionFuture.cancel(true);
                }
            } catch (Exception e) {
                view.reportError("Failed to reconnect");
            }
        }
    }

    //--------------------------------------UPDATES FROM THE SERVER----------------------------------

    @Override
    public void notifyEvent(Event event) {
        Logger.println(7, event);//debug
        eventHandler.handleEvent(event);
    }

    //----------------------------------------REQUESTS TO THE SERVER----------------------------------

    @Override
    public void registerNickname(String nickname) throws IllegalArgumentException {
        try {
            setMyNickname(nickname);
            server.registerNickname(nickname);
        } catch (IllegalArgumentException | IllegalStateException e) {
            clearNickname();
            view.reportError(e.getMessage());
        }
    }

    @Override
    public void joinLobby(UUID lobbyID) {
        server.joinLobby(lobbyID);
    }

    @Override
    public void requestNewGame(Level level, int playersN) {
        try {
            server.requestNewGame(level, playersN);
        } catch (RuntimeException e) {
            view.reportError(e.getMessage());
        }
    }

    @Override
    public void goNext() {
        try {
            server.goNext();
        } catch (RuntimeException e) {
            view.reportError(e.getMessage());
        }
    }

    @Override
    public void chooseShipPiece(int choice) {
        try {
            server.chooseShipPiece(choice);
        } catch (RuntimeException e) {
            view.reportError(e.getMessage());
        }
    }

    @Override
    public void activateComponent(Point point) {
        try {
            server.activateComponent(point);
        } catch (RuntimeException e) {
            view.reportError(e.getMessage());
        }
    }


    @Override
    public void removeComponent(Point point) {
        try {
            server.removeComponent(point);
        } catch (RuntimeException e) {
            view.reportError(e.getMessage());
        }
    }

    @Override
    public void useBattery(Point point) {
        try {
            server.useBattery(point);
        } catch (RuntimeException e) {
            view.reportError(e.getMessage());
        }
    }

    @Override
    public void initializeCabin(Point point, CrewType crewType) {
        try {
            server.initializeCabin(point, crewType);
        } catch (RuntimeException e) {
            view.reportError(e.getMessage());
        }
    }

    @Override
    public void placeShipOnFlightboard(int startingPosition) {
        try {
            server.placeShipOnFlightBoard(startingPosition);
        } catch (RuntimeException e) {
            view.reportError(e.getMessage());
        }
    }

    @Override
    public void placeShipOnFlightBoard() {
        try {
            server.placeShipOnFlightBoard();
        } catch (RuntimeException e) {
            view.reportError(e.getMessage());
        }
    }

    @Override
    public void flipHourglass() {
        try {
            server.flipHourglass();
        } catch (RuntimeException e) {
            view.reportError(e.getMessage());
        }
    }

    @Override
    public void requestRandComponent() {
        try {
            server.requestRandComponent();
        } catch (RuntimeException e) {
            view.reportError(e.getMessage());
        }
    }

    @Override
    public void requestComponent(int componentId) {
        try {
            server.requestComponent(componentId);
        } catch (RuntimeException e) {
            view.reportError(e.getMessage());
        }
    }

    @Override
    public void stashComponent() {
        try {
            server.stashComponent();
        } catch (RuntimeException e) {
            view.reportError(e.getMessage());
        }
    }

    @Override
    public void grabPlacedComponent() {
        try {
            server.grabPlacedComponent();
        } catch (RuntimeException e) {
            view.reportError(e.getMessage());
        }
    }

    @Override
    public void grabStashedComponent(int index) {
        try {
            server.grabStashedComponent(index);
        } catch (RuntimeException e) {
            view.reportError(e.getMessage());
        }
    }

    @Override
    public void acquireForecast(int index) {
        try {
            server.acquireForecast(index);
        } catch (RuntimeException e) {
            view.reportError(e.getMessage());
        }
    }

    @Override
    public void releaseForecast() {
        try {
            server.releaseForecast();
        } catch (RuntimeException e) {
            view.reportError(e.getMessage());
        }
    }

    @Override
    public void rejectComponent() {
        try {
            server.rejectComponent();
        } catch (RuntimeException e) {
            view.reportError(e.getMessage());
        }
    }

    @Override
    public void placeComponent(Point point, Direction orientation) {
        try {
            server.placeComponent(point, orientation);
        } catch (RuntimeException e) {
            view.reportError(e.getMessage());
        }
    }

    @Override
    public void drawCard() {
        try {
            server.drawCard();
        } catch (RuntimeException e) {
            view.reportError(e.getMessage());
        }
    }

    @Override
    public void placeGoods(Point point, GoodsType good) {
        try {
            server.placeGoods(point, good);
        } catch (RuntimeException e) {
            view.reportError(e.getMessage());
        }
    }

    @Override
    public void removeGoods(Point point, GoodsType good) {
        try {
            server.removeGoods(point, good);
        } catch (RuntimeException e) {
            view.reportError(e.getMessage());
        }
    }

    @Override
    public void choosePlanet(int choice) {
        try {
            server.choosePlanet(choice);
        } catch (RuntimeException e) {
            view.reportError(e.getMessage());
        }
    }

    @Override
    public void loseCrew(Point p) {
        try {
            server.loseCrew(p);
        } catch (RuntimeException e) {
            view.reportError(e.getMessage());
        }
    }

    @Override
    public void grabReward() {
        try {
            server.grabReward();
        } catch (RuntimeException e) {
            view.reportError(e.getMessage());
        }
    }

    @Override
    public void loseGoods(Point p) {
        try {
            server.loseGoods(p);
        } catch (RuntimeException e) {
            view.reportError(e.getMessage());
        }
    }

    @Override
    public void giveUp() {
        try {
            server.giveUp();
        } catch (RuntimeException e) {
            view.reportError(e.getMessage());
        }
    }

    private void runAndInterceptIOE(RunnableWithIOE action) {
        try {
            action.run();
        } catch (IOException e) {
            view.reportError("IO Exception: " + e.getMessage());
        }
    }
}

@FunctionalInterface
interface RunnableWithIOE {
    void run() throws IOException;
}
