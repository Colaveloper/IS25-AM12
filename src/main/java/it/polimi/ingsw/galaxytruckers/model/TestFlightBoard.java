package it.polimi.ingsw.galaxytruckers.model;

import com.google.common.annotations.VisibleForTesting;

import java.util.*;
import java.util.List;

public class TestFlightBoard extends FlightBoard{
    @VisibleForTesting
    protected static int loopLength;
    @VisibleForTesting
    protected static List<Integer> startingPositions;


    public TestFlightBoard(int shipsN) {
        super();
        loopLength = 18;
        startingPositions = Arrays.asList(4, 2, 1, 0).subList(0, shipsN);
        this.startingPositionsLeft.clear();
        this.startingPositionsLeft.addAll(startingPositions);
    }

    @Override
    protected int getLoopLength() {
        return loopLength;
    }
}
