package it.polimi.ingsw.galaxytruckers.model;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import javafx.scene.image.Image;

import java.util.*;
import java.util.List;

public class TestFlightBoard extends FlightBoard{
    private static Image image;
    @VisibleForTesting
    protected static int loopLength;
    @VisibleForTesting
    protected static List<Integer> startingPositions;


    public TestFlightBoard(Set<ShipBoard> allShips) {
        super(allShips);
        loopLength = 18;
        startingPositions = Arrays.asList(4, 2, 1, 0).subList(0, allShips.size());
        image = new Image("file:src/main/resources/texture/cardboard/learning-flight-board.png");
        this.startingPositionsLeft = new ArrayList<>(startingPositions);
    }

    @Override
    public boolean placeShipOnFlightBoard(ShipBoard shipBoard, int startingPosition) {
        shipToPlace.put(shipBoard, startingPositionsLeft.removeFirst());
        // to be interpreted as "building phase is finished for everybody"
        return startingPositionsLeft.isEmpty();
    }

    @Override
    public Image getImage() {
        return image;
        // TODO: composite players on top
    }

    @Override
    public String getDescription() {
        return "Test-Flight: "+super.getDescription();
        // TODO: describe
    }

    @Override
    protected int getLoopLength() {
        return loopLength;
    }
}
