package it.polimi.ingsw.galaxytruckers.view.cliElements;

import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.AdventureCard;

import java.util.ArrayList;
import java.util.List;

public class CliForecastCards extends CliElement {
    private List<AdventureCard> forecastDeck;

    @Override
    protected List<String> getNewDescription() {
        List<String> newDescription = new ArrayList<>();
        newDescription.add("Cards in the forecast deck:\n");
        for (AdventureCard card : forecastDeck) {
            newDescription.addAll(new CliAdventureCard(card).getDescription());
        }
        return newDescription;
    }

    public void setCards(List<AdventureCard> cards) {
        forecastDeck = cards;
        setDirty();
    }
//    private final List<AdventureCard> forecastDeck;
//    private SecondShipBuildingState gameState;
//
//    public CliForecastScreen(ClientModel model, ClientController controller, SecondShipBuildingState gameState) {
//        super(model, controller, gameState);
//        this.gameState = gameState;
//        this.forecastDeck = gameState.getForecastDeck();
//    }
//
//    @Override
//    public void parseAndInvoke(String input) {
//        controller.releaseForecast();
//    }
//
//    @Override
//    public void render() {
//        printShips();
//        printActions();
//        System.out.println("Cards in the forecast deck:\n");
//        for (AdventureCard card : forecastDeck){
////            System.out.println(new CliAdventureCard(model).getDescription());
//        }
//    }
}
