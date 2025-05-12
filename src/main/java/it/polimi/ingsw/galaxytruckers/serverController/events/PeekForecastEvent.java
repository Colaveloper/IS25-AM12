package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.util.ArrayList;
import java.util.List;

public record PeekForecastEvent(String playerName, int deckIndex, List<Integer> forecastDeckIds) implements Event{
    public static PeekForecastEvent from(ShipBoard shipBoard, int deckIndex, List<AdventureCard> forecastDeck) {
        return new PeekForecastEvent(
                Player.getPlayer(shipBoard).getNickname(),
                deckIndex,
                new ArrayList<>()
                //TODO: add ids to adventure cards
                // forecastDeck.stream().map(AdventureCard::getId).toList()
        );
    }

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }
}
