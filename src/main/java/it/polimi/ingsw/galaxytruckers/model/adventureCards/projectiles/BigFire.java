package it.polimi.ingsw.galaxytruckers.model.adventureCards.projectiles;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import it.polimi.ingsw.galaxytruckers.view.enums.ProjectileType;

import java.awt.*;
import java.util.Optional;
import java.util.Set;
import java.util.function.IntSupplier;

public class BigFire extends Projectile{
    public BigFire(IntSupplier dice, Direction direction) {
        super(dice, direction);
    }

    public BigFire(Direction direction) {
        super(direction);
    }

    @Override
    public Set<Point> getActivatablePoints(ShipBoard shipBoard) {
        return Set.of();
    }

    @Override
    protected Optional<Point> getComponentPositionToRemove(ShipBoard shipBoard) {
        return getFirstFoundComponentPosition(shipBoard);
    }

    @Override
    public ProjectileType getProjectileType() {
        return ProjectileType.BIGFIRE;
    }
}
