package it.polimi.ingsw.galaxytruckers.model.adventureCards.projectiles;

import it.polimi.ingsw.galaxytruckers.model.Dice;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.viewEnums.ProjectileType;

import java.awt.*;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.IntSupplier;
import java.util.stream.Stream;

public abstract class Projectile {

    protected final int direction;
    protected int diceRoll;
    private static final Dice dice = new Dice() {};

    public Projectile( IntSupplier dice, int direction) {
        this.direction = direction;
        this.diceRoll = dice.getAsInt();
    }

    public Projectile(int direction) {
        this(dice, direction);
    }

    /**
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
     * @param shipBoard the ship that is threatened by the projectile
     * @return the set of locations of the activatable components of the right type to protect from a projectile
     */
    public abstract Set<Point> getActivatablePoints(ShipBoard shipBoard);

    /**
     * @param shipBoard the ship that is threatened by the projectile
     * @return an optional with the component position to be eliminated
     */
    protected Optional<Point> getFirstFoundComponentPosition(ShipBoard shipBoard) {
        Stream<Map.Entry<Point, Component>> line = shipBoard.getComponentMap().entrySet().stream()
                .filter(e -> (direction % 2 == 0 ? e.getKey().x : e.getKey().y) == diceRoll);

        if (direction == 0) {
            return line.min(Comparator.comparingInt(e -> e.getKey().y))
                    .map(Map.Entry::getKey); // Min y
        } else if (direction == 1) {
            return line.max(Comparator.comparingInt(e -> e.getKey().x))
                    .map(Map.Entry::getKey); // Max x
        } else if (direction == 2) {
            return line.max(Comparator.comparingInt(e -> e.getKey().y))
                    .map(Map.Entry::getKey); // Max y
        } else if (direction == 3) {
            return line.min(Comparator.comparingInt(e -> e.getKey().x))
                    .map(Map.Entry::getKey); // Min x
        } else {
            throw new IllegalArgumentException("Invalid direction");
        }
    }

    protected abstract Optional<Point> getComponentPositionToRemove(ShipBoard shipBoard);

    public abstract ProjectileType getProjectileType();

    public int getDiceRoll() {
        return diceRoll;
    }

    public int getDirection() {
        return direction;
    }
}