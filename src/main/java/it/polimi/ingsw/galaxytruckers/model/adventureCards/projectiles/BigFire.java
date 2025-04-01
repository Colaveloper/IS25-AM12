package it.polimi.ingsw.galaxytruckers.model.adventureCards.projectiles;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.Optional;
import java.util.Set;
import java.util.function.IntSupplier;

public class BigFire extends Projectile{
    public BigFire(IntSupplier dice, int direction) {
        super(dice, direction);
    }

    public BigFire(int direction) {
        super(direction);
    }

    @Override
    public Set<Point> getActivatablePoints(ShipBoard shipBoard) {
        return Set.of();
    }

    @Override
    protected Optional<Component> getComponentToRemove(ShipBoard shipBoard) {
        return getFirstFoundComponent(shipBoard);
    }
}
