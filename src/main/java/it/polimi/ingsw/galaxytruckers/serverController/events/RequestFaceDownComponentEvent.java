package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ComponentBank;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

public record RequestFaceDownComponentEvent(String playerName, int componentId, int numFaceDown) implements Event {

    public static RequestFaceDownComponentEvent from(ShipBoard shipBoard, Component component, ComponentBank componentBank) {
        return new RequestFaceDownComponentEvent(
                Player.getPlayer(shipBoard).getNickname(),
                component.getId(),
                componentBank.getCoveredComponents().size()
        );
    }

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }
}
