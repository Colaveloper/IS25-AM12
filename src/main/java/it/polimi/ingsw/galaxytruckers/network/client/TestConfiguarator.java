package it.polimi.ingsw.galaxytruckers.network.client;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
        return Set.of();
    }

    @Override
    public List<Integer> getStartingPositions() {
        return new ArrayList<>();
    }

    @Override
    public int getLoopLenght() {
        return 0;
    }
}
