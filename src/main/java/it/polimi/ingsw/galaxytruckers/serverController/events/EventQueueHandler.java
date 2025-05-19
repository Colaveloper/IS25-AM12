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
        this.thread = new Thread(this::processQueue, "ModelEvent-handler-thread");
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
                    requestFaceUpComponentEvent.componentId());
        }
    }

    @Override
    public void visit(RequestFaceDownComponentEvent requestFaceDownComponentEvent) {
        for (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            client.notifyFaceDownComponentRequest(
                    requestFaceDownComponentEvent.playerName(),
                    requestFaceDownComponentEvent.componentId()
            );
        }
    }

    @Override
    public void visit(RejectComponentEvent rejectComponentEvent) {
        for (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            client.notifyComponentRejection(
                    rejectComponentEvent.playerName(),
                    rejectComponentEvent.componentId()
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
    public void visit(PlaceComponentEvent placeComponentEvent) {
        for (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            client.notifyComponentPositioning(
                    placeComponentEvent.playerName(),
                    placeComponentEvent.componentId(),
                    placeComponentEvent.rotation(),
                    placeComponentEvent.position()
            );
        }
    }

    @Override
    public void visit(PeekForecastEvent peekForecastEvent) {
        for (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            if (peekForecastEvent.playerName().equals(player.getNickname())) {
                client.sendForecastDeck(
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
    public void visit(FlightBoardUpdateEvent flightBoardUpdateEvent) {
        for  (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            client.notifyPlayerPosition(
                    flightBoardUpdateEvent.playerName(),
                    flightBoardUpdateEvent.position()
            );
        }
    }

    @Override
    public void visit(CabinUpdateEvent cabinUpdateEvent) {
        for (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            client.notifyCabinUpdate(
                    cabinUpdateEvent.playerName(),
                    cabinUpdateEvent.point(),
                    cabinUpdateEvent.numResidents(),
                    cabinUpdateEvent.crewType()
            );
        }
    }

    @Override
    public void visit(BatteryUpdateEvent batteryUpdateEvent) {
        for (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            client.notifyBatteryUpdate(
                    batteryUpdateEvent.playerName(),
                    batteryUpdateEvent.point(),
                    batteryUpdateEvent.numBatteries()
            );
        }
    }

    @Override
    public void visit(CargoHoldUpdateEvent cargoHoldUpdateEvent) {
        for (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            client.notifyCargoHoldUpdate(
                    cargoHoldUpdateEvent.playerName(),
                    cargoHoldUpdateEvent.point(),
                    cargoHoldUpdateEvent.cargo()
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

    @Override
    public void visit(HourglassEndEvent hourglassEndEvent) {
        for (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            client.notifyHourglassEnd();
        }
    }

    @Override
    public void visit(ShipPieceRemoveEvent shipPieceRemoveEvent) {
        for (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            client.notifyShipPieceRemoval(
                    shipPieceRemoveEvent.playerName(),
                    shipPieceRemoveEvent.positions()
            );
        }
    }

    @Override
    public void visit(SelectionPointsEvent selectionPointsEvent) {
        for (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            client.notifySelection(
                    selectionPointsEvent.playerName(),
                    selectionPointsEvent.points()
            );
        }
    }

    @Override
    public void visit(PlanetChoiceEvent planetChoiceEvent) {
        for (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            client.notifyPlanetChoice(
                    planetChoiceEvent.playerName(),
                    planetChoiceEvent.planetId(),
                    planetChoiceEvent.cargoPoints());
        }
    }

    @Override
    public void visit(GoodsBufferUpdateEvent goodsBufferUpdateEvent) {
        for (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            client.updateGoodsBuffer(
                    goodsBufferUpdateEvent.goodsType()
            );
        }
    }

    @Override
    public void visit(ProjectileEvent projectileEvent) {
        for (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            client.showProjectile(
                    projectileEvent.projectileType(),
                    projectileEvent.direction(),
                    projectileEvent.roll(),
                    projectileEvent.selectablePoints(),
                    projectileEvent.batteries()
            );
        }
    }

    @Override
    public void visit(GameEndEvent gameEndEvent) {
        for (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            client.showFinalScores(
                    gameEndEvent.playerToScore()
            );
        }
    }

    @Override
    public void visit(InvalidShipsUpdateEvent invalidShipsUpdateEvent) {
        for (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            client.notifyInvalidShipsUpdate(
                    invalidShipsUpdateEvent.invalidPlayers()
            );
        }
    }

    @Override
    public void visit(ShipNotConnectedEvent shipNotConnectedEvent) {
        Player player = Player.getPlayer(shipNotConnectedEvent.playerName());
        VirtualClient client = SessionManager.getInstance().getClient(player);
        client.showShipPieces(shipNotConnectedEvent.shipPieces());
    }
}
