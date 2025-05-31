package it.polimi.ingsw.galaxytruckers.view.cliElements;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.DescriptionUtils;

import java.util.ArrayList;
import java.util.List;

public class CliForecast extends CliElement {
    private final GameColor[] blockedForecasts;

    public CliForecast(ShipBoard[] blockedForecasts) {
        this.blockedForecasts = new GameColor[blockedForecasts.length];
        for (int i = 0; i < blockedForecasts.length; i++) {
            if (blockedForecasts[i] != null) {
                this.blockedForecasts[i] = blockedForecasts[i].getColor();
            } else {
                this.blockedForecasts[i] = null;
            }
        }
    }

    public void removeBlockedForecast(int index) {
        this.blockedForecasts[index] = null;
    }

    public void setBlockedForecasts(int index, GameColor gameColor) {
        this.blockedForecasts[index] = gameColor;
    }

    @Override
    protected List<String> getNewDescription() {
        List<String> result = new ArrayList<>();

        for (int i=0; i<blockedForecasts.length; i++) {
            List<String> forecastDescription = new ArrayList<>();
            forecastDescription.add(blockedForecasts[i] != null
                            ? "   "+blockedForecasts[i].getDescription()+"   "
                            : " free  "
                    );
            forecastDescription.add("   "+i+"   ");
            result = DescriptionUtils.sideBySide(result, forecastDescription);
        }

        return DescriptionUtils.borderAndTitle(result, "forecast decks");
    }
}

//    //    private final List<AdventureCard> forecastDeck;
//
//    @Override
//    protected List<String> getNewDescription() {
//        //Visualizza array di booleani per i deck bloccati
//        for (AdventureCard adventureCard : forecastDeck) {
//            switch (adventureCard) {
//                case AbandonedShipCard e -> {
//                    System.out.println(e.getCardLevel());
//                    System.out.println(e.getCreditPrize());
//                    //...
//                }
//            }
//        }
//        return List.of();
//    }

//            this.forecastDeck = model.getGame().getCurrentState().getLockedForecasts();
//        this.coveredComponentN = componentBank.getCoveredComponentsNProperty();
//        for(Component component : componentBank.getUncoveredComponentsProperty()){
//            revealedComponents.add(new CliComponent(component));
//        }

