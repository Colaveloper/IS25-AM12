package it.polimi.ingsw.galaxytruckers.network.client;

import java.awt.*;
import java.util.List;
import java.util.Set;

public abstract class ConfigFactory {
    public abstract boolean isForecastPresent();
    public abstract boolean isStashingAllowed();
    public abstract boolean isHourglassPresent();
    public abstract boolean isShipPlacementFree();
    public abstract boolean isGivingUpAllowed();
    public abstract Set<Point> getShipArea();
    public abstract List<Integer> getStartingPositions();
    public abstract int getLoopLength();
    public int getComponentsN() {
        return 156;
    };
}
