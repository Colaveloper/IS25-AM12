package it.polimi.ingsw.galaxytruckers.view.cli;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import javafx.beans.InvalidationListener;

import java.io.IOException;
import java.util.*;

public class CliAllShips extends CliElement {

    private final Map<Colors, CliShipBoard> cliShipBoards = new HashMap<>();

    public CliAllShips(ClientModel model) {
        super(model);
        model.getShipsProperty().addListener(this);
        model.getShipsProperty().forEach((color, ship) -> {
            try {
                CliShipBoard cliShipBoard = new CliShipBoard(model, color);
                cliShipBoards.put(color, cliShipBoard);
                cliShipBoard.addListener(this);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        model.getHandProperty().addListener(this); // TODO didn't check yet
    }

    @Override
    public List<String> getNewDescription() throws IOException {
        List<List<String>> descriptions = new ArrayList<>();
        List<String> sequenceDescription = new ArrayList<>();
        StringBuilder row = new StringBuilder();

        for (Colors c : model.getPlayerToColor().values()) {
            List<String> shipDescription = new ArrayList<>();

            CliShipBoard newShip = new CliShipBoard(model, c);
            newShip.addListener(this);
            shipDescription.addAll(newShip.getDescription());

            CliComponent newHand = new CliComponent(model, model.getHandProperty().get(c));
            shipDescription.addAll(newHand.getDescription());

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
