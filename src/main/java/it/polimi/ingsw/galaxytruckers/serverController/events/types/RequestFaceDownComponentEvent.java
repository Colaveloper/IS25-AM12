package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

/**
 * Event that represents a request for a face-down component.
 * This event is dispatched when a player wants to retrieve or interact with a component
 * that is currently face-down in the component bank.
 *
 * @param playerName The name of the player requesting the component
 * @param componentId The unique identifier of the requested component
 */
public record RequestFaceDownComponentEvent(String playerName, int componentId) implements LobbyEvent {

    /**
     * Creates a RequestFaceDownComponentEvent from a ship board and component.
     *
     * @param shipBoard The ship board associated with the player making the request
     * @param component The component being requested
     * @return A new RequestFaceDownComponentEvent with the player's name extracted from the ship board
     */
    public static RequestFaceDownComponentEvent from(ShipBoard shipBoard, Component component) {
        return new RequestFaceDownComponentEvent(
                Player.getPlayer(shipBoard).getNickname(),
                component.getId()
        );
    }

}
