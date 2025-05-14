package it.polimi.ingsw.galaxytruckers.view.cli;

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

    public CliComponentBank(ClientModel model) throws IOException {
        super(model);

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
        description.add(row.toString());

        return description;
    }
}
