package it.polimi.ingsw.galaxytruckers.serverController.events;

public interface EventVisitor {

    void visit(LobbyEvent lobbyEvent);

    void visit(StartBuildingEvent startBuildingEvent);

    void visit(FlipHourglassEvent flipHourglassEvent);

    void visit(RequestFaceUpComponentEvent requestFaceUpComponentEvent);

    void visit(RequestFaceDownComponentEvent requestFaceDownComponentEvent);

    void visit(RejectComponentEvent rejectComponentEvent);

    void visit(PlaceComponentEvent placeComponentEvent);

    void visit(PeekForecastEvent peekForecastEvent);

    void visit(ReleaseForecastEvent releaseForecastEvent);

    void visit(GrabStashedComponentEvent grabStashedComponentEvent);

    void visit(StashComponentEvent stashComponentEvent);

    void visit(FlightBoardUpdateEvent flightBoardUpdateEvent);

    void visit(NewCardEvent newCardEvent);

    void visit(SurrenderEvent surrenderEvent);

    void visit(CargoHoldUpdateEvent cargoHoldUpdateEvent);

    void visit(BatteryUpdateEvent batteryUpdateEvent);

    void visit(CabinUpdateEvent cabinUpdateEvent);

    void visit(ShipStatUpdateEvent shipStatUpdateEvent);

    void visit(HourglassEndEvent hourglassEndEvent);

    void visit(ShipPieceRemoveEvent shipPieceRemoveEvent);

    void visit(SelectionPointsEvent selectionPointsEvent);

    void visit(PlanetChoiceEvent planetChoiceEvent);

    void visit(GoodsBufferUpdateEvent goodsBufferUpdateEvent);

    void visit(ProjectileEvent projectileEvent);

    void visit(GameEndEvent gameEndEvent);

    void visit(InvalidShipsUpdateEvent invalidShipsUpdateEvent);

    void visit(ShipNotConnectedEvent shipNotConnectedEvent);
}
