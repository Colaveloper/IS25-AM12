package it.polimi.ingsw.galaxytruckers.adventureCards.utils;

import it.polimi.ingsw.galaxytruckers.Dice;
import it.polimi.ingsw.galaxytruckers.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.IntSupplier;

public abstract class Projectile {

    protected final int direction;
    protected int diceRoll;
    private static final Dice dice = new Dice() {};;

    public Projectile(IntSupplier dice, int direction) {
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
        throw new UnsupportedOperationException("Method implemented only in subclasses");
    }

    /**
     * @param shipBoard the ship that is threatened by the projectile
     * @return the set of locations of the activatable components of the right type to protect from a projectile
     */
    public abstract Set<Point> getActivatablePoints(ShipBoard shipBoard);

    /**
     * @param shipBoard the ship that is threatened by the projectile
     * @return an optional with the component at risk of being eliminated
     */
    protected Optional<Component> getHitComponent(ShipBoard shipBoard) {
        return switch (direction) {
            case 0 -> // Point with maximum y.
                    shipBoard.getComponentMap().entrySet().stream()
                            .filter(e -> e.getKey().x == diceRoll)
                            .max(Comparator.comparingInt(e -> e.getKey().y))
                            .map(Map.Entry::getValue);
            case 1 -> // Point with minimum x.
                    shipBoard.getComponentMap().entrySet().stream()
                            .filter(e -> e.getKey().x == diceRoll)
                            .min(Comparator.comparingInt(e -> e.getKey().x))
                            .map(Map.Entry::getValue);
            case 2 -> // Point with minimum y.
                    shipBoard.getComponentMap().entrySet().stream()
                            .filter(e -> e.getKey().x == diceRoll)
                            .min(Comparator.comparingInt(e -> e.getKey().y))
                            .map(Map.Entry::getValue);
            case 3 -> // Point with maximum x.
                    shipBoard.getComponentMap().entrySet().stream()
                            .filter(e -> e.getKey().x == diceRoll)
                            .max(Comparator.comparingInt(e -> e.getKey().x))
                            .map(Map.Entry::getValue);
            default -> throw new IllegalArgumentException("Invalid direction");
        };
    }
}