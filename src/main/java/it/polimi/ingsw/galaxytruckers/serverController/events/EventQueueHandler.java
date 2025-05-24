package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.network.server.SessionManager;
import it.polimi.ingsw.galaxytruckers.network.server.VirtualClient;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Lobby;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

public class EventQueueHandler implements EventHandler {
    private final Lobby lobby;
    private final EventQueue eventQueue;
    private Thread thread;

    public EventQueueHandler(Lobby lobby) {
        this.lobby = lobby;
        this.eventQueue = lobby.getEventQueue();
        this.thread = new Thread(this::processQueue, "ModelEvent-handler-thread");
    }

    public void start() {
        this.thread.start();
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
                return;
            }
        }
    }

    @Override
    public void handleEvent(Event event) {
        switch (event) {
            case ActivateComponentEvent activateComponentEvent -> {
                handle(activateComponentEvent);
            }
            case GoodsUpdateEvent goodsUpdateEvent -> {
                handle(goodsUpdateEvent);
            }
            case UseBatteryEvent useBatteryEvent -> {
                handle(useBatteryEvent);
            }
            case InitializeCabinEvent initializeCabinEvent -> {
                handle(initializeCabinEvent);
            }
            case CargoHoldUpdateEvent cargoHoldUpdateEvent -> {
                handle(cargoHoldUpdateEvent);
            }
            case FlightBoardUpdateEvent flightBoardUpdateEvent -> {
                handle(flightBoardUpdateEvent);
            }
            case FlipHourglassEvent flipHourglassEvent -> {
                handle(flipHourglassEvent);
            }
            case GameEndEvent gameEndEvent -> {
                handle(gameEndEvent);
            }
            case GoodsBufferUpdateEvent goodsBufferUpdateEvent -> {
                handle(goodsBufferUpdateEvent);
            }
            case GrabStashedComponentEvent grabStashedComponentEvent -> {
                handle(grabStashedComponentEvent);
            }
            case HourglassEndEvent hourglassEndEvent -> {
                handle(hourglassEndEvent);
            }
            case ValidateShipEvent validateShipEvent -> {
                handle(validateShipEvent);
            }
            case JoinLobbyEvent joinLobbyEvent -> {
                handle(joinLobbyEvent);
            }
            case NewCardEvent newCardEvent -> {
                handle(newCardEvent);
            }
            case ForecastDetailsEvent forecastDetailsEvent -> {
                handle(forecastDetailsEvent);
            }
            case PlaceComponentEvent placeComponentEvent -> {
                handle(placeComponentEvent);
            }
            case PlanetChoiceEvent planetChoiceEvent -> {
                handle(planetChoiceEvent);
            }
            case PlayerDisconnectionEvent playerDisconnectionEvent -> {
                handle(playerDisconnectionEvent);
            }
            case PlayerExitEvent playerExitEvent -> {
                handle(playerExitEvent);
            }
            case ProjectileEvent projectileEvent -> {
                handle(projectileEvent);
            }
            case RejectComponentEvent rejectComponentEvent -> {
                handle(rejectComponentEvent);
            }
            case ReleaseForecastEvent releaseForecastEvent -> {
                handle(releaseForecastEvent);
            }
            case RemoveComponentEvent removeComponentEvent -> {
                handle(removeComponentEvent);
            }
            case RequestFaceDownComponentEvent requestFaceDownComponentEvent -> {
                handle(requestFaceDownComponentEvent);
            }
            case RequestFaceUpComponentEvent requestFaceUpComponentEvent -> {
                handle(requestFaceUpComponentEvent);
            }
            case SelectionPointsEvent selectionPointsEvent -> {
                handle(selectionPointsEvent);
            }
            case ShipNotConnectedEvent shipNotConnectedEvent -> {
                handle(shipNotConnectedEvent);
            }
            case ShipPieceRemoveEvent shipPieceRemoveEvent -> {
                handle(shipPieceRemoveEvent);
            }
            case ShipStatUpdateEvent shipStatUpdateEvent -> {
                handle(shipStatUpdateEvent);
            }
            case StartBuildingEvent startBuildingEvent -> {
                handle(startBuildingEvent);
            }
            case StashComponentEvent stashComponentEvent -> {
                handle(stashComponentEvent);
            }
            case SurrenderEvent surrenderEvent -> {
                handle(surrenderEvent);
            }
        }
    }

    private void handle(RemoveComponentEvent removeComponentEvent) {
        //TODO: define method
    }


    private void handle(JoinLobbyEvent joinLobbyEvent) {
        for (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            if (joinLobbyEvent.playerName().equals(player.getNickname())) {
                client.setupLobby(lobby.getId(), joinLobbyEvent.playerColors());
            } else {
                client.updateLobbyPlayers(joinLobbyEvent.playerColors());
            }
        }
    }

    
    private void handle(StartBuildingEvent startBuildingEvent) {
        for (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            client.notifyStartBuilding(lobby.getLevel(), lobby.getNumPlayers());
        }
    }

    
    private void handle(RequestFaceUpComponentEvent requestFaceUpComponentEvent) {
        for (Player p : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(p);
            client.notifyFaceUpComponentRequest(
                    requestFaceUpComponentEvent.playerName(),
                    requestFaceUpComponentEvent.componentId());
        }
    }

    
    private void handle(RequestFaceDownComponentEvent requestFaceDownComponentEvent) {
        for (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            client.notifyFaceDownComponentRequest(
                    requestFaceDownComponentEvent.playerName(),
                    requestFaceDownComponentEvent.componentId()
            );
        }
    }

    
    private void handle(RejectComponentEvent rejectComponentEvent) {
        for (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            client.notifyComponentRejection(
                    rejectComponentEvent.playerName(),
                    rejectComponentEvent.componentId()
            );
        }
    }

    
    private void handle(FlipHourglassEvent flipHourglassEvent) {
        for (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            client.notifyHourglassFlipped(
                    flipHourglassEvent.playerName(),
                    flipHourglassEvent.isLast()
            );
        }
    }

    
    private void handle(PlaceComponentEvent placeComponentEvent) {
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

    
    private void handle(ForecastDetailsEvent forecastDetailsEvent) {
        for (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            if (forecastDetailsEvent.playerName().equals(player.getNickname())) {
                client.sendForecastDeck(
                        forecastDetailsEvent.forecastDeckIds()
                );
            } else {
                client.notifyPeekForecast(
                        forecastDetailsEvent.playerName(),
                        forecastDetailsEvent.deckIndex()
                );
            }
        }
    }

    
    private void handle(ReleaseForecastEvent releaseForecastEvent) {
        for (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            client.notifyReleaseForecast(
                    releaseForecastEvent.playerName(),
                    releaseForecastEvent.deckIndex()
            );
        }
    }

    
    private void handle(GrabStashedComponentEvent grabStashedComponentEvent) {
        for (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            client.notifyGrabFromStash(
                    grabStashedComponentEvent.playerName(),
                    grabStashedComponentEvent.componentId(),
                    grabStashedComponentEvent.stashedComponentIds()
            );
        }
    }

    
    private void handle(StashComponentEvent stashComponentEvent) {
        for (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            client.notifyStashComponent(
                    stashComponentEvent.playerName(),
                    stashComponentEvent.stashedComponentIds()
            );
        }
    }

    
    private void handle(FlightBoardUpdateEvent flightBoardUpdateEvent) {
        for  (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            client.notifyPlayerPosition(
                    flightBoardUpdateEvent.playerName(),
                    flightBoardUpdateEvent.position()
            );
        }
    }

    
    private void handle(InitializeCabinEvent initializeCabinEvent) {
        for (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            client.notifyCabinUpdate(
                    initializeCabinEvent.playerName(),
                    initializeCabinEvent.point(),
                    initializeCabinEvent.numResidents(),
                    initializeCabinEvent.crewType()
            );
        }
    }

    
    private void handle(UseBatteryEvent useBatteryEvent) {
        for (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            client.notifyBatteryUpdate(
                    useBatteryEvent.playerName(),
                    useBatteryEvent.point(),
                    useBatteryEvent.numBatteries()
            );
        }
    }

    
    private void handle(CargoHoldUpdateEvent cargoHoldUpdateEvent) {
        for (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            client.notifyCargoHoldUpdate(
                    cargoHoldUpdateEvent.playerName(),
                    cargoHoldUpdateEvent.point(),
                    cargoHoldUpdateEvent.cargo()
            );
        }
    }

    
    private void handle(ShipStatUpdateEvent shipStatUpdateEvent) {
        for (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            client.notifyShipStatUpdate(
                    shipStatUpdateEvent.playerName(),
                    shipStatUpdateEvent.statType(),
                    shipStatUpdateEvent.value()
            );
        }
    }

    
    private void handle(NewCardEvent newCardEvent) {
        for (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            client.notifyNewCard(newCardEvent.cardId());
        }
    }

    
    private void handle(SurrenderEvent surrenderEvent) {
        for (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            client.notifySurrender(surrenderEvent.playerNames());
        }
    }

    
    private void handle(HourglassEndEvent hourglassEndEvent) {
        for (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            client.notifyHourglassEnd();
        }
    }

    
    private void handle(ShipPieceRemoveEvent shipPieceRemoveEvent) {
        for (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            client.notifyShipPieceRemoval(
                    shipPieceRemoveEvent.playerName(),
                    shipPieceRemoveEvent.positions()
            );
        }
    }

    
    private void handle(SelectionPointsEvent selectionPointsEvent) {
        for (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            client.notifySelection(
                    selectionPointsEvent.playerName(),
                    selectionPointsEvent.points(),
                    selectionPointsEvent.batteries()
            );
        }
    }

    
    private void handle(PlanetChoiceEvent planetChoiceEvent) {
        for (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            client.notifyPlanetChoice(
                    planetChoiceEvent.playerName(),
                    planetChoiceEvent.planetId(),
                    planetChoiceEvent.cargoPoints());
        }
    }

    
    private void handle(GoodsBufferUpdateEvent goodsBufferUpdateEvent) {
        for (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            client.updateGoodsBuffer(goodsBufferUpdateEvent.adding(),
                    goodsBufferUpdateEvent.goodsType()
            );
        }
    }

    
    private void handle(ProjectileEvent projectileEvent) {
        for (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            client.showProjectile(
                    projectileEvent.playerName(),
                    projectileEvent.projectileType(),
                    projectileEvent.direction(),
                    projectileEvent.roll(),
                    projectileEvent.selectablePoints(),
                    projectileEvent.batteries()
            );
        }
    }

    
    private void handle(GameEndEvent gameEndEvent) {
        for (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            client.showFinalScores(
                    gameEndEvent.playerToScore()
            );
        }
    }

    
    private void handle(ValidateShipEvent validateShipEvent) {
        for (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            client.notifyInvalidShipsUpdate(
                    validateShipEvent.playerName()
            );
        }
    }

    
    private void handle(ShipNotConnectedEvent shipNotConnectedEvent) {
        Player player = Player.getPlayer(shipNotConnectedEvent.playerName());
        VirtualClient client = SessionManager.getInstance().getClient(player);
        //client.showShipPieces(shipNotConnectedEvent.playerName(), shipNotConnectedEvent.shipPieces());//todo: now it s a map
    }

    
    private void handle(ActivateComponentEvent activateComponentEvent) {
        for (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            client.notifyComponentActivation(
                    activateComponentEvent.playerName(),
                    activateComponentEvent.point()
            );
        }
    }

    
    private void handle(GoodsUpdateEvent goodsUpdateEvent) {
        for (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            client.notifyAddGoods(
                    goodsUpdateEvent.playerName(),
                    goodsUpdateEvent.goods(),
                    goodsUpdateEvent.cargos()
            );
        }
    }

    
    private void handle(PlayerDisconnectionEvent playerDisconnectionEvent) {
        Player disconnectedPlayer = Player.getPlayer(playerDisconnectionEvent.playerName());
        for (Player player : lobby.getPlayers()) {
            if (!player.equals(disconnectedPlayer)) {
                VirtualClient client = SessionManager.getInstance().getClient(player);
                client.notifyPlayerDisconnection(playerDisconnectionEvent.playerName());
            }
        }
        stop();
    }

    
    private void handle(PlayerExitEvent playerExitEvent) {
        for (Player player : lobby.getPlayers()) {
            VirtualClient client = SessionManager.getInstance().getClient(player);
            //TODO: notify player exit to clients
        }
        stop();
    }
}
