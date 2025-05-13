package it.polimi.ingsw.galaxytruckers.view;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import it.polimi.ingsw.galaxytruckers.view.cli.CliElement;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import javafx.collections.FXCollections;
import javafx.scene.Node;

import java.util.*;

public class CliFlightBoard extends CliElement {

    public CliFlightBoard(ClientModel model) {
        super(model);
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
