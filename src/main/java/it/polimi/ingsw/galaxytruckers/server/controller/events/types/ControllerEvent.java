package it.polimi.ingsw.galaxytruckers.server.controller.events.types;

/**
 * Represents an event that affects the controller or the overall application state,
 * rather than a specific lobby.
 */
public sealed interface ControllerEvent extends Event permits AddActiveLobbyEvent, RemoveActiveLobbyEvent,
                                                              SetActiveLobbiesEvent {
}
