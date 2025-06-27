package it.polimi.ingsw.galaxytruckers.server.model.adventureCards.projectiles;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.Cannon;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.shared.enums.Direction;
import it.polimi.ingsw.galaxytruckers.shared.enums.ProjectileType;

import java.awt.*;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.IntSupplier;
import java.util.stream.Collectors;

/**
 * Represents a Big Meteor projectile in the Galaxy Truckers game.
 */
public class BigMeteor extends Projectile {
    /**
     * Constructs a Big Meteor projectile with a specified dice roll and direction.
     *
     * @param dice      the dice roll supplier to determine the position of the Big Meteor
     * @param direction the direction of the Big Meteor
     */
    public BigMeteor(IntSupplier dice, Direction direction) {
        super(dice, direction);
    }

    /**
     * Constructs a Big Meteor projectile with a specified direction.
     *
     * @param direction the direction of the Big Meteor
     */
    public BigMeteor(Direction direction) {
        super(direction);
    }

    /**
     * {@inheritDoc}
     * Returns a set of points where cannons can be activated to defend against the Big Meteor.
     *
     * @param shipBoard the ship board to check for activatable points
     * @return a set of points where cannons can be activated
     */
    @Override
    public Set<Point> getActivatablePoints(ShipBoard shipBoard) {
        return shipBoard.getCannons().entrySet().stream()
                .filter(e -> {
                    Cannon c = e.getValue();
                    return shipBoard.getActivatables().containsKey(e.getKey())
                            && cannonPositionIsEffective(e);
                })
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    /**
     * {@inheritDoc}
     * This method returns the first found component position if there
     * are no cannons effectively firing at the projectile.
     *
     * @param shipBoard the ship that is threatened by the projectile
     * @return an Optional containing the position of the component to be removed, if any
     */
    @Override
    protected Optional<Point> getComponentPositionToRemove(ShipBoard shipBoard) {
        return getFirstFoundComponentPosition(shipBoard).filter(
                (_) -> shipBoard.getCannons().entrySet().stream()
                        .filter(e -> {
                            Cannon c = e.getValue();
                            return c.getFirePower() > 0;
                        })
                        .noneMatch(this::cannonPositionIsEffective));
    }

    /**
     * Checks if the cannon's position is effective against the Big Meteor based on its direction and dice roll.
     *
     * @param e the entry containing the cannon's position and its properties
     * @return true if the cannon's position is effective, false otherwise
     */
    @VisibleForTesting
    protected boolean cannonPositionIsEffective(Map.Entry<Point, Cannon> e) {
        return e.getValue().getOrientation() == direction && switch (direction) {
            case Direction.UP -> e.getKey().x == diceRoll;
            case Direction.LEFT, Direction.RIGHT -> Math.abs(e.getKey().y - diceRoll) < 2;
            case Direction.DOWN -> Math.abs(e.getKey().x - diceRoll) < 2;
        };
    }

    /**
     * {@inheritDoc}
     * This method returns the type of the projectile, which is BIGMETEOR.
     *
     * @return the ProjectileType BIGMETEOR
     */
    @Override
    public ProjectileType getProjectileType() {
        return ProjectileType.BIGMETEOR;
    }
}
