package it.polimi.ingsw.galaxytruckers.serverController.dto;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * DTO for an active lobby in the Galaxy Truckers game.
 *
 * @param id         the unique identifier of the lobby
 * @param level      the level of the game
 * @param numPlayers the number of players allowed in the lobby
 * @param players    the list of player names in the lobby
 * @param host       the name of the host player who created the lobby
 */
public record ActiveLobbyDTO(UUID id, Level level, int numPlayers, List<String> players, String host) implements
                                                                                                      Serializable {
}
