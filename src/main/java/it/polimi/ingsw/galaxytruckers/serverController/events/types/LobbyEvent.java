package it.polimi.ingsw.galaxytruckers.serverController.events.types;

public sealed interface LobbyEvent extends Event permits ActivateComponentEvent, CurrentPlayerUpdateEvent,
                                                         FlightBoardUpdateEvent, FlipHourglassEvent,
                                                         ForecastDetailsEvent, GameEndEvent, GameStateUpdateEvent,
                                                         GoodsUpdateEvent, GrabCreditsEvent, GrabStashedComponentEvent,
                                                         HourglassEndEvent, InitializeCabinEvent, JoinLobbyEvent,
                                                         LobbyDetailsEvent, LoseCrewEvent, NewCardEvent,
                                                         PeekForecastEvent, PlaceComponentEvent, PlanetChoiceEvent,
                                                         PlayerDisconnectionEvent, PlayerExitEvent,
                                                         RejectComponentEvent, ReleaseForecastEvent,
                                                         RemoveComponentEvent, RequestFaceDownComponentEvent,
                                                         RequestFaceUpComponentEvent, ShipNotConnectedEvent,
                                                         ShipPieceRemoveEvent, StashComponentEvent, SurrenderEvent,
                                                         SurrenderRequestEvent, UseBatteryEvent, ValidateShipEvent {
}
