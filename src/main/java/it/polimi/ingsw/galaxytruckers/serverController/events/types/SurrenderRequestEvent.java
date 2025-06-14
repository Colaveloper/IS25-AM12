package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.SurrenderCause;

public record SurrenderRequestEvent(String playerName, SurrenderCause cause) implements LobbyEvent{
}
