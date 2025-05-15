package it.polimi.ingsw.galaxytruckers.view.cli;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.Component;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;

import java.io.IOException;
import java.util.*;

public class CliAllShips extends CliElement {

    private final Map<Colors, CliShipBoard> cliShipBoards; // ALL FINAL
    private Map<Colors, CliComponent> hands; // UPDATE WHEN HANDS CHANGES
    private Map<Colors, List<CliComponent>> stashedComponentsMap;

    public CliAllShips(ClientModel model) {
        super(model);
        cliShipBoards = new HashMap<>();
        model.getShips().forEach((color, ship) -> {
            try {
                CliShipBoard cliShipBoard = new CliShipBoard(model, color);
                cliShipBoards.put(color, cliShipBoard);
                cliShipBoard.addListener(this);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

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
    }

    @Override
    public List<String> getNewDescription() throws IOException {
        List<List<String>> descriptions = new ArrayList<>();
        List<String> sequenceDescription = new ArrayList<>();
        StringBuilder row = new StringBuilder();

        for (Colors c : model.getPlayerToColor().values()) {
            List<String> shipDescription = new ArrayList<>();

            shipDescription.addAll(cliShipBoards.get(c).getDescription());


            List<String> handDescription = new ArrayList<>();
            handDescription.addAll(hands.get(c).getDescription());

            List<List<String>> stashedDescription = new ArrayList<>();
            for(CliComponent component : stashedComponentsMap.get(c)) {
                stashedDescription.add(component.getDescription());
            }


            shipDescription.add(" hand:\t\tstashed" + " ".repeat(23));
            for (int i = 0; i < handDescription.size(); i++) {
                row.append(handDescription.get(i));
                row.append("\t\t");
                for(List<String> stashed : stashedDescription) {
                    row.append(stashed.get(i));
                }
                row.append(" ".repeat(21));
                shipDescription.add(row.toString());
                row.setLength(0);
            }

            descriptions.add(shipDescription);
        }
        for (int i = 0; i < descriptions.getFirst().size(); i++) {
            for (List<String> shipDescription : descriptions) {
                row.append(shipDescription.get(i));
            }
            sequenceDescription.add(row.toString());
            row.setLength(0);
        }

        return sequenceDescription;
    }
}
