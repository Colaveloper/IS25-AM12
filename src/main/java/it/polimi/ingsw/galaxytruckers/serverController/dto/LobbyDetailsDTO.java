package it.polimi.ingsw.galaxytruckers.serverController.dto;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

/**
 * DTO for lobby details.
 * Contains information about the lobby such as its ID, player colors, level, and number of players.
 *
 * @param lobbyId      the unique identifier of the lobby
 * @param playerColors a map of player names to their respective game colors
 * @param level        the level of the game
 * @param playersN     the number of players in the lobby
 */
public record LobbyDetailsDTO(UUID lobbyId, Map<String, GameColor> playerColors, Level level, int playersN) implements
                                                                                                            Serializable {
}
