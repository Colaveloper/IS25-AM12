package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.util.List;
import java.util.Set;

public record InvalidShipsUpdateEvent(List<String> invalidPlayers) implements Event{
    public static InvalidShipsUpdateEvent from(Set<ShipBoard> invalidShips) {
        return new InvalidShipsUpdateEvent(
                invalidShips.stream()
                        .map(s -> Player.getPlayer(s).getNickname())
                        .toList()
        );
    }

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }
}
