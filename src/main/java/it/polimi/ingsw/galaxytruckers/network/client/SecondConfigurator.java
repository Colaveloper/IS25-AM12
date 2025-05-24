package it.polimi.ingsw.galaxytruckers.network.client;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class SecondConfigurator extends ConfigFactory {

    @Override
    public boolean isForecastPresent() {
        return true;
    }

    @Override
    public boolean isStashingAllowed() {
        return true;
    }

    @Override
    public boolean isHourglassPresent() {
        return true;
    }

    @Override
    public boolean isShipPlacementFree() {
        return true;
    }

    @Override
    public boolean isGivingUpAllowed() {
        return true;
    }

    @Override
    public Set<Point> getShipArea() {
        return Set.of(
                new Point(4, 7), new Point(4, 8), new Point(4, 9),
                new Point(5, 6), new Point(5, 7), new Point(5, 8), new Point(5, 9),
                new Point(6, 5), new Point(6, 6), new Point(6, 7), new Point(6, 8), new Point(6, 9),
                new Point(7, 6), new Point(7, 7), new Point(7, 8),
                new Point(8, 5), new Point(8, 6), new Point(8, 7), new Point(8, 8), new Point(8, 9),
                new Point(9, 6), new Point(9, 7), new Point(9, 8), new Point(9, 9),
                new Point(10, 7),new Point(10, 8),new Point(10, 9)
        );
    }

    @Override
    public List<Integer> getStartingPositions() {
        return new ArrayList<>(List.of(6, 3, 1, 0));
    }

    @Override
    public int getLoopLength() {
        return 24;
    }
}
