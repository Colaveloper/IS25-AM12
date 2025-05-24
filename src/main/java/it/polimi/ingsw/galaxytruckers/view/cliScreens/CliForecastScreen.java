package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.view.adventureClient.CliAdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CliForecastScreen extends CliScreen {

    private List<CliAdventureCard> forecastDeck;

    public CliForecastScreen(ClientModel model, ClientController controller, GameState gameState) {
        super(model, controller, gameState);

        forecastDeck = new ArrayList<>();
        for(Integer i : model.getForecastDeck()){
            CliAdventureCard card = new CliAdventureCard(model, i);
            forecastDeck.add(card);
        }
    }

    @Override
    public void parseAndInvoke(String input) {
        controller.releaseForecast();
    }

    @Override
    public void render() {
        List<String> description = new ArrayList<>();
        List<String> cardDescription = new ArrayList<>();

        description.add("This cards will appear in the upcoming adventure\n\n");
        for(CliAdventureCard adventureCard : forecastDeck){
            cardDescription = adventureCard.getNewDescription();
            cardDescription.set(0, adventureCard.getCardName());
            cardDescription.add("\n");
            description.addAll(cardDescription);
        }
    }
}
