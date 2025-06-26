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

    /** {@inheritDoc}
     * <p>Returns an empty set, as you cannot defend from a big fire.</p>
     */
    @Override
    public Set<Point> getActivatablePoints(ShipBoard shipBoard) {
        return Set.of();
    }

    /** * {@inheritDoc}
     * This method always returns the first found component position
     *
     * @param shipBoard the ship that is threatened by the projectile
     * @return an Optional containing the position of the component to be removed, if any
     */
    @Override
    protected Optional<Point> getComponentPositionToRemove(ShipBoard shipBoard) {
        return getFirstFoundComponentPosition(shipBoard);
    }
    /**
     * {@inheritDoc}
     * This method returns the type of the projectile, which is BIGFIRE.
     *
     * @return the ProjectileType BIGFIRE
     */
    @Override
    public ProjectileType getProjectileType() {
        return ProjectileType.BIGFIRE;
    }
}
