package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;

import java.util.Map;

public record LobbyDetailsEvent(String playerName, Map<String, GameColor> playerColors) implements Event{
}
