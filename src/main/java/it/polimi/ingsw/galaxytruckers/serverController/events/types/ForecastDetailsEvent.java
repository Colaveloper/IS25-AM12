package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.util.List;
import java.util.Optional;

/**
 * Event that provides information about upcoming adventure cards in the forecast deck.
 * This event is dispatched when a player uses an ability to peek at future cards,
 * allowing them to strategize based on upcoming encounters.
 *
 * @param playerName The name of the player who is viewing the forecast
 * @param forecastDeckIds The list of IDs of the adventure cards in the forecast deck
 */
public record ForecastDetailsEvent(String playerName, List<Integer> forecastDeckIds) implements LobbyEvent {
    /**
     * Creates a ForecastDetailsEvent from a ship board and the forecast deck.
     *
     * @param shipBoard The ship board associated with the player viewing the forecast
     * @param forecastDeck The list of adventure cards in the forecast deck
     * @return A new ForecastDetailsEvent with the player's name extracted from the ship board
     */
    public static ForecastDetailsEvent from(ShipBoard shipBoard, List<AdventureCard> forecastDeck) {
        return new ForecastDetailsEvent(
                Player.getPlayer(shipBoard).getNickname(),
                forecastDeck.stream().map(AdventureCard::getId).toList()
        );
    }

    @Override
    public Optional<String> getReceiverName() {
        return Optional.of(playerName);
    }
}
