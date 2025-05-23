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

    public Lobby(UUID id, int playersN, Level level, List<String> players) {
        this.id = id;
        this.playersN = playersN;
        this.level = level;
        this.players = players;
    }

    public Lobby(UUID id, int playersN, Level level) {
        this.id = id;
        this.playersN = playersN;
        this.level = level;
        this.players = new ArrayList<>();
    }
}
