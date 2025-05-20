package it.polimi.ingsw.galaxytruckers.view.cliElements;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.FourColors;
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

    private final IntegerProperty coveredComponentN;
    private final MapProperty<Component, CliComponent> revealedComponents;

    private final List<BooleanProperty> forecastDeck;

    private CliComponentBank(ClientModel model) throws IOException {
        super(model);

        forecastDeck = model.getForecastDeckAvailablility();
        for(BooleanProperty forecast : forecastDeck) {
            forecast.addListener(this);
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
    public List<String> getNewDescription() throws IOException {
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
        row.setLength(0);

        row
                .append("Forecast decks:" + "\t\tdeck 1 :")
                .append(forecastDeck.get(0).get())
                .append("\t\tdeck 2: ")
                .append(forecastDeck.get(1).get())
                .append("\t\tdeck 3: ")
                .append(forecastDeck.get(2).get());

        description.add(row.toString());

        return description;
    }


    /////////////////////////////////////////// SINGLETON LOGIC ///////////////////////////////////////
    private static CliComponentBank instance;

    public static synchronized CliComponentBank getInstance(ClientModel model) throws IOException {
        if (instance == null) {
            instance = new CliComponentBank(model);
        }
        return instance;
    }

    // Optional: a version without parameters once initialized
    public static CliComponentBank getInstance() {
        if (instance == null) {
            throw new IllegalStateException("CliComponentBank not initialized. Call getInstance(model) first.");
        }
        return instance;
    }
}
