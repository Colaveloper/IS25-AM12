package it.polimi.ingsw.galaxytruckers.network.client;

import java.awt.*;
import java.util.*;
import java.util.List;

public class TestConfiguarator extends ConfigFactory {

    @Override
    public boolean isForecastPresent() {
        return false;
    }

    @Override
    public boolean isStashingAllowed() {
        return false;
    }

    @Override
    public boolean isHourglassPresent() {
        return false;
    }

    @Override
    public boolean isShipPlacementFree() {
        return false;
    }

    @Override
    public boolean isGivingUpAllowed() {
        return false;
    }

    @Override
    public Set<Point> getShipArea() {
        return Set.of(new Point(5, 7),
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
                new Point(8, 6),
                new Point(8, 7),
                new Point(8, 8),
                new Point(8, 9),
                new Point(9, 7),
                new Point(9, 8),
                new Point(9, 9));
    }

    @Override
    public List<Integer> getStartingPositions() {
        return new ArrayList<>(List.of(4, 2, 1, 0)) ;
    }

    @Override
    public int getLoopLength() {
        return 18;
    }
}
