package it.polimi.ingsw.galaxytruckers.serverController.events;

public interface EventVisitor {

    void visit(LobbyEvent lobbyEvent);

    void visit(StartBuildingEvent startBuildingEvent);

    void visit(FlipHourglassEvent flipHourglassEvent);

    void visit(RequestFaceUpComponentEvent requestFaceUpComponentEvent);

    void visit(RequestFaceDownComponentEvent requestFaceDownComponentEvent);

    void visit(RejectComponentEvent rejectComponentEvent);

    void visit(ShipMapUpdateEvent shipMapUpdateEvent);

    void visit(PeekForecastEvent peekForecastEvent);

    void visit(ReleaseForecastEvent releaseForecastEvent);

    void visit(GrabStashedComponentEvent grabStashedComponentEvent);

    void visit(StashComponentEvent stashComponentEvent);

    void visit(PlaceShipOnFlightBoardEvent placeShipOnFlightBoardEvent);

    void visit(NewCardEvent newCardEvent);

    void visit(SurrenderEvent surrenderEvent);

    void visit(CargoHoldUpdate cargoHoldUpdate);

    void visit(BatteryUpdate batteryUpdate);

    void visit(CabinUpdate cabinUpdate);

    void visit(ShipStatUpdateEvent shipStatUpdateEvent);
}
