package it.polimi.ingsw.galaxytruckers.model.adventureCards.projectiles;

import it.polimi.ingsw.galaxytruckers.model.Dice;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import it.polimi.ingsw.galaxytruckers.view.enums.ProjectileType;

import java.awt.*;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.IntSupplier;
import java.util.stream.Stream;

/**
 * Represents a projectile that can be fired at a ship.
 * It has a direction and a dice roll that determine its origin.
 */
public abstract class Projectile {

    protected final Direction direction;
    protected int diceRoll;
    private static final Dice dice = new Dice() {};

    /**
     * Constructs a Projectile with a specified dice roll and direction.
     * (used for testing purposes)
     *
     * @param dice the dice roll that determines the projectile's origin
     * @param direction the direction from which the projectile is fired
     */
    public Projectile(IntSupplier dice, Direction direction) {
        this.direction = direction;
        this.diceRoll = dice.getAsInt();
    }

    /**
     * Constructs a Projectile with a specified direction and a random dice roll.
     *
     * @param direction the direction from which the projectile is fired
     */
    public Projectile(Direction direction) {
        this(dice, direction);
    }

    /**
     * After any activation took place, this method is called to remove
     * the component at {@code getComponentPositionToRemove}, if any.
     * The return value indicates whether a component was actually removed.
     *
     * @param shipBoard the ship that is threatened by the projectile
     * @return true if a component is removed (in that case a check on the connectivity should take place), false otherwise
     */
    public boolean fireAt(ShipBoard shipBoard) {
        Optional<Point> removalPosition = getComponentPositionToRemove(shipBoard);
        removalPosition.ifPresent(shipBoard::discardComponent);
        return removalPosition.isPresent();
    }

    /**
     * Returns the set of locations of the activatable components to protect from a specific projectile.
     *
     * @param shipBoard the ship that is threatened by the projectile
     * @return the set of locations of the activatable components of the right type to protect from a projectile
     */
    public abstract Set<Point> getActivatablePoints(ShipBoard shipBoard);

    /**
     * Returns the first found component position, if any,that the projectile hits.
     *
     * @param shipBoard the ship that is threatened by the projectile
     * @return an optional with the component position found, if any
     */
    protected Optional<Point> getFirstFoundComponentPosition(ShipBoard shipBoard) {
        Stream<Map.Entry<Point, Component>> line = shipBoard.getComponentMap().entrySet().stream()
                .filter(e -> (
                        (direction==Direction.UP || direction==Direction.DOWN)
                                ? e.getKey().x
                                : e.getKey().y) == diceRoll);



        return switch (direction) {
            case UP    -> line.min(Comparator.comparingInt(e -> e.getKey().y)).map(Map.Entry::getKey); // Min y
            case RIGHT -> line.max(Comparator.comparingInt(e -> e.getKey().x)).map(Map.Entry::getKey); // Max x
            case DOWN  -> line.max(Comparator.comparingInt(e -> e.getKey().y)).map(Map.Entry::getKey); // Max y
            case LEFT  -> line.min(Comparator.comparingInt(e -> e.getKey().x)).map(Map.Entry::getKey); // Min x
        };
    }

    /**
     * Returns the position of the component to remove, if any, based on {@code getFirstFoundComponentPosition}.
     *
     * @param shipBoard the ship that is threatened by the projectile
     * @return an optional with the position of the component to remove, if any
     */
    protected abstract Optional<Point> getComponentPositionToRemove(ShipBoard shipBoard);

    /**
     * @return the type of the projectile
     */
    public abstract ProjectileType getProjectileType();

    public int getDiceRoll() {
        return diceRoll;
    }

    public Direction getDirection() {
        return direction;
    }
}