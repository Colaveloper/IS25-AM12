package it.polimi.ingsw.galaxytruckers.serverController.events;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.network.server.ClientHandler;
import it.polimi.ingsw.galaxytruckers.network.server.SessionManager;
import it.polimi.ingsw.galaxytruckers.network.server.VirtualClient;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Lobby;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.util.ArrayList;
import java.util.List;

public class EventQueueHandler implements EventHandler {
    private final List<Player> players;
    private final EventQueue eventQueue;
    private Thread thread;
    private Runnable afterEach = () -> {};

    @VisibleForTesting
    protected void setAfterEach(Runnable afterEach) {
        this.afterEach = afterEach;
    }

    public EventQueueHandler(List<Player> players, EventQueue eventQueue) {
        this.players = players;
        this.eventQueue = eventQueue;
    }

    public void start() {
        if (this.thread == null) {
            this.thread = new Thread(this::processQueue, "ModelEvent-handler-thread");
        }
        this.thread.start();
    }

    public void stop() {
        if (this.thread != null) {
            this.thread.interrupt();
            this.thread = null;
        }
    }

    private void processQueue() {
        try {
            while (true) {
                Event event = eventQueue.dequeue();
                handleEvent(event);
                afterEach.run();
            }
        } catch (InterruptedException e) {
            System.out.println("EventQueueHandler interrupted");
        }
    }

    @Override
    public void handleEvent(Event event) {
        switch (event) {
            case ActivateComponentEvent activateComponentEvent -> {
                broadcastEvent(activateComponentEvent);
            }
            case FlightBoardUpdateEvent flightBoardUpdateEvent -> {
                broadcastEvent(flightBoardUpdateEvent);
            }
            case FlipHourglassEvent flipHourglassEvent -> {
                broadcastEvent(flipHourglassEvent);
            }
            case ForecastDetailsEvent forecastDetailsEvent -> {
                sendEvent(forecastDetailsEvent.playerName(), forecastDetailsEvent);
            }
            case GameEndEvent gameEndEvent -> {
                broadcastEvent(gameEndEvent);
            }
            case GoodsUpdateEvent goodsUpdateEvent -> {
                broadcastEvent(goodsUpdateEvent);
            }
            case GrabStashedComponentEvent grabStashedComponentEvent -> {
                broadcastEvent(grabStashedComponentEvent);
            }
            case HourglassEndEvent hourglassEndEvent -> {
                broadcastEvent(hourglassEndEvent);
            }
            case InitializeCabinEvent initializeCabinEvent -> {
                broadcastEvent(initializeCabinEvent);
            }
            case JoinLobbyEvent joinLobbyEvent -> {
                broadcastEvent(joinLobbyEvent);
            }
            case LobbyDetailsEvent lobbyDetailsEvent -> {
                sendEvent(lobbyDetailsEvent.playerName(), lobbyDetailsEvent);
            }
            case NewCardEvent newCardEvent -> {
                broadcastEvent(newCardEvent);
            }
            case PeekForecastEvent peekForecastEvent -> {
                broadcastEvent(peekForecastEvent);
            }
            case PlaceComponentEvent placeComponentEvent -> {
                broadcastEvent(placeComponentEvent);
            }
            case PlanetChoiceEvent planetChoiceEvent -> {
                broadcastEvent(planetChoiceEvent);
            }
            case PlayerDisconnectionEvent playerDisconnectionEvent -> {
                broadcastEvent(playerDisconnectionEvent);
                stop();
            }
            case PlayerExitEvent playerExitEvent -> {
                broadcastEvent(playerExitEvent);
                stop();
            }
            case RejectComponentEvent rejectComponentEvent -> {
                broadcastEvent(rejectComponentEvent);
            }
            case ReleaseForecastEvent releaseForecastEvent -> {
                broadcastEvent(releaseForecastEvent);
            }
            case RemoveComponentEvent removeComponentEvent -> {
                broadcastEvent(removeComponentEvent);
            }
            case RequestFaceDownComponentEvent requestFaceDownComponentEvent -> {
                broadcastEvent(requestFaceDownComponentEvent);
            }
            case RequestFaceUpComponentEvent requestFaceUpComponentEvent -> {
                broadcastEvent(requestFaceUpComponentEvent);
            }
            case ShipNotConnectedEvent shipNotConnectedEvent -> {
                broadcastEvent(shipNotConnectedEvent);
            }
            case ShipPieceRemoveEvent shipPieceRemoveEvent -> {
                broadcastEvent(shipPieceRemoveEvent);
            }
            case ShipStatUpdateEvent shipStatUpdateEvent -> {
                broadcastEvent(shipStatUpdateEvent);
            }
            case StashComponentEvent stashComponentEvent -> {
                broadcastEvent(stashComponentEvent);
            }
            case SurrenderEvent surrenderEvent -> {
                broadcastEvent(surrenderEvent);
            }
            case UseBatteryEvent useBatteryEvent -> {
                broadcastEvent(useBatteryEvent);
            }
            case ValidateShipEvent validateShipEvent -> {
                broadcastEvent(validateShipEvent);
            }
        }
    }

    private List<Player> getPlayers() {
        synchronized (players) {
            return new ArrayList<>(players);
        }
    }

    private void broadcastEvent(Event event) {
        for (Player player : getPlayers()) {
            ClientHandler virtualClient = SessionManager.getInstance().getClient(player);
            if (virtualClient != null) {
                virtualClient.notifyEvent(event);
            }
        }
    }

    private void sendEvent(String playerName, Event event) {
        ClientHandler virtualClient = SessionManager.getInstance().getClient(Player.getPlayer(playerName));
        if (virtualClient != null) {
            virtualClient.notifyEvent(event);
        }
    }
}
