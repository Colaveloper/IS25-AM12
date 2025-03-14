package it.polimi.ingsw.galaxytruckers.shipBuilding;

import java.awt.Point;
import java.util.Arrays;
import java.util.List;

public enum Level {
    TEST(Arrays.asList(
            new Point(5, 7),
            new Point(5, 8),
            new Point(5, 9),
            new Point(6, 6),
            new Point(6, 7),
            new Point(6, 8),
            new Point(6, 9),
            new Point(7, 5),
            new Point(7, 6),
            new Point(7, 7),
            new Point(7, 8),
            new Point(7, 6),
            new Point(8, 6),
            new Point(8, 7),
            new Point(8, 8),
            new Point(8, 9),
            new Point(9, 7),
            new Point(9, 8),
            new Point(9, 9)),
        Arrays.asList(
            new Point(9, 5),
            new Point(10, 5)
            )),
    SECOND(Arrays.asList(
            new Point(4, 7),
            new Point(4, 8),
            new Point(4, 9),
            new Point(5, 6),
            new Point(5, 7),
            new Point(5, 8),
            new Point(5, 9),
            new Point(6, 5),
            new Point(6, 6),
            new Point(6, 7),
            new Point(6, 8),
            new Point(6, 9),
            new Point(7, 6),
            new Point(7, 7),
            new Point(7, 8),
            new Point(8, 5),
            new Point(8, 6),
            new Point(8, 7),
            new Point(8, 8),
            new Point(8, 9),
            new Point(9, 6),
            new Point(9, 7),
            new Point(9, 8),
            new Point(9, 9),
            new Point(10, 7),
            new Point(10, 8),
            new Point(10, 9)),
        Arrays.asList(
            new Point(9, 5),
            new Point(10, 5))
    );

    private List<Point> shipArea;
    private List<Point> stashArea;

    Level(List<Point> shipPoints, List<Point> stashPoints) {
        this.shipArea = shipPoints;
        this.stashArea = stashPoints;
    }

    public List<Point> getShipArea() {
        return shipArea;
    }
    public List<Point> getStashArea() {
        return stashArea;
    }
}
