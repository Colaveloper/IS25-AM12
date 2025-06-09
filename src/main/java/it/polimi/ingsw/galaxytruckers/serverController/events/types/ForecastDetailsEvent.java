package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.util.List;
import java.util.Optional;

public record ForecastDetailsEvent(String playerName, List<Integer> forecastDeckIds) implements LobbyEvent {
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
