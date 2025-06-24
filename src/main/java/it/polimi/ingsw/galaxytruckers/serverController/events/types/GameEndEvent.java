package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Lobby;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Event that represents the end of a game.
 * This event is dispatched when a game has completed and contains the final scores for each player.
 *
 * @param playerToScore A map associating each player's name with their final score
 */
public record GameEndEvent(Map<String, Integer> playerToScore) implements LobbyEvent {
    /**
     * Creates a GameEndEvent from a map of ship boards to scores.
     *
     * @param shipToScore A map associating each player's ship board with their final score
     * @return A new GameEndEvent with player names extracted from the ship boards
     */
    public static GameEndEvent from(Map<ShipBoard, Integer> shipToScore) {
        return new GameEndEvent(
                shipToScore.entrySet().stream()
                        .collect(Collectors.toMap(
                                e -> Player.getPlayer(e.getKey()).getNickname(),
                                Map.Entry::getValue))
        );
    }

    /**
     * Executes the lobby action for this event.
     * Removes the lobby as the game has ended.
     *
     * @param lobby The lobby in which the game has ended
     */
    @Override
    public void runLobbyAction(Lobby lobby) {
        lobby.remove();
    }
}
