package it.polimi.ingsw.galaxytruckers.network.client;

import java.awt.*;
import java.util.List;
import java.util.Set;

public abstract class ConfigFactory {
    abstract boolean isForecastPresent();
    abstract boolean isStashingAllowed();
    abstract boolean isHourglassPresent();
    abstract boolean isShipPlacementFree();
    abstract boolean isGivingUpAllowed();
    abstract Set<Point> getShipArea();
    abstract List<Integer> getStartingPositions();
    abstract int getLoopLenght();
    int getComponentsN() {
        return 0;
    };
}
