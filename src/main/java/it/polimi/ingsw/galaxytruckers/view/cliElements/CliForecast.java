package it.polimi.ingsw.galaxytruckers.view.cliElements;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.view.observables.ObservableList;
import it.polimi.ingsw.galaxytruckers.view.DescriptionUtils;

import java.util.ArrayList;
import java.util.List;

public class CliForecast extends CliElement {
    private final List<GameColor> blockedForecasts = new ArrayList<>();

    public CliForecast(ObservableList<GameColor> blockedForecasts) {

        this.blockedForecasts.add(null);
        this.blockedForecasts.add(null);
        this.blockedForecasts.add(null);
        blockedForecasts.addListener(new ObservableList.Listener<>() {
            @Override
            public void onAdd(int index, GameColor element) {
                CliForecast.this.blockedForecasts.set(index, element);
            }

            @Override
            public void onRemove(int index, GameColor element) {
                CliForecast.this.blockedForecasts.remove(index);
            }
        });
    }

    @Override
    protected List<String> getNewDescription() {
        List<String> result = new ArrayList<>();

        for (int i=0; i<blockedForecasts.size(); i++) {
            List<String> forecastDescription = new ArrayList<>();
            forecastDescription.add(blockedForecasts.get(i) != null
                            ? "   "+blockedForecasts.get(i).getDescription()+"   "
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

