package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Lobby;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.util.Map;
import java.util.stream.Collectors;

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
