package it.polimi.ingsw.galaxytruckers.server.controller.events.types;

import it.polimi.ingsw.galaxytruckers.server.controller.lobby.Lobby;

import java.util.Map;

/**
 * Event signaling the end of the game, containing the final scores for each player.
 *
 * @param playerToScore a map from player names to their final scores
 */
public record GameEndEvent(Map<String, Integer> playerToScore) implements LobbyEvent {
    @Override
    public void runLobbyAction(Lobby lobby) {
        lobby.remove();
    }
}
