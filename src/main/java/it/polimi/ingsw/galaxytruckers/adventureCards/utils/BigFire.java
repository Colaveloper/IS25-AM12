package it.polimi.ingsw.galaxytruckers.adventureCards.utils;

import it.polimi.ingsw.galaxytruckers.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.Optional;
import java.util.Set;

public class BigFire extends Projectile{
    public BigFire(int direction) {
        super(direction);
    }

    @Override
    public boolean fireAt(ShipBoard shipBoard) {
        Optional<Component> hitComponent = super.getHitComponent(shipBoard);
        hitComponent.ifPresent(shipBoard::remove);
        return hitComponent.isPresent();
    }

    @Override
    public Set<Point> getActivatablePoints(ShipBoard shipBoard) {
        return Set.of();
    }
}
