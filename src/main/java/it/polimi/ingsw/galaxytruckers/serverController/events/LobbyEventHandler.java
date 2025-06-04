package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.network.server.ClientHandler;
import it.polimi.ingsw.galaxytruckers.network.server.SessionManager;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.*;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.util.List;
import java.util.function.Supplier;

public class LobbyEventHandler extends EventQueueHandler<LobbyEvent> {
    private final Supplier<List<Player>> supplier;

    public LobbyEventHandler(EventQueue<LobbyEvent> eventQueue, Supplier<List<Player>> supplier) {
        super(eventQueue);
        this.supplier = supplier;
    }

    @Override
    public void handleEvent(LobbyEvent event) {
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
            case GameStateUpdateEvent gameStateUpdateEvent -> {
                broadcastEvent(gameStateUpdateEvent);
            }
            case LoseCrewEvent loseCrewEvent -> {
                broadcastEvent(loseCrewEvent);
            }
        }
    }

    private List<Player> getPlayers() {
        return supplier.get();
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
