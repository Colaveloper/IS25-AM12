package it.polimi.ingsw.galaxytruckers.view.cliElements;

import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;

import java.util.*;

public class CliFlightBoard extends CliElement {

    public CliFlightBoard(ClientModel model) {
        super(model);
        model.startingPositionLeftProperty().addListener(this);
    }

    @Override
    public List<String> getNewDescription() {
        String[] result = new String[model.getLoopLength()];
        Arrays.fill(result, "_");

        // Place '□' at the starting positions
        model.startingPositionLeftProperty().forEach(pos -> result[pos] = "□");

        // Place emojis from colorToPlace
        model.colorToPlaceProperty().forEach((color, pos) -> result[pos] = color.getDescription());

        return List.of(String.join("", result));
    }
}
