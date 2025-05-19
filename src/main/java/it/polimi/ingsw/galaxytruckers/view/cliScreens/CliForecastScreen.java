package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.view.adventureClient.CliAdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CliForecastScreen extends CliScreen {

    private List<CliAdventureCard> forecastDeck;

    public CliForecastScreen(ClientModel model, ClientController controller) throws IOException {
        super(model, controller);

        forecastDeck = new ArrayList<>();
        for(Integer i : model.getForecastDeck()){
            CliAdventureCard card = new CliAdventureCard(model, i);
            card.addListener(this);
            forecastDeck.add(card);
        }
    }

    @Override
    public boolean isLegalInput(String input) {
        return true;
    }

    @Override
    public void parseAndInvoke(String input) {
        controller.releaseForecast();
    }

    @Override
    protected List<String> getNewDescription() throws IOException {
        List<String> description = new ArrayList<>();
        List<String> cardDescription = new ArrayList<>();

        description.add("This cards will appear in the upcoming adventure\n\n");
        for(CliAdventureCard adventureCard : forecastDeck){
            cardDescription = adventureCard.getDescription();
            cardDescription.set(0, adventureCard.getCardName());
            cardDescription.add("\n");
            description.addAll(cardDescription);
        }
        return description;
    }
}
