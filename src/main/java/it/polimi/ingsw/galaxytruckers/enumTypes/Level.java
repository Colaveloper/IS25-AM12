package it.polimi.ingsw.galaxytruckers.enumTypes;

import java.awt.Point;
import java.util.*;

public enum Level {
    TEST(Arrays.asList(new Point(5, 7), new Point(5, 8), new Point(5, 9), new Point(6, 6), new Point(6, 7), new Point(6, 8), new Point(6, 9), new Point(7, 5), new Point(7, 6), new Point(7, 7), new Point(7, 8), new Point(7, 6), new Point(8, 6), new Point(8, 7), new Point(8, 8), new Point(8, 9), new Point(9, 7), new Point(9, 8), new Point(9, 9)),
            Arrays.asList(new Point(9, 5), new Point(10, 5)),
            18,
            Arrays.asList(4, 2, 1, 0),
            null),
    SECOND(Arrays.asList(new Point(4, 7), new Point(4, 8), new Point(4, 9), new Point(5, 6), new Point(5, 7), new Point(5, 8), new Point(5, 9), new Point(6, 5), new Point(6, 6), new Point(6, 7), new Point(6, 8), new Point(6, 9), new Point(7, 6), new Point(7, 7), new Point(7, 8), new Point(8, 5), new Point(8, 6), new Point(8, 7), new Point(8, 8), new Point(8, 9), new Point(9, 6), new Point(9, 7), new Point(9, 8), new Point(9, 9), new Point(10, 7), new Point(10, 8), new Point(10, 9)),
            Arrays.asList(new Point(9, 5), new Point(10, 5)),
            24,
            Arrays.asList(6, 3, 1, 0),
            3);

    private List<Point> shipArea;
    private List<Point> stashArea;
    private int loopLength;
    private List<Integer> startingPositions;
    private Integer buildingTime; // can be null
//    private Map<Level, Integer> pileComposition;


    Level(List<Point> shipPoints, List<Point> stashPoints, int loopLength, List<Integer> startingPositions, Integer buildingTime) { // (, List<Level> pileComposition)
        this.shipArea = shipPoints;
        this.stashArea = stashPoints;
        this.loopLength = loopLength;
        this.startingPositions = startingPositions;
        this.buildingTime = buildingTime;
//        this.pileComposition = pileComposition;
    }

    public List<Point> getShipArea() {
        return shipArea;
    }

    public List<Point> getStashArea() {
        return stashArea;
    }

    public int getLoopLength() {
        return loopLength;
    }

    public List<Integer> getStartingPositions() {
        return startingPositions;
    }

    public Optional<Integer> getBuildingTime() {
        return Optional.ofNullable(buildingTime);
    }

//    public List<Level> getPileComposition() {
//        return pileComposition;
//    }

}
