package it.polimi.ingsw.galaxytruckers.adventureCards.utils;

import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.Set;

public abstract class Projectile {
    public abstract boolean fireAt(ShipBoard shipBoard);
    public abstract Set<Point> getActivatablePoints(ShipBoard shipBoard);
}