package it.polimi.ingsw.galaxytruckers.serverController.events;

/**
 * Game event signaling a specific state change in the whole application.
 * Includes methods to inspect what changed
 */
public sealed interface Event permits ActivateComponentEvent, FlightBoardUpdateEvent, FlipHourglassEvent,
                                      ForecastDetailsEvent, GameEndEvent, GameStateUpdateEvent, GoodsUpdateEvent,
                                      GrabStashedComponentEvent, HourglassEndEvent, InitializeCabinEvent,
                                      JoinLobbyEvent, LobbyDetailsEvent, LoseCrewEvent, NewCardEvent, PeekForecastEvent,
                                      PlaceComponentEvent, PlanetChoiceEvent, PlayerDisconnectionEvent, PlayerExitEvent,
                                      RejectComponentEvent, ReleaseForecastEvent, RemoveComponentEvent,
                                      RequestFaceDownComponentEvent, RequestFaceUpComponentEvent, ShipNotConnectedEvent,
                                      ShipPieceRemoveEvent, ShipStatUpdateEvent, StashComponentEvent, SurrenderEvent,
                                      UseBatteryEvent, ValidateShipEvent {
}
