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
    CliComponent currentComponent;

    public CliComponentBank(ClientModel model) {
        super(model);
        coveredComponentN = model.coveredComponentNProperty();
        listenToInvalidation(coveredComponentN);
        listenToInvalidation(currentComponent);
    }

    @Override
    public List<String> getNewDescription() throws IOException {
        String padding = "  ";
        StringBuilder row = new StringBuilder();
        List<String> description = new ArrayList<>();

        description.add("Face down: " + coveredComponentN.get());

        description.add("Face up: ");
//        for (int i = 0; i < 3; i++) {
//            for (CliComponent component : revealedComponents) {
//                row.append(component.getDescription().get(i));
////                System.out.println("REVEALED");
//                row.append(padding);
//            }
//            description.add(row.toString());
//            row.setLength(0);
//        }
//        for (int n = 1; n <= revealedComponents.size(); n++) {
//            row.append("  ").append(n).append("  ").append(padding);
//        }
//        description.add(row.toString());
//        row.setLength(0);
//
//        description.add("Stash: " + "\tHand: ");
//        for (int i = 0; i < 3; i++) {
//            for (CliComponent component : stashedComponents) {
//                row.append(component.getDescription().get(i));
//                System.out.println("STASHED");
//                row.append(padding);
//            }
//            for (int n = 0; n < 2 - stashedComponents.size(); n++) {
//                row.append("     ").append(padding);
//            }
//            row.append("\t\t\t");
//            row.append(currentComponent.get().getDescription().get(i));
//            description.add(row.toString());
//            row.setLength(0);
//        }
//        for (int n = 0; n < stashedComponents.size(); n++) {
//            row.append("  ").append((char) ('A' + n)).append("  ").append(padding);
//        }
        description.add(row.toString());

        return description;
    }
}
