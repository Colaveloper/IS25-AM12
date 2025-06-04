package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.util.Map;
import java.util.stream.Collectors;

public record GameEndEvent(Map<String, Integer> playerToScore) implements LobbyEvent {
    public static GameEndEvent from(Map<ShipBoard, Integer> shipToScore) {
        return new GameEndEvent(
                shipToScore.entrySet().stream()
                        .collect(Collectors.toMap(
                                e -> Player.getPlayer(e.getKey()).getNickname(),
                                Map.Entry::getValue))
        );
    }

}
