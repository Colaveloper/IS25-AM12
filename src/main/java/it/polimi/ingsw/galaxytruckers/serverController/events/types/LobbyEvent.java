package it.polimi.ingsw.galaxytruckers.serverController.events.types;

public sealed interface LobbyEvent extends Event permits ActivateComponentEvent, FlightBoardUpdateEvent,
                                                         FlipHourglassEvent, ForecastDetailsEvent, GameEndEvent,
                                                         GameStateUpdateEvent, GoodsUpdateEvent,
                                                         GrabStashedComponentEvent, HourglassEndEvent,
                                                         InitializeCabinEvent, JoinLobbyEvent, LobbyDetailsEvent,
                                                         LoseCrewEvent, NewCardEvent, PeekForecastEvent,
                                                         PlaceComponentEvent, PlanetChoiceEvent,
                                                         PlayerDisconnectionEvent, PlayerExitEvent,
                                                         RejectComponentEvent, ReleaseForecastEvent,
                                                         RemoveComponentEvent, RequestFaceDownComponentEvent,
                                                         RequestFaceUpComponentEvent, ShipNotConnectedEvent,
                                                         ShipPieceRemoveEvent, ShipStatUpdateEvent, StashComponentEvent,
                                                         SurrenderEvent, UseBatteryEvent, ValidateShipEvent {
}
