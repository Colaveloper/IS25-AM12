package it.polimi.ingsw.galaxytruckers.client.view.cli.cliElements;

import it.polimi.ingsw.galaxytruckers.client.view.cli.DescriptionUtils;
import it.polimi.ingsw.galaxytruckers.client.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.ShipBoard;

import java.util.*;

/**
 * Represents a CLI flight board that displays the positions of ships
 */
public class CliFlightBoard extends CliElement {
    private final int loopLength;
    private final List<Integer> startingPositions;
    private final Map<ShipBoard, Integer> shipToPlace;

    /**
     * Creates a new CLI flight board from the given FlightBoard model.
     *
     * @param flightBoard The FlightBoard model to create the CLI flight board from
     */
    public CliFlightBoard(FlightBoard flightBoard) {
        shipToPlace = new HashMap<>(flightBoard.getShipToPlace());
        startingPositions = new ArrayList<>(flightBoard.getStartingPositions());
        loopLength = flightBoard.getLoopLength();
    }

    /**
     * Sets the position of a ship on the flight board.
     * @param shipBoard the ShipBoard to place
     * @param position the position to place the ship at
     */
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
                .forEach(e -> asArray[e.getValue()] = GameColorCliMapper.toAnsiBullet(e.getKey()));

        List<String> result = new ArrayList<>(List.of(String.join("", asArray)));
        result = DescriptionUtils.borderAndTitle(result, "flight board");

        return result;
    }
}
