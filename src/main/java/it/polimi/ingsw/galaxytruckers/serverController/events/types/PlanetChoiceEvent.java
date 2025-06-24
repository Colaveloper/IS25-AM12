package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

/**
 * Event that represents a player making a planet choice during gameplay.
 * This event is dispatched when a player selects a planet to land on,
 * and also indicates which player will take the next action after this choice.
 *
 * @param playerName The name of the player who made the planet choice
 * @param planetId The identifier of the selected planet
 * @param nextPlayerName The name of the player who will act next
 */
public record PlanetChoiceEvent(String playerName, int planetId, String nextPlayerName) implements LobbyEvent {
    /**
     * Creates a PlanetChoiceEvent from the current player's ship board, planet ID, and next player's ship board.
     *
     * @param shipBoard The ship board of the player making the planet choice
     * @param planetId The identifier of the selected planet
     * @param nextShipBoard The ship board of the player who will act next
     * @return A new PlanetChoiceEvent with player names extracted from the respective ship boards
     */
    public static PlanetChoiceEvent from(ShipBoard shipBoard, int planetId, ShipBoard nextShipBoard) {
        return new PlanetChoiceEvent(
                Player.getPlayer(shipBoard).getNickname(),
                planetId,
                Player.getPlayer(nextShipBoard).getNickname()
        );
    }
}
