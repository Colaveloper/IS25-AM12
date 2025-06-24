package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

/**
 * Event that represents a request to validate a player's ship configuration.
 * This event is dispatched when a player has finished building their ship and wants to verify
 * that it meets all the requirements for gameplay.
 *
 * @param playerName The name of the player whose ship needs validation
 */
public record ValidateShipEvent(String playerName) implements LobbyEvent {
    /**
     * Creates a ValidateShipEvent from a ship board.
     *
     * @param shipBoard The ship board to be validated
     * @return A new ValidateShipEvent with the player's name extracted from the ship board
     */
    public static ValidateShipEvent from(ShipBoard shipBoard) {
        return new ValidateShipEvent(
                Player.getPlayer(shipBoard).getNickname()
        );
    }

}
