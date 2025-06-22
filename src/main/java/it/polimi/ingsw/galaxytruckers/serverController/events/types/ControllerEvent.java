package it.polimi.ingsw.galaxytruckers.serverController.events.types;

public sealed interface ControllerEvent extends Event permits AddActiveLobbyEvent, RemoveActiveLobbyEvent,
                                                              SetActiveLobbiesEvent {
}
