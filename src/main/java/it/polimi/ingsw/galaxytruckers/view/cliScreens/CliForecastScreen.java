package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;
import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliAdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.state.SecondShipBuildingState;

import java.util.ArrayList;
import java.util.List;

public class CliForecastScreen extends CliScreen {
    private final List<AdventureCard> forecastDeck;

    public CliForecastScreen(ClientModel model, ClientController controller, GameState gameState) {
        super(model, controller, gameState);
        this.forecastDeck = gameState.getForecastDeck();
    }

    @Override
    public void parseAndInvoke(String input) {
        controller.releaseForecast();
    }

    @Override
    public void render() {
        System.out.println("Cards in the forecast deck:\n");
        for (AdventureCard card : forecastDeck){
            System.out.println(new CliAdventureCard(model).getDescription());
        }
    }
}
