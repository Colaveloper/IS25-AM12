package it.polimi.ingsw.galaxytruckers.view.cliElements;

import it.polimi.ingsw.galaxytruckers.view.DescriptionUtils;
import it.polimi.ingsw.galaxytruckers.view.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.view.model.Player;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.util.*;

public class CliFlightBoard extends CliElement {
    private final int loopLength;
    private final List<Integer> startingPositions;
    private final Map<ShipBoard, Integer> shipToPlace;

    public CliFlightBoard(FlightBoard flightBoard) {
        shipToPlace = new HashMap<>(flightBoard.getShipToPlace());
        startingPositions = new ArrayList<>(flightBoard.getStartingPositions());
        loopLength = flightBoard.getLoopLength();
    }

    public void setPosition(ShipBoard shipBoard, int position) {
        shipToPlace.put(shipBoard, position);
    }

    @Override
    protected List<String> getNewDescription() {
        String[] asArray = new String[loopLength];
        Arrays.fill(asArray, "_");

        // Place '□' at the starting positions
        startingPositions.forEach(pos -> asArray[pos] = "▷");

        // Place emojis from colorToPlace
        shipToPlace.entrySet().stream()
                .map(pos -> Map.entry(
                        pos.getKey().getColor(),
                        Math.floorMod(pos.getValue(), loopLength)))
                .forEach(e -> asArray[e.getValue()] = e.getKey().getDescription());

        List<String> result = new ArrayList<>(List.of(String.join("", asArray)));
        result = DescriptionUtils.borderAndTitle(result, "flight board");

        return result;
    }

    public void updatePositions(ShipBoard shipBoard, int position) {
        shipToPlace.put(shipBoard, position);
    }
}
