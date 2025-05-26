package it.polimi.ingsw.galaxytruckers.view.controller;

import it.polimi.ingsw.galaxytruckers.view.model.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerRegistry {
    private final Map<String, Player> players = new HashMap<>();

    public Player getByNickname(String nickname) {
        return players.get(nickname);
    }

    public Player addPlayer(String nickname) {
        Player player = new Player(nickname);
        players.put(nickname, player);
        return player;
    }

}
