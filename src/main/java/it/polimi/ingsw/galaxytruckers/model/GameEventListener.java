package it.polimi.ingsw.galaxytruckers.model;

import it.polimi.ingsw.galaxytruckers.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.StatType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.*;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.model.state.GameState;
import it.polimi.ingsw.galaxytruckers.serverController.dto.StateDTOConverter;
import it.polimi.ingsw.galaxytruckers.serverController.events.*;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.*;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;
import it.polimi.ingsw.galaxytruckers.view.Direction;

import java.awt.*;
import java.util.Map;
import java.util.Set;
import java.util.List;

public class GameEventListener {
    private EventListener<LobbyEvent> controllerListener;

    public void setControllerListener(EventListener<LobbyEvent> controllerListener) {
        this.controllerListener = controllerListener;
    }

    public void notifyActivateComponentEvent(ShipBoard shipBoard, Point point, boolean active) {
        controllerListener.notifyEvent(new ActivateComponentEvent(Player.getPlayer(shipBoard).getNickname(), point, active));
    }

    public void notifyFlightBoardUpdateEvent(ShipBoard shipBoard, int position) {
        controllerListener.notifyEvent(FlightBoardUpdateEvent.from(shipBoard, position));
    }

    public void notifyFlipHourglassEvent(ShipBoard shipBoard) {
        controllerListener.notifyEvent(new FlipHourglassEvent(Player.getPlayer(shipBoard).getNickname()));
    }

    public void notifyForecastDetailsEvent(ShipBoard shipBoard, List<AdventureCard> adventureCards) {
        controllerListener.notifyEvent(new ForecastDetailsEvent(
                Player.getPlayer(shipBoard).getNickname(),
                adventureCards.stream().map(AdventureCard::getId).toList()));
    }

    public void notifyGameEndEvent(Map<ShipBoard, Integer> finalScores) {
        controllerListener.notifyEvent(GameEndEvent.from(finalScores));
    }

    public void notifyGameStateUpdateEvent(GameState gameState) {
        System.out.println("Game state update: " + gameState.getClass().getSimpleName());
        controllerListener.notifyEvent(new GameStateUpdateEvent(
                StateDTOConverter.convert(gameState)
        ));
    }

    public void notifyGoodsUpdateEvent(ShipBoard shipBoard, Point point, GoodsType goodsType, boolean add) {
        controllerListener.notifyEvent(new GoodsUpdateEvent(
                Player.getPlayer(shipBoard).getNickname(),
                point,
                goodsType,
                add
        ));
    }

    public void notifyGrabStashedComponentEvent(ShipBoard shipBoard, int index) {
        controllerListener.notifyEvent(GrabStashedComponentEvent.from(shipBoard, index));
    }

    public void notifyHourglassEndEvent() {
        controllerListener.notifyEvent(new HourglassEndEvent());
    }

    public void notifyCabinInitializationEvent(ShipBoard shipBoard, Point point, CrewType crewType) {
        controllerListener.notifyEvent(new InitializeCabinEvent(Player.getPlayer(shipBoard).getNickname(),point, crewType));
    }

    public void notifyLoseCrewEvent(ShipBoard shipBoard, Point point) {
        controllerListener.notifyEvent(new LoseCrewEvent(
                Player.getPlayer(shipBoard).getNickname(),
                point
        ));
    }

    public void notifyNewCardEvent(AdventureCard adventureCard) {
        controllerListener.notifyEvent(NewCardEvent.from(adventureCard));
    }

    public void notifyPeekForecastEvent(ShipBoard shipBoard, int deckIndex) {
        controllerListener.notifyEvent(new PeekForecastEvent(
                Player.getPlayer(shipBoard).getNickname(),
                deckIndex));
    }

    public void notifyPlaceComponentEvent(ShipBoard shipBoard, Direction orientation, Point position) {
        controllerListener.notifyEvent(new PlaceComponentEvent(
                Player.getPlayer(shipBoard).getNickname(),
                position,
                orientation
        ));
    }

    public void notifyPlanetChoiceEvent(ShipBoard shipBoard, int planetId, ShipBoard nextShip) {
        controllerListener.notifyEvent(PlanetChoiceEvent.from(shipBoard,planetId, nextShip));
    }

    public void notifyRejectComponentEvent(ShipBoard shipBoard) {
        controllerListener.notifyEvent(new RejectComponentEvent(Player.getPlayer(shipBoard).getNickname()));
    }

    public void notifyReleaseForecastEvent(ShipBoard shipBoard, int deckIndex) {
        controllerListener.notifyEvent(new ReleaseForecastEvent(
                Player.getPlayer(shipBoard).getNickname(), deckIndex));
    }

    public void notifyRemoveComponentEvent(ShipBoard shipBoard, Point point) {
        controllerListener.notifyEvent(RemoveComponentEvent.from(shipBoard,point));
    }

    public void notifyRequestFaceDownComponentEvent(ShipBoard shipBoard, Component component) {
        controllerListener.notifyEvent(RequestFaceDownComponentEvent.from(shipBoard,component));
    }

    public void notifyRequestFaceUpComponentEvent(ShipBoard shipBoard, Component component) {
        controllerListener.notifyEvent(RequestFaceUpComponentEvent.from(shipBoard,component));
    }

    public void notifyShipNotConnectedEvent(ShipBoard shipBoard, List<Set<Point>> shipPieces) {
        controllerListener.notifyEvent(new ShipNotConnectedEvent(
                Player.getPlayer(shipBoard).getNickname(),
                shipPieces
        ));
    }

    public void notifyShipPieceRemovalEvent(ShipBoard shipBoard, int pieceIndex) {
        controllerListener.notifyEvent(new ShipPieceRemoveEvent(
                Player.getPlayer(shipBoard).getNickname(),
                pieceIndex
        ));
    }

    public void notifyShipStatUpdateEvent(ShipBoard shipBoard, StatType statType, int value) {
        controllerListener.notifyEvent(new ShipStatUpdateEvent(
                Player.getPlayer(shipBoard).getNickname(),
                statType,
                value
        ));
    }

    public void notifyStashComponentEvent(ShipBoard shipBoard) {
        controllerListener.notifyEvent(new StashComponentEvent(Player.getPlayer(shipBoard).getNickname()));
    }

    public void notifySurrenderEvent(List<ShipBoard> ships) {
        controllerListener.notifyEvent(new SurrenderEvent(
                ships.stream().map(s -> Player.getPlayer(s).getNickname()).toList()
        ));
    }

    public void notifyUseBatteryEvent(ShipBoard shipBoard, Point point) {
        controllerListener.notifyEvent(new UseBatteryEvent(Player.getPlayer(shipBoard).getNickname(), point));
    }

    public void notifyValidateShipEvent(ShipBoard shipBoard) {
        controllerListener.notifyEvent(ValidateShipEvent.from(shipBoard));
    }

    public void notifyCurrentPlayerUpdateEvent(ShipBoard shipBoard) {
        controllerListener.notifyEvent(new CurrentPlayerUpdateEvent(Player.getPlayer(shipBoard).getNickname()));
    }
}
