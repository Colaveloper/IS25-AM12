package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.network.server.SessionManager;
import it.polimi.ingsw.galaxytruckers.network.server.VirtualClient;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Lobby;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

public class EventQueueHandler implements EventHandler, EventVisitor {
    private final Lobby lobby;
    private final EventQueue eventQueue;
    private Thread thread;

    public EventQueueHandler(Lobby lobby) {
        this.lobby = lobby;
        this.eventQueue = lobby.getEventQueue();
    }

    public void start() {
        this.thread = new Thread(this::processQueue, "Event-handler-thread");
    }

    public void stop() {
        if (this.thread != null) {
            this.thread.interrupt();
            this.thread = null;
        }
    }

    private void processQueue() {
        while (true) {
            try {
                Event event = eventQueue.dequeue();
                handleEvent(event);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public void handleEvent(Event event) {
        event.accept(this);
    }

    @Override
    public void visit(LobbyEvent lobbyEvent) {
        for (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            if (lobbyEvent.playerName().equals(player.getNickname())) {
                client.setupLobby(lobby.getId(),lobbyEvent.playerColors());
            } else {
                client.updateLobbyPlayers(lobbyEvent.playerColors());
            }
        }
    }

    @Override
    public void visit(StartBuildingEvent startBuildingEvent) {
        for (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            client.notifyStartBuilding();
        }
    }

    @Override
    public void visit(RequestFaceUpComponentEvent requestFaceUpComponentEvent) {
        for (Player p : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(p);
            client.notifyFaceUpComponentRequest(
                    requestFaceUpComponentEvent.playerName(),
                    requestFaceUpComponentEvent.componentId(),
                    requestFaceUpComponentEvent.faceUpComponentIds());
        }
    }

    @Override
    public void visit(RequestFaceDownComponentEvent requestFaceDownComponentEvent) {
        for (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            client.notifyFaceDownComponentRequest(
                    requestFaceDownComponentEvent.playerName(),
                    requestFaceDownComponentEvent.componentId(),
                    requestFaceDownComponentEvent.numFaceDown()
            );
        }
    }

    @Override
    public void visit(RejectComponentEvent rejectComponentEvent) {
        for (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            client.notifyComponentRejection(
                    rejectComponentEvent.playerName(),
                    rejectComponentEvent.componentId(),
                    rejectComponentEvent.faceUpComponentIds()
            );
        }
    }

    @Override
    public void visit(FlipHourglassEvent flipHourglassEvent) {
        for (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            client.notifyHourglassFlipped(
                    flipHourglassEvent.playerName(),
                    flipHourglassEvent.isLast()
            );
        }
    }

    @Override
    public void visit(ShipMapUpdateEvent shipMapUpdateEvent) {
        for (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            client.notifyShipMapUpdate(
                    shipMapUpdateEvent.playerName(),
                    shipMapUpdateEvent.componentIdMap()
            );
        }
    }

    @Override
    public void visit(PeekForecastEvent peekForecastEvent) {
        for (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            if (peekForecastEvent.playerName().equals(player.getNickname())) {
                client.sendForecastDeck(
                        peekForecastEvent.deckIndex(),
                        peekForecastEvent.forecastDeckIds()
                );
            } else {
                client.notifyPeekForecast(
                        peekForecastEvent.playerName(),
                        peekForecastEvent.deckIndex()
                );
            }
        }
    }

    @Override
    public void visit(ReleaseForecastEvent releaseForecastEvent) {
        for (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            client.notifyReleaseForecast(
                    releaseForecastEvent.playerName(),
                    releaseForecastEvent.deckIndex()
            );
        }
    }

    @Override
    public void visit(GrabStashedComponentEvent grabStashedComponentEvent) {
        for (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            client.notifyGrabFromStash(
                    grabStashedComponentEvent.playerName(),
                    grabStashedComponentEvent.componentId(),
                    grabStashedComponentEvent.stashedComponentIds()
            );
        }
    }

    @Override
    public void visit(StashComponentEvent stashComponentEvent) {
        for (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            client.notifyStashComponent(
                    stashComponentEvent.playerName(),
                    stashComponentEvent.stashedComponentIds()
            );
        }
    }

    @Override
    public void visit(PlaceShipOnFlightBoardEvent placeShipOnFlightBoardEvent) {
        for  (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            client.notifyPlaceShipOnFlightBoard(
                    placeShipOnFlightBoardEvent.playerName(),
                    placeShipOnFlightBoardEvent.playerToPlace()
            );
        }
    }

    @Override
    public void visit(CabinUpdate cabinUpdate) {
        for (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            client.notifyCabinUpdate(
                    cabinUpdate.playerName(),
                    cabinUpdate.point(),
                    cabinUpdate.numResidents(),
                    cabinUpdate.crewType()
            );
        }
    }

    @Override
    public void visit(BatteryUpdate batteryUpdate) {
        for (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            client.notifyBatteryUpdate(
                    batteryUpdate.playerName(),
                    batteryUpdate.point(),
                    batteryUpdate.numBatteries()
            );
        }
    }

    @Override
    public void visit(CargoHoldUpdate cargoHoldUpdate) {
        for (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            client.notifyCargoHoldUpdate(
                    cargoHoldUpdate.playerName(),
                    cargoHoldUpdate.point(),
                    cargoHoldUpdate.cargo()
            );
        }
    }

    @Override
    public void visit(ShipStatUpdateEvent shipStatUpdateEvent) {
        for (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            client.notifyShipStatUpdate(
                    shipStatUpdateEvent.playerName(),
                    shipStatUpdateEvent.statType(),
                    shipStatUpdateEvent.value()
            );
        }
    }

    @Override
    public void visit(NewCardEvent newCardEvent) {
        for (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            client.notifyNewCard(newCardEvent.cardId());
        }
    }

    @Override
    public void visit(SurrenderEvent surrenderEvent) {
        for (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            client.notifySurrender(surrenderEvent.playerNames());
        }
    }
}
