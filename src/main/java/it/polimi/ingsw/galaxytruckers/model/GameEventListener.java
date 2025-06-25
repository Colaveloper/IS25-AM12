package it.polimi.ingsw.galaxytruckers.model;

import it.polimi.ingsw.galaxytruckers.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.SurrenderCause;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.*;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.model.state.GameState;
import it.polimi.ingsw.galaxytruckers.serverController.dto.DtoConverter;
import it.polimi.ingsw.galaxytruckers.serverController.dto.GameSnapshot;
import it.polimi.ingsw.galaxytruckers.serverController.dto.LobbyDetailsDTO;
import it.polimi.ingsw.galaxytruckers.serverController.dto.ShipBoardDTO;
import it.polimi.ingsw.galaxytruckers.serverController.events.*;
import it.polimi.ingsw.galaxytruckers.serverController.events.EventListener;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.*;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;
import it.polimi.ingsw.galaxytruckers.serverController.utils.ConversionUtils;
import it.polimi.ingsw.galaxytruckers.view.Direction;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class GameEventListener {
    private final EventListener<LobbyEvent> controllerListener;
    private final Supplier<LobbyDetailsDTO> lobbyDetailsSupplier;
    private Runnable startAdventureCallback = () -> {};

    public GameEventListener(EventListener<LobbyEvent> controllerListener, Supplier<LobbyDetailsDTO> lobbyDetailsSupplier) {
        this.controllerListener = controllerListener;
        this.lobbyDetailsSupplier = lobbyDetailsSupplier;
    }

    public void setStartAdventureCallback(Runnable startAdventureCallback) {
        this.startAdventureCallback = startAdventureCallback;
    }

    public void requestSnapshot(Game game, ShipBoard shipBoard) {
        controllerListener.notifyEvent(new GameSnapshotEvent(
                ConversionUtils.convert(shipBoard),
                lobbyDetailsSupplier.get(),
                getGameSnapshot(game)
        ));
    }

    private GameSnapshot getGameSnapshot(Game game) {
        Map<String, ShipBoardDTO> ships = new HashMap<>();
        for (ShipBoard s : game.getShipBoards()) {
            ships.put(ConversionUtils.convert(s),DtoConverter.getShipBoard(s));
        }
        return new GameSnapshot(
                DtoConverter.getFlightBoard(game.getFlightBoard()),
                DtoConverter.getComplexState(game.getCurrentState()),
                ships,
                (game.getDeck().getCurrentCard() != null)
                        ? game.getDeck().getCurrentCard().getId()
                        : -1
        );
    }

    public void notifyStartAdventure() {
        startAdventureCallback.run();
    }

    public void notifyActivateComponentEvent(ShipBoard shipBoard, Point point, boolean active) {
        controllerListener.notifyEvent(new ActivateComponentEvent(ConversionUtils.convert(shipBoard), point, active));
    }

    public void notifyFlightBoardUpdateEvent(ShipBoard shipBoard, int position) {
        controllerListener.notifyEvent(new FlightBoardUpdateEvent(
                ConversionUtils.convert(shipBoard),
                position
        ));
    }

    public void notifyFlipHourglassEvent(ShipBoard shipBoard) {
        controllerListener.notifyEvent(new FlipHourglassEvent(ConversionUtils.convert(shipBoard)));
    }

    public void notifyForecastDetailsEvent(ShipBoard shipBoard, List<AdventureCard> adventureCards) {
        controllerListener.notifyEvent(new ForecastDetailsEvent(
                ConversionUtils.convert(shipBoard),
                adventureCards.stream().map(AdventureCard::getId).toList()));
    }

    public void notifyGameEndEvent(Map<ShipBoard, Integer> finalScores) {
        controllerListener.notifyEvent(new GameEndEvent(
                ConversionUtils.convertMap(finalScores)
        ));
    }

    public void notifyGameStateUpdateEvent(GameState gameState) {
        System.out.println("Game state update: " + gameState.getClass().getSimpleName());
        controllerListener.notifyEvent(new GameStateUpdateEvent(
                DtoConverter.getState(gameState)
        ));
    }

    public void notifyGoodsUpdateEvent(ShipBoard shipBoard, Point point, GoodsType goodsType, boolean add) {
        controllerListener.notifyEvent(new GoodsUpdateEvent(
                ConversionUtils.convert(shipBoard),
                point,
                goodsType,
                add
        ));
    }

    public void notifyGrabPlacedComponentEvent(ShipBoard shipBoard) {
        controllerListener.notifyEvent(new GrabPlacedComponentEvent(
                ConversionUtils.convert(shipBoard)
        ));
    }

    public void notifyGrabStashedComponentEvent(ShipBoard shipBoard, int index) {
        controllerListener.notifyEvent(new GrabStashedComponentEvent(
                ConversionUtils.convert(shipBoard),
                index
        ));
    }

    public void notifyHourglassEndEvent() {
        controllerListener.notifyEvent(new HourglassEndEvent());
    }

    public void notifyCabinInitializationEvent(ShipBoard shipBoard, Point point, CrewType crewType) {
        controllerListener.notifyEvent(new InitializeCabinEvent(ConversionUtils.convert(shipBoard), point, crewType));
    }

    public void notifyLoseCrewEvent(ShipBoard shipBoard, Point point) {
        controllerListener.notifyEvent(new LoseCrewEvent(
                ConversionUtils.convert(shipBoard),
                point
        ));
    }

    public void notifyNewCardEvent(AdventureCard adventureCard) {
        controllerListener.notifyEvent(new NewCardEvent(
                adventureCard.getId()
        ));
    }

    public void notifyPeekForecastEvent(ShipBoard shipBoard, int deckIndex) {
        controllerListener.notifyEvent(new PeekForecastEvent(
                ConversionUtils.convert(shipBoard),
                deckIndex));
    }

    public void notifyPlaceComponentEvent(ShipBoard shipBoard, Direction orientation, Point position) {
        controllerListener.notifyEvent(new PlaceComponentEvent(
                ConversionUtils.convert(shipBoard),
                position,
                orientation
        ));
    }

    public void notifyPlanetChoiceEvent(ShipBoard shipBoard, int planetId, ShipBoard nextShipBoard) {
        controllerListener.notifyEvent(new PlanetChoiceEvent(
                ConversionUtils.convert(shipBoard),
                planetId,
                ConversionUtils.convert(nextShipBoard)
        ));
    }

    public void notifyRejectComponentEvent(ShipBoard shipBoard) {
        controllerListener.notifyEvent(new RejectComponentEvent(ConversionUtils.convert(shipBoard)));
    }

    public void notifyReleaseForecastEvent(ShipBoard shipBoard, int deckIndex) {
        controllerListener.notifyEvent(new ReleaseForecastEvent(
                ConversionUtils.convert(shipBoard), deckIndex));
    }

    public void notifyRemoveComponentEvent(ShipBoard shipBoard, Point point) {
        controllerListener.notifyEvent(new RemoveComponentEvent(
                ConversionUtils.convert(shipBoard),
                point
        ));
    }

    public void notifyRequestFaceDownComponentEvent(ShipBoard shipBoard, Component component) {
        controllerListener.notifyEvent(new RequestFaceDownComponentEvent(
                ConversionUtils.convert(shipBoard),
                component.getId()
        ));
    }

    public void notifyRequestFaceUpComponentEvent(ShipBoard shipBoard, Component component) {
        controllerListener.notifyEvent(new RequestFaceUpComponentEvent(
                ConversionUtils.convert(shipBoard),
                component.getId()
        ));
    }

    public void notifyShipNotConnectedEvent(ShipBoard shipBoard, List<Set<Point>> shipPieces) {
        controllerListener.notifyEvent(new ShipNotConnectedEvent(
                ConversionUtils.convert(shipBoard),
                shipPieces
        ));
    }

    public void notifyShipPieceRemovalEvent(ShipBoard shipBoard, int pieceIndex) {
        controllerListener.notifyEvent(new ShipPieceRemoveEvent(
                ConversionUtils.convert(shipBoard),
                pieceIndex
        ));
    }

    public void notifyGrabCreditsEvent(ShipBoard shipBoard, int value) {
        controllerListener.notifyEvent(new GrabCreditsEvent(
                ConversionUtils.convert(shipBoard),
                value
        ));
    }

    public void notifyStashComponentEvent(ShipBoard shipBoard) {
        controllerListener.notifyEvent(new StashComponentEvent(ConversionUtils.convert(shipBoard)));
    }

    public void notifySurrenderEvent(List<ShipBoard> ships) {
        controllerListener.notifyEvent(new SurrenderEvent(
                ConversionUtils.convertCollection(ships, ArrayList::new)
        ));
    }

    public void notifySurrenderRequestEvent(ShipBoard shipBoard, SurrenderCause cause) {
        controllerListener.notifyEvent(new SurrenderRequestEvent(ConversionUtils.convert(shipBoard), cause));
    }

    public void notifyUseBatteryEvent(ShipBoard shipBoard, Point point) {
        controllerListener.notifyEvent(new UseBatteryEvent(ConversionUtils.convert(shipBoard), point));
    }

    public void notifyValidateShipEvent(ShipBoard shipBoard) {
        controllerListener.notifyEvent(new ValidateShipEvent(
                ConversionUtils.convert(shipBoard)
        ));
    }

    public void notifyCurrentPlayerUpdateEvent(ShipBoard shipBoard) {
        controllerListener.notifyEvent(new CurrentPlayerUpdateEvent(ConversionUtils.convert(shipBoard)));
    }
}
