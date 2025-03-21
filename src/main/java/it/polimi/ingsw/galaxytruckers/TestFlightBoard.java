package it.polimi.ingsw.galaxytruckers;

import it.polimi.ingsw.galaxytruckers.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;
import javafx.scene.image.Image;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TestFlightBoard extends FlightBoard{
    private static Image image;

    public TestFlightBoard(Set<ShipBoard> allShips) {
        super(allShips);
        this.loopLength = Level.TEST.getLoopLength();
        this.startingPositionsLeft = new ArrayList<>(Level.TEST.getStartingPositions());
        image = new Image("file:src/main/resources/texture/cardboard/learning-flight-board.png");
    }

    @Override
    public boolean placeShipOnFlightBoard(ShipBoard shipBoard, int startingPosition) {
        shipToPlace.put(shipBoard, startingPositionsLeft.removeFirst());
        // to be interpreted as "building phase is finished for everybody"
        return startingPositionsLeft.size() + allShips.size() == 4;
    }
//
//    @Override
//    public Set<ShipBoard> getLappedShips() {
//        return Set.of();
//    }

    @Override
    public void giveUp(ShipBoard shipBoard) {
        throw new IllegalArgumentException("cannot give up in test flight");
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
}
