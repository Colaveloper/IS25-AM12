package it.polimi.ingsw.galaxytruckers.view.cli;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.Component;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CliComponentBank extends CliElement {

    IntegerProperty coveredComponentN;
    MapProperty<Component, CliComponent> revealedComponents;

    private Map<Colors, CliComponent> hands; // UPDATE WHEN HANDS CHANGES
    private Map<Colors, List<CliComponent>> stashedComponentsMap;

    public CliComponentBank(ClientModel model) throws IOException {
        super(model);

        hands = new SimpleMapProperty<>(FXCollections.observableHashMap());
        model.getHand().forEach((color, componentProperty) -> {
            try {
                CliComponent hand = new CliComponent(model, componentProperty);
                hands.put(color, hand);
                hand.addListener(this);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        stashedComponentsMap = new SimpleMapProperty<>(FXCollections.observableHashMap());
        model.getStashed().forEach((color, propertyList) -> {
            try {
                List<CliComponent> stashedComponents = new ArrayList<>();
                for(ObjectProperty<Component> property : propertyList) {
                    CliComponent stashed = new CliComponent(model, property);
                    stashed.addListener(this);
                    stashedComponents.add(stashed);
                }
                stashedComponentsMap.put(color, stashedComponents);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

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
                                    new CliComponent(model, new SimpleObjectProperty<Component>(added))
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
