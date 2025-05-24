package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.FourColors;

import java.util.Map;

public record LobbyDetailsEvent(String playerName, Map<String, FourColors> playerColors) implements Event{
}
