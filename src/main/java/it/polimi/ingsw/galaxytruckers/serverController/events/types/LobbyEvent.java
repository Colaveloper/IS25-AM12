package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.serverController.lobby.Lobby;

public sealed interface LobbyEvent extends Event permits ActivateComponentEvent, CurrentPlayerUpdateEvent,
                                                         FlightBoardUpdateEvent, FlipHourglassEvent,
                                                         ForecastDetailsEvent, GameEndEvent, GameSnapshotEvent,
                                                         GameStateUpdateEvent, GoodsUpdateEvent, GrabCreditsEvent,
                                                         GrabPlacedComponentEvent, GrabStashedComponentEvent,
                                                         HourglassEndEvent, InitializeCabinEvent, JoinLobbyEvent,
                                                         LobbyDetailsEvent, LoseCrewEvent, NewCardEvent,
                                                         PeekForecastEvent, PlaceComponentEvent, PlanetChoiceEvent,
                                                         PlayerDisconnectionEvent, PlayerExitEvent,
                                                         RejectComponentEvent, ReleaseForecastEvent,
                                                         RemoveComponentEvent, RequestFaceDownComponentEvent,
                                                         RequestFaceUpComponentEvent, ShipNotConnectedEvent,
                                                         ShipPieceRemoveEvent, StashComponentEvent, SurrenderEvent,
                                                         SurrenderRequestEvent, UseBatteryEvent, ValidateShipEvent {
    default void runLobbyAction(Lobby lobby) {}
}
