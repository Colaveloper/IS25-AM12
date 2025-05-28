package it.polimi.ingsw.galaxytruckers.view.controller;

import it.polimi.ingsw.galaxytruckers.view.model.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerRegistry {
    private final Map<String, Player> players = new HashMap<>();

    public Player getByNickname(String nickname) {
        synchronized (players) {
            return players.get(nickname);
        }
    }

    public Player addPlayer(String nickname) {
        synchronized (players) {
            if (players.containsKey(nickname)) {
                return players.get(nickname);
            }
            Player player = new Player(nickname);
            players.put(nickname, player);
            return player;
        }
    }

    public void removePlayer(Player player) {
        synchronized (players) {
            players.remove(player.getNickname());
        }
    }

    public void clear() {
        synchronized (players) {
            players.clear();
        }
    }

}
