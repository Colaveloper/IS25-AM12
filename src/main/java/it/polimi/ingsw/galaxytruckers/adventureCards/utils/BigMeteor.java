package it.polimi.ingsw.galaxytruckers.adventureCards.utils;

import it.polimi.ingsw.galaxytruckers.shipBuilding.Cannon;
import it.polimi.ingsw.galaxytruckers.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.Optional;
import java.util.Set;
import java.util.function.IntSupplier;
import java.util.stream.Collectors;

public class BigMeteor extends Projectile {
    public BigMeteor(IntSupplier dice, int direction) {
        super(dice, direction);
    }

    public BigMeteor(int direction) {
        super(direction);
    }

    @Override
    public Set<Point> getActivatablePoints(ShipBoard shipBoard) {
        return shipBoard.getCannons().keySet().stream()
                .filter(shipBoard.getActivatables().keySet()::contains)
                .collect(Collectors.toSet());
    }

    @Override
    protected Optional<Component> getComponentToRemove(ShipBoard shipBoard) {
        return getFirstFoundComponent(shipBoard).filter(
                (_) -> shipBoard.getCannons().entrySet().stream()
                        .filter(e -> {
                            Cannon c = e.getValue();
                            return c.getOrientation() == direction && c.getFirePower()>0;
                        })
                        .anyMatch(e -> {
                            return switch (direction) {
                                case 0 -> e.getKey().x == diceRoll;
                                case 1, 3 ->  Math.abs(e.getKey().y - diceRoll) < 2;
                                case 2 ->  Math.abs(e.getKey().x - diceRoll) < 2;
                                default -> throw new IllegalArgumentException("Nonexistent direction");
                            };
                        })
        );
    }
}
