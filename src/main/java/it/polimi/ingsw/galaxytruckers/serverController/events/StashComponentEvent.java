package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.util.List;

public record StashComponentEvent(String playerName, List<Integer> stashedComponentIds) implements Event {
    public static StashComponentEvent from(ShipBoard shipBoard) {
        return new StashComponentEvent(
                Player.getPlayer(shipBoard).getNickname(),
                shipBoard.getStashedComponents().stream().map(Component::getId).toList()
        );
    }

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }
}
