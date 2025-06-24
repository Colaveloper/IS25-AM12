package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

/**
 * Event that represents a component being rejected by the player during ship building.
 *
 * @param playerName The name of the player whose component was rejected
 */
public record RejectComponentEvent(String playerName) implements LobbyEvent {

    /**
     * Creates a RejectComponentEvent from a ship board and the rejected component.
     *
     * @param shipBoard The ship board on which the component placement was attempted
     * @param component The component that was rejected
     * @return A new RejectComponentEvent with the player's name extracted from the ship board
     */
    public static RejectComponentEvent from(ShipBoard shipBoard, Component component) {
        return new RejectComponentEvent(
                Player.getPlayer(shipBoard).getNickname()
        );
    }

}
