package it.polimi.ingsw.galaxytruckers.view.cli;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.FourColors;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;

import java.io.IOException;
import java.util.*;

public class CliAllShips extends CliElement {

    private final Map<FourColors, CliShipBoard> cliShipBoards; // ALL FINAL
    private final Map<FourColors, CliComponent> hands; // UPDATE WHEN HANDS CHANGES

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
    }

    @Override
    public List<String> getNewDescription() throws IOException {
        List<List<String>> descriptions = new ArrayList<>();
        List<String> sequenceDescription = new ArrayList<>();
        StringBuilder row = new StringBuilder();

        for (FourColors c : model.getPlayerToColor().values()) {
            List<String> shipDescription = new ArrayList<>();

            shipDescription.addAll(cliShipBoards.get(c).getDescription());


            shipDescription.addAll(hands.get(c).getDescription());

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
