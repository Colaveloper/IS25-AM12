package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;

import java.util.Map;
import java.util.UUID;

public record LobbyDetailsEvent(String playerName, UUID lobbyId, Map<String, GameColor> playerColors, Level level, int playersN) implements
                                                                                                                                 LobbyEvent {
}
