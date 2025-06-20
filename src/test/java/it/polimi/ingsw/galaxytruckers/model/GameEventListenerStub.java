package it.polimi.ingsw.galaxytruckers.model;

import it.polimi.ingsw.galaxytruckers.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.SurrenderCause;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.*;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.model.state.GameState;
import it.polimi.ingsw.galaxytruckers.serverController.events.EventListener;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.LobbyEvent;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GameEventListenerStub extends GameEventListener{

    public GameEventListenerStub() {
        super(null);
    }

    @Override
    public void notifyLoseCrewEvent(ShipBoard shipBoard, Point point) {
    }

    @Override
    public void notifyRejectComponentEvent(ShipBoard shipBoard) {
    }

    @Override
    public void notifyReleaseForecastEvent(ShipBoard shipBoard, int deckIndex) {
    }

    @Override
    public void notifyRemoveComponentEvent(ShipBoard shipBoard, Point point) {
    }

    @Override
    public void notifyRequestFaceDownComponentEvent(ShipBoard shipBoard, Component component) {
    }

    @Override
    public void notifyRequestFaceUpComponentEvent(ShipBoard shipBoard, Component component) {
    }

    @Override
    public void notifyShipNotConnectedEvent(ShipBoard shipBoard, List<Set<Point>> shipPieces) {
    }

    @Override
    public void notifyShipPieceRemovalEvent(ShipBoard shipBoard, int pieceIndex) {
    }

    @Override
    public void notifyGrabCreditsEvent(ShipBoard shipBoard, int value) {
    }

    @Override
    public void notifyStashComponentEvent(ShipBoard shipBoard) {
    }

    @Override
    public void notifySurrenderEvent(List<ShipBoard> ships) {
    }

    @Override
    public void notifyValidateShipEvent(ShipBoard shipBoard) {
    }

    @Override
    public void notifyUseBatteryEvent(ShipBoard shipBoard, Point point) {
    }

    @Override
    public void notifyPlanetChoiceEvent(ShipBoard shipBoard, int planetId, ShipBoard nextShip) {
    }

    @Override
    public void notifyPlaceComponentEvent(ShipBoard shipBoard, Direction orientation, Point position) {
    }

    @Override
    public void notifyPeekForecastEvent(ShipBoard shipBoard, int deckIndex) {
    }

    @Override
    public void notifyNewCardEvent(AdventureCard adventureCard) {
    }

    @Override
    public void notifyCabinInitializationEvent(ShipBoard shipBoard, Point point, CrewType crewType) {
    }

    @Override
    public void notifyHourglassEndEvent() {
    }

    @Override
    public void notifyGrabStashedComponentEvent(ShipBoard shipBoard, int index) {
    }

    @Override
    public void notifyGoodsUpdateEvent(ShipBoard shipBoard, Point point, GoodsType goodsType, boolean add) {
    }

    @Override
    public void notifyGameStateUpdateEvent(GameState gameState) {
    }

    @Override
    public void notifyGameEndEvent(Map<ShipBoard, Integer> finalScores) {
    }

    @Override
    public void notifyForecastDetailsEvent(ShipBoard shipBoard, List<AdventureCard> adventureCards) {
    }

    @Override
    public void notifyFlipHourglassEvent(ShipBoard shipBoard) {
    }

    @Override
    public void notifyFlightBoardUpdateEvent(ShipBoard shipBoard, int position) {
    }

    @Override
    public void notifyActivateComponentEvent(ShipBoard shipBoard, Point point, boolean active) {
    }

    @Override
    public void notifyCurrentPlayerUpdateEvent(ShipBoard shipBoard) {
    }

    @Override
    public void notifySurrenderRequestEvent(ShipBoard shipBoard, SurrenderCause cause) {
    }

    @Override
    public void notifyGrabPlacedComponentEvent(ShipBoard shipBoard) {
    }
}
