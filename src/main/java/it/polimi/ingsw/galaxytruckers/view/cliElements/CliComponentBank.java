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

    public CliComponentBank(ClientModel model) throws IOException {
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
        for (int n = 1; n <= model.revealedComponentsProperty().size(); n++) {
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


        //STASHED AND HAND IN COMPONENTBANK
//        List<List<String>> allShipsHandsStashed = new ArrayList<>();
//
//        for(Colors c : model.getPlayerToColor().values()) {
//            List<String> handStashedDescription = new ArrayList<>();
//            List<List<String>> stashedDescription = new ArrayList<>();
//            List<String> handDescription = new ArrayList<>();
//
//            handStashedDescription.add("hand:\t\tstashed:\t\t\t");
//
//            handDescription.addAll(hands.get(c).getDescription());
//            for(CliComponent component : stashedComponentsMap.get(c)) {
//                stashedDescription.add(component.getDescription());
//            }
//
//
//            for (int i = 0; i < handDescription.size(); i++) {
//                row.append(handDescription.get(i));
//                row.append("\t\t");
//                for(List<String> stashed : stashedDescription) {
//                    row.append(stashed.get(i));
//                }
//                row.append("\t\t\t");
//                handStashedDescription.add(row.toString());
//                row.setLength(0);
//            }
//
//            allShipsHandsStashed.add(handStashedDescription);
//        }
//
//        for(int index = 0; index < allShipsHandsStashed.getFirst().size(); index++) {
//            for(List<String> stashed : allShipsHandsStashed) {
//                row.append(stashed.get(index));
//            }
//            description.add(row.toString());
//            row.setLength(0);
//        }
//
//        description.add(row.toString());

        return description;
    }
}
