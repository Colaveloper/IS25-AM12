package it.polimi.ingsw.galaxytruckers.view.model;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Lobby {
    private final UUID id;
    private final int playersN;
    private final Level level;
    private final List<String> players;
    private final String host;

    public Lobby(UUID id, int playersN, Level level, List<String> players, String host) {
        this.host = host;
        this.id = id;
        this.playersN = playersN;
        this.level = level;
        this.players = players;
    }

    public Lobby(UUID id, int playersN, Level level, String host) {
        this.host = host;
        this.id = id;
        this.playersN = playersN;
        this.level = level;
        this.players = new ArrayList<>();
    }

    public int getPlayersN() {
        return playersN;
    }

    public Level getLevel() {
        return level;
    }

    public UUID getId() {
        return id;
    }

    public List<String> getPlayers() {
        return players;
    }

    public String getHost() {
        return host;
    }
}
