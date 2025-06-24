package it.polimi.ingsw.galaxytruckers.view.model;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a game lobby in the Galaxy Truckers game.
 * A lobby is a waiting room where players gather before starting a game.
 * It contains information about the game configuration, such as the flight level,
 * maximum number of players, and the current list of players.
 */
public class Lobby {
    /** Unique identifier for this lobby */
    private final UUID id;

    /** Maximum number of players allowed in this lobby */
    private final int playersN;

    /** Flight level for the game that will be started from this lobby */
    private final Level level;

    /** List of player usernames currently in the lobby */
    private final List<String> players;

    /** Username of the player who created and hosts this lobby */
    private final String host;

    /**
     * Creates a new lobby with the specified parameters and an existing list of players.
     *
     * @param id The unique identifier for this lobby
     * @param playersN The maximum number of players allowed
     * @param level The flight level for the game
     * @param players The initial list of players in the lobby
     * @param host The username of the lobby host
     */
    public Lobby(UUID id, int playersN, Level level, List<String> players, String host) {
        this.host = host;
        this.id = id;
        this.playersN = playersN;
        this.level = level;
        this.players = players;
    }

    /**
     * Creates a new empty lobby with the specified parameters.
     * Initializes an empty list of players.
     *
     * @param id The unique identifier for this lobby
     * @param playersN The maximum number of players allowed
     * @param level The flight level for the game
     * @param host The username of the lobby host
     */
    public Lobby(UUID id, int playersN, Level level, String host) {
        this.host = host;
        this.id = id;
        this.playersN = playersN;
        this.level = level;
        this.players = new ArrayList<>();
    }

    /**
     * Gets the maximum number of players allowed in this lobby.
     *
     * @return The maximum number of players
     */
    public int getPlayersN() {
        return playersN;
    }

    /**
     * Gets the flight level for the game.
     *
     * @return The game flight level
     */
    public Level getLevel() {
        return level;
    }

    /**
     * Gets the unique identifier for this lobby.
     *
     * @return The lobby's UUID
     */
    public UUID getId() {
        return id;
    }

    /**
     * Gets the list of player usernames currently in the lobby.
     *
     * @return The list of players
     */
    public List<String> getPlayers() {
        return players;
    }

    /**
     * Gets the username of the player who created and hosts this lobby.
     *
     * @return The host's username
     */
    public String getHost() {
        return host;
    }
}
