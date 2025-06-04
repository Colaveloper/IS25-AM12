package it.polimi.ingsw.galaxytruckers.serverController.dto;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Lobby;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public record LobbyDTO(UUID id, Level level, int numPlayers, List<String> players, String host) implements Serializable {

    public static LobbyDTO from(Lobby lobby) {
        return new LobbyDTO(
                lobby.getId(),
                lobby.getLevel(),
                lobby.getNumPlayers(),
                lobby.getPlayers().stream().map(Player::getNickname).toList(),
                lobby.getHost().getNickname());
    }
}
