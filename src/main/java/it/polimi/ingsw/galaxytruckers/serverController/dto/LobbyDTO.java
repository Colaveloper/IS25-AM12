package it.polimi.ingsw.galaxytruckers.serverController.dto;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public record LobbyDTO(UUID id, Level level, int numPlayers, List<String> players, String host) implements Serializable {
}
