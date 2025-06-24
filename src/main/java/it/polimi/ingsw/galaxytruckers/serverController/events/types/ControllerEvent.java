package it.polimi.ingsw.galaxytruckers.serverController.events.types;

/**
 * Interface for all controller-related events in the Galaxy Truckers game.
 * ControllerEvents represent state changes related to lobby discovery and management.
 * This sealed interface permits only specific event implementations that handle
 * activities like adding/removing lobbies and updating lobby lists.
 */
public sealed interface ControllerEvent extends Event permits AddActiveLobbyEvent, RemoveActiveLobbyEvent,
                                                              SetActiveLobbiesEvent {
}
