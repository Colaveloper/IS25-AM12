package it.polimi.ingsw.galaxytruckers.view.cliElements;

import it.polimi.ingsw.galaxytruckers.view.DescriptionUtils;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;

import java.util.*;

public class CliFlightBoard extends CliElement {

    public CliFlightBoard(ClientModel model) {
        super(model);
        model.startingPositionLeftProperty().addListener(this);
    }

    @Override
    public List<String> getNewDescription() {
        String[] asArray = new String[model.getLoopLength()];
        Arrays.fill(asArray, "_");

        // Place '□' at the starting positions
        model.startingPositionLeftProperty().forEach(pos -> asArray[pos] = "□");

        // Place emojis from colorToPlace
        model.colorToPlaceProperty().forEach((color, pos) -> asArray[pos] = color.getDescription());

        List<String> result = new ArrayList<>(List.of(String.join("", asArray)));
        DescriptionUtils.borderAndTitle(result, "flight board");

        return result;
    }
}
