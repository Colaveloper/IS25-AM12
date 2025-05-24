package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.util.List;

public record ForecastDetailsEvent(String playerName, List<Integer> forecastDeckIds) implements Event{
    public static ForecastDetailsEvent from(ShipBoard shipBoard, List<AdventureCard> forecastDeck) {
        return new ForecastDetailsEvent(
                Player.getPlayer(shipBoard).getNickname(),
                forecastDeck.stream().map(AdventureCard::getId).toList()
        );
    }

}
