package it.polimi.ingsw.galaxytruckers.view.cliElements;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.FourColors;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ComponentBank;
import it.polimi.ingsw.galaxytruckers.network.client.ConfigFactory;
import it.polimi.ingsw.galaxytruckers.view.DescriptionUtils;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.Component;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CliComponentBank extends CliElement {

    private final ComponentBank componentBank;

    private final boolean[] forecastDeck;

    public CliComponentBank(ClientModel model) {
        super(model);
        this.componentBank = componentBank;
        this.forecastDeck = forecasts;
        if (config.isForecastPresent()) {
            forecastDeck = model.getForecastDeckAvailablility();
            for(BooleanProperty forecast : forecastDeck) {
                forecast.addListener(this);
            }
        } else {
            forecastDeck = null;
        }

        coveredComponentN = model.coveredComponentNProperty();
        coveredComponentN.addListener(this);

        revealedComponents = new SimpleMapProperty<>(FXCollections.observableHashMap());
        revealedComponents.addListener(this);

        model.revealedComponentsProperty().addListener((ListChangeListener<Component>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (Component added : change.getAddedSubList()) {
                        try {
                            revealedComponents.put(
                                    added,
                                    new CliComponent(model, new SimpleObjectProperty<>(added))
                            );
                            System.out.println("ADDED");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                if (change.wasRemoved()) {
                    for (Component removed : change.getRemoved()) {
                        revealedComponents.remove(removed);
                    }
                }
            }
        } );
    }

    @Override
    public List<String> getDescription() {
        String padding = "  ";
        StringBuilder row = new StringBuilder();
        List<String> description = new ArrayList<>();

        description.add("Face down: " + coveredComponentN.get());

        description.add("Face up: ");
        for (int i = 0; i < 3; i++) {
            for (CliComponent cliComponent : revealedComponents.values()) {
                row.append(cliComponent.getDescription().get(i));
                row.append(padding);
            }
            description.add(row.toString());
            row.setLength(0);
        }
        for (int n = 1; n <= revealedComponents.size(); n++) {
            row.append("  ").append(n).append("  ").append(padding);
        }
        description.add(row.toString());

        if (forecastDeck != null) {
            List<String> forecast = new ArrayList<>();
            forecast.add("  deck 1    deck 2    deck 3  ");
            forecast.add(
                    (forecastDeck.get(0).get() ? "available " : "  taken   ") +
                    (forecastDeck.get(1).get() ? "available " : "  taken   ") +
                    (forecastDeck.get(2).get() ? "available " : "  taken   ")
            );
            description.addAll(DescriptionUtils.borderAndTitle(forecast, "forecast decks"));
        }

        return description;
    }
}
