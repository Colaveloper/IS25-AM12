package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.serverController.lobby.Lobby;

/**
 * Interface for all lobby-related events in the Galaxy Truckers game.
 * LobbyEvents represent state changes or actions specific to a game lobby context,
 * such as player actions, game state updates, and lobby management.
 * This sealed interface permits a specific set of event implementations that
 * represent various lobby interactions and state changes.
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
     * Executes an action associated with this event on the specified lobby.
     * By default, this method does nothing, but implementing events can override
     * it to perform lobby-specific actions when the event is processed.
     *
     * @param lobby the lobby on which to perform the action
     */
    default void runLobbyAction(Lobby lobby) {}
}
