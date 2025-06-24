package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

/**
 * Event that represents a player releasing the forecast deck.
 * This event is dispatched when a player chooses to stop viewing the
 * cards in the forecast deck.
 *
 * @param playerName The name of the player releasing the forecast card
 * @param deckIndex The index of the forecast deck being released
 */
public record ReleaseForecastEvent(String playerName, int deckIndex) implements LobbyEvent {
    /**
     * Creates a ReleaseForecastEvent from a ship board and deck index.
     *
     * @param shipBoard The ship board associated with the player releasing the card
     * @param deckIndex The index of the forecast deck being released
     * @return A new ReleaseForecastEvent with the player's name extracted from the ship board
     */
    public static ReleaseForecastEvent from(ShipBoard shipBoard, int deckIndex) {
        return new ReleaseForecastEvent(
                Player.getPlayer(shipBoard).getNickname(),
                deckIndex
        );
    }

}
