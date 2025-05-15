package it.polimi.ingsw.galaxytruckers.view.screens;

import it.polimi.ingsw.galaxytruckers.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import it.polimi.ingsw.galaxytruckers.view.adventureClient.CliAdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ForecastScreen extends ScreenStrategy {

    private List<CliAdventureCard> forecastDeck;

    public ForecastScreen(ClientModel model) throws IOException {
        super(model);

        forecastDeck = new ArrayList<>();
        for(Integer i : model.getForecastDeck()){
            CliAdventureCard card = new CliAdventureCard(model, i);
            card.addListener(this);
            forecastDeck.add(card);
        }
    }

    @Override
    public boolean isLegalInput(ClientModel model, String input) {
        return true;
    }

    @Override
    public void parseAndInvoke(ClientModel model, String input, VirtualServer server) throws IOException {
        //todo
    }

    @Override
    public void showGUI(ClientModel model, Pane root, VirtualServer server) throws IOException {

    }

    @Override
    protected List<String> getNewDescription() throws IOException {
        List<String> description = new ArrayList<>();

        description.add("This cards will appear in the upcoming adventure\n\n");
        for(CliAdventureCard adventureCard : forecastDeck){
            description.addAll(adventureCard.getDescription());
        }
        return description;
    }
}
