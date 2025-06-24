package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.serverController.lobby.Lobby;

/**
 * Represents an event that occurs within a game lobby.
 * Implementations of this interface signal changes or actions specific to a lobby.
 */
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
    /**
     * Executes an action on the given lobby when this event is processed.
     *
     * @param lobby the lobby on which to perform the action
     */
    default void runLobbyAction(Lobby lobby) {}
}
