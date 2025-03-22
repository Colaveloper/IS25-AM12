package it.polimi.ingsw.galaxytruckers.adventureCards.utils;

import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.Set;

public class SmallMeteor extends Projectile {
    @Override
    public boolean fireAt(ShipBoard shipBoard) {
        return false;
    }

    @Override
    public Set<Point> getActivatablePoints(ShipBoard shipBoard) {
        return Set.of();
    }
}
