package it.polimi.ingsw.galaxytruckers.serverController.dto;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

public record LobbyDetailsDTO(UUID lobbyId, Map<String, GameColor> playerColors, Level level, int playersN) implements
                                                                                                            Serializable {
}
