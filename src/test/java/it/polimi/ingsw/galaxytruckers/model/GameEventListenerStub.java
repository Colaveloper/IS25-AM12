package it.polimi.ingsw.galaxytruckers.model;

import it.polimi.ingsw.galaxytruckers.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.projectiles.Projectile;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.StatType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.*;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.serverController.events.EventListener;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GameEventListenerStub extends GameEventListener{
    @Override
    public void notifySurrenderEvent(List<ShipBoard> ships) {
    }

    @Override
    public void notifyStashComponentEvent(ShipBoard shipBoard) {
    }

    @Override
    public void notifyStartBuildingEvent() {
    }

    @Override
    public void notifyShipStatUpdateEvent(ShipBoard shipBoard, StatType statType, int value) {
    }

    @Override
    public void notifyShipPieceRemoveEvent(ShipBoard shipBoard, List<Point> positions) {
    }

    @Override
    public void notifyShipNotConnectedEvent(ShipBoard shipBoard, List<Set<Point>> shipPieces) {
    }

    @Override
    public void notifySelectionPointEvent(ShipBoard shipBoard, List<Point> points, List<Point> batteries) {
    }

    @Override
    public void notifyRequestFaceUpComponentEvent(ShipBoard shipBoard, Component component) {
    }

    @Override
    public void notifyRequestFaceDownComponentEvent(ShipBoard shipBoard, Component component) {
    }

    @Override
    public void notifyRemoveComponentEvent(ShipBoard shipBoard, Point point) {
    }

    @Override
    public void notifyReleaseForecastEvent(ShipBoard shipBoard, int deckIndex) {
    }

    @Override
    public void notifyRejectComponentEvent(ShipBoard shipBoard, Component component) {
    }

    @Override
    public void notifyProjectileEvent(ShipBoard shipBoard, Projectile projectile) {
    }

    @Override
    public void notifyPlanetChoiceEvent(ShipBoard shipBoard, int planetId) {
    }

    @Override
    public void notifyPlaceComponentEvent(ShipBoard shipBoard, Component component, Point position) {
    }

    @Override
    public void notifyPeekForecastEvent(ShipBoard shipBoard, int deckIndex, List<AdventureCard> forecastDeck) {
    }

    @Override
    public void notifyNewCardEvent(AdventureCard adventureCard) {
    }

    @Override
    public void notifyInvalidShipsUpdateEvent(Set<ShipBoard> invalidShips) {
    }

    @Override
    public void notifyHourglassEndEvent() {
    }

    @Override
    public void notifyGrabStashedComponentEvent(ShipBoard shipBoard, Component component) {
    }

    @Override
    public void notifyGoodsBufferUpdateEvent(ShipBoard shipBoard, boolean adding, GoodsType goodsType) {
    }

    @Override
    public void notifyGameEndEvent(Map<ShipBoard, Integer> finalScores) {
    }

    @Override
    public void notifyFlipHourglassEvent(ShipBoard shipBoard, boolean isLast) {
    }

    @Override
    public void notifyFlightBoardUpdateEvent(ShipBoard shipBoard, int position) {
    }

    @Override
    public void notifyCargoHoldUpdateEvent(ShipBoard shipBoard, Point point, CargoHold cargoHold) {
    }

    @Override
    public void notifyCabinUpdateEvent(ShipBoard shipBoard, Point point, Cabin cabin) {
    }

    @Override
    public void notifyBatteryUpdateEvent(ShipBoard shipBoard, Point point, Battery battery) {
    }

    @Override
    public void setControllerListener(EventListener controllerListener) {
    }
}
