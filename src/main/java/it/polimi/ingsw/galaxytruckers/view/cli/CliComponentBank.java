package it.polimi.ingsw.galaxytruckers.view.cli;

import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.Component;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
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
import java.util.List;

public class CliComponentBank extends CliElement {

    IntegerProperty coveredComponentN;
    List<CliComponent> revealedComponents;

    public CliComponentBank(ClientModel model) throws IOException {
        super(model);

        coveredComponentN = model.coveredComponentNProperty();
        coveredComponentN.addListener(this);

        model.revealedComponentsProperty().addListener(this);
    }

    @Override
    public List<String> getNewDescription() throws IOException {
        String padding = "  ";
        StringBuilder row = new StringBuilder();
        List<String> description = new ArrayList<>();

        description.add("Face down: " + coveredComponentN.get());

        description.add("Face up: ");
        for (int i = 0; i < 3; i++) {
            for (Component component : model.revealedComponentsProperty()) {
                // TODO optimize to minimize redrawing // TODO: restore
//                row.append(new CliComponent(model, component).getDescription().get(i));
//                System.out.println("REVEALED");
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
