package it.polimi.ingsw.galaxytruckers.view.cliElements;

import it.polimi.ingsw.galaxytruckers.view.DescriptionUtils;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.FlightBoard;

import java.util.*;

public class CliFlightBoard extends CliElement {
    private final FlightBoard flightBoard;

    public CliFlightBoard(ClientModel model) {
        super();
        this.flightBoard = model.getGame().getFlightBoard();
        //model.startingPositionLeftProperty().addListener(this);
    }

    @Override
    public List<String> getNewDescription() {
        String[] asArray = new String[flightBoard.getLoopLength()];
        Arrays.fill(asArray, "_");

        // Place '□' at the starting positions
        flightBoard.getStartingPositions().forEach(pos -> asArray[pos] = "□");

        // Place emojis from colorToPlace
        flightBoard.getShipToPlace().entrySet().stream()
                .map(e -> Map.entry(
                        e.getKey().getColor(),
                        e.getValue()%flightBoard.getLoopLength()))
                .forEach((e) -> asArray[e.getValue()] = e.getKey().getDescription());

        List<String> result = new ArrayList<>(List.of(String.join("", asArray)));
        DescriptionUtils.borderAndTitle(result, "flight board");

        return result;
    }
}
