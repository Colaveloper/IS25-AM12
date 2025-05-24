package it.polimi.ingsw.galaxytruckers.model;

import it.polimi.ingsw.galaxytruckers.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.projectiles.Projectile;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.StatType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Battery;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Cabin;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CargoHold;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.events.*;

import java.awt.*;
import java.util.Map;
import java.util.Set;
import java.util.List;

public class GameEventListener {
    private EventListener controllerListener;

    public void setControllerListener(EventListener controllerListener) {
        this.controllerListener = controllerListener;
    }

    public void notifyBatteryUpdateEvent(ShipBoard shipBoard, Point point, Battery battery) {
        controllerListener.notifyEvent(UseBatteryEvent.from(shipBoard, point, battery));
    }

    public void notifyCabinUpdateEvent(ShipBoard shipBoard, Point point, Cabin cabin) {
        controllerListener.notifyEvent(InitializeCabinEvent.from(shipBoard, point, cabin));
    }

    public void notifyCargoHoldUpdateEvent(ShipBoard shipBoard, Point point, CargoHold cargoHold) {
        controllerListener.notifyEvent(CargoHoldUpdateEvent.from(shipBoard,point,cargoHold));
    }

    public void notifyFlightBoardUpdateEvent(ShipBoard shipBoard, int position) {
        controllerListener.notifyEvent(FlightBoardUpdateEvent.from(shipBoard,position));
    }

    public void notifyFlipHourglassEvent(ShipBoard shipBoard, boolean isLast) {
        controllerListener.notifyEvent(FlipHourglassEvent.from(shipBoard,isLast));
    }

    public void notifyGameEndEvent(Map<ShipBoard, Integer> finalScores) {
        controllerListener.notifyEvent(GameEndEvent.from(finalScores));
    }

    public void notifyGoodsBufferUpdateEvent(ShipBoard shipBoard, boolean adding, GoodsType goodsType) {
        controllerListener.notifyEvent(GoodsBufferUpdateEvent.from(shipBoard,adding,goodsType));
    }

    public void notifyGrabStashedComponentEvent(ShipBoard shipBoard, Component component) {
        controllerListener.notifyEvent(GrabStashedComponentEvent.from(shipBoard,component));
    }

    public void notifyHourglassEndEvent() {
        controllerListener.notifyEvent(new HourglassEndEvent());
    }

    public void notifyInvalidShipsUpdateEvent(Set<ShipBoard> invalidShips) {
        controllerListener.notifyEvent(ValidateShipEvent.from(invalidShips));
    }

    public void notifyNewCardEvent(AdventureCard adventureCard) {
        controllerListener.notifyEvent(NewCardEvent.from(adventureCard));
    }

    public void notifyPeekForecastEvent(ShipBoard shipBoard, int deckIndex, java.util.List<AdventureCard> forecastDeck) {
        controllerListener.notifyEvent(ForecastDetailsEvent.from(shipBoard, deckIndex, forecastDeck));
    }

    public void notifyPlaceComponentEvent(ShipBoard shipBoard, Component component, Point position) {
        controllerListener.notifyEvent(PlaceComponentEvent.from(shipBoard,component,position));
    }

    public void notifyPlanetChoiceEvent(ShipBoard shipBoard, int planetId) {
        controllerListener.notifyEvent(PlanetChoiceEvent.from(shipBoard,planetId));
    }

    public void notifyProjectileEvent(ShipBoard shipBoard, Projectile projectile) {
        controllerListener.notifyEvent(ProjectileEvent.from(shipBoard,projectile));
    }

    public void notifyRejectComponentEvent(ShipBoard shipBoard, Component component) {
        controllerListener.notifyEvent(RejectComponentEvent.from(shipBoard,component));
    }

    public void notifyReleaseForecastEvent(ShipBoard shipBoard, int deckIndex) {
        controllerListener.notifyEvent(ReleaseForecastEvent.from(shipBoard,deckIndex));
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

    public void notifySelectionPointEvent(ShipBoard shipBoard, List<Point> points, List<Point> batteries) {
        controllerListener.notifyEvent(SelectionPointsEvent.from(shipBoard, points, batteries));
    }

    public void notifyShipNotConnectedEvent(ShipBoard shipBoard, List<Set<Point>> shipPieces) {
        controllerListener.notifyEvent(ShipNotConnectedEvent.from(shipBoard,shipPieces));
    }

    public void notifyShipPieceRemoveEvent(ShipBoard shipBoard, List<Point> positions) {
        controllerListener.notifyEvent(ShipPieceRemoveEvent.from(shipBoard,positions));
    }

    public void notifyShipStatUpdateEvent(ShipBoard shipBoard, StatType statType, int value) {
        controllerListener.notifyEvent(ShipStatUpdateEvent.from(shipBoard,statType,value));
    }

    public void notifyStartBuildingEvent() {
        controllerListener.notifyEvent(new StartBuildingEvent());
    }

    public void notifyStashComponentEvent(ShipBoard shipBoard) {
        controllerListener.notifyEvent(StashComponentEvent.from(shipBoard));
    }

    public void notifySurrenderEvent(List<ShipBoard> ships) {
        controllerListener.notifyEvent(SurrenderEvent.from(ships));
    }

    public void notifyActivateComponentEvent(ShipBoard shipBoard, Point point) {
        controllerListener.notifyEvent(ActivateComponentEvent.from(shipBoard, point));
    }
}
