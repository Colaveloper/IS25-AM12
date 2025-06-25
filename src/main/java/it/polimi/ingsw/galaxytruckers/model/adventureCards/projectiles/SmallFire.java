package it.polimi.ingsw.galaxytruckers.model.adventureCards.projectiles;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import it.polimi.ingsw.galaxytruckers.view.enums.ProjectileType;

import java.awt.*;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.IntSupplier;
import java.util.stream.Collectors;

public class SmallFire extends Projectile {
    public SmallFire(IntSupplier dice, Direction direction) {
        super(dice, direction);
    }

    public SmallFire(Direction direction) {
        super(direction);
    }

    /**
     * {@inheritDoc}
     * Returns a set of points where shields can be activated to defend against the Small Fire.
     *
     * @param shipBoard the ship board to check for activatable points
     * @return a set of points where shields can be activated
     */
    @Override
    public Set<Point> getActivatablePoints(ShipBoard shipBoard) {
        return shipBoard.getShields().entrySet().stream()
                .filter(e -> e.getValue().getDefensibleDirections().contains(direction))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    /**
     * {@inheritDoc}
     * This method returns the first found component position if there are no shields effectively defending against the projectile.
     *
     * @param shipBoard the ship that is threatened by the projectile
     * @return an Optional containing the position of the component to be removed, if any
     */
    @Override
    protected Optional<Point> getComponentPositionToRemove(ShipBoard shipBoard) {
        if (shipBoard.getShieldDirections().contains(direction)) {
            return Optional.empty();
        }
        return getFirstFoundComponentPosition(shipBoard);
    }

    /**
     * {@inheritDoc}
     * This method returns the type of the projectile, which is SMALLFIRE.
     *
     * @return the ProjectileType SMALLFIRE
     */
    @Override
    public ProjectileType getProjectileType() {
        return ProjectileType.SMALLFIRE;
    }
}
