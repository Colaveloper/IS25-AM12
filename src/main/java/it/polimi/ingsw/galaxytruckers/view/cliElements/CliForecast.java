package it.polimi.ingsw.galaxytruckers.view.cliElements;

import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.AbandonedShipCard;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;

import java.io.IOException;
import java.util.List;

public class CliForecast extends CliElement {
    private final boolean[] blockedForecasts;
    private final List<AdventureCard> forecastDeck;

    public CliForecast(ClientModel model, boolean[] blockedForecasts, List<AdventureCard> forecastDeck) {
        super(model);
        this.blockedForecasts = blockedForecasts;
        this.forecastDeck = forecastDeck;
    }

    @Override
    protected List<String> getNewDescription() throws IOException {
        //Visualizza array di booleani per i deck bloccati
        for (AdventureCard adventureCard : forecastDeck) {
            switch (adventureCard) {
                case AbandonedShipCard e -> {
                    System.out.println(e.getCardLevel());
                    System.out.println(e.getCreditPrize());
                    //...
                }
            }
        }
        return List.of();
    }
}
