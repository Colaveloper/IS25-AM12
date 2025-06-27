package it.polimi.ingsw.galaxytruckers.network.client;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.Event;
import it.polimi.ingsw.galaxytruckers.utils.Logger;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import it.polimi.ingsw.galaxytruckers.view.ErrorReporter;
import it.polimi.ingsw.galaxytruckers.view.controller.ClientEventHandler;
import it.polimi.ingsw.galaxytruckers.view.controller.PlayerRegistry;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.MetaState;
import it.polimi.ingsw.galaxytruckers.view.model.Player;

import java.awt.*;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * ClientController is responsible for handling client-side interactions with the server,
 * managing the client components, and processing events received from the server.
 * It implements the ClientControllerInterface and ControllerToServer interfaces.
 */
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

    /**
     * Sets the model for the client controller.
     *
     * @param model the ClientModel instance to be used by the controller
     */
    public void setModel(ClientModel model) {
        this.model = model;
    }

    /**
     * Sets the server handler for the client controller.
     *
     * @param server the ServerHandler instance to be used by the controller
     */
    public void setServer(ServerHandler server) {
        this.server = server;
    }

    /**
     * Sets the view for the client controller.
     *
     * @param view the view object to be used by the controller
     */
    public void setView(ErrorReporter view) {
        this.view = view;
    }

    /**
     * Initializes the event handler for processing events received from the server.
     */
    public void initEventHandler() {
        this.eventHandler = new ClientEventHandler(this, model, playerRegistry);
    }

    //---------------------------------------INTERNAL CALLS------------------------------------------

    /**
     * Sets the client model's meta state to CREATION, allowing the user to create
     * a new game
     */
    public void showGameCreation() {
        model.setMetaState(MetaState.CREATION);
    }

    /**
     * Create a new player for the given nickname and sets it
     * as the client player in the model.
     *
     * @param nickname the nickname of the player to be created
     */
    public void setMyNickname(String nickname) {
        Player player = playerRegistry.addPlayer(nickname);
        model.setPlayer(player);
    }

    /**
     * Removes the client player from the registry and from the client model.
     */
    public void clearNickname() {
        Player player = model.getClientPlayer();
        playerRegistry.removePlayer(player);
        model.setPlayer(null);
    }

    @Override
    public void clearModel() {
        model.getPlayers().stream()
                .filter(p -> !p.equals(model.getClientPlayer()))
                .forEach(playerRegistry::removePlayer);
        model.clearGame();
    }

    /**
     * Reports an error message to the view.
     *
     * @param message the error message to be reported
     */
    public void reportError(String message) {
        view.reportError(message);
    }

    /**
     * Simulates a disconnection from the server.
     */
    public void dropConnection() {
        server.dropConnection();
    }

    /**
     * Signals to the client that it has been disconnected from the server.
     * Afterwards, the client will attempt to reconnect at regular intervals.
     */
    public void signalDisconnection() {
        synchronized (connectionLock) {
            if (connected) {
                view.reportError("You have been disconnected, trying to reconnect...");
                connected = false;
                connectionFuture = scheduler.scheduleAtFixedRate(this::reconnect, 3, 3, TimeUnit.SECONDS);
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

    @Override
    public void quit() {
        try {
            clearModel();
            server.leaveLobby();
        }
        catch (RuntimeException e) {
            view.reportError(e.getMessage());
        }
    }
}

