package it.polimi.ingsw.galaxytruckers.model.adventureCards.projectiles;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Cannon;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.Map;
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
        return shipBoard.getCannons().entrySet().stream()
                .filter(e -> {
                    Cannon c = e.getValue();
                    return shipBoard.getActivatables().containsKey(e.getKey())
                            && c.getFirePower()>0
                            && cannonPositionIsEffective(e);
                })
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    @Override
    protected Optional<Point> getComponentPositionToRemove(ShipBoard shipBoard) {
        return getFirstFoundComponentPosition(shipBoard).filter(
                (_) -> shipBoard.getCannons().entrySet().stream()
                .filter(e -> {
                    Cannon c = e.getValue();
                    return c.getFirePower()>0;
                })
                .noneMatch(this::cannonPositionIsEffective));
    }

    // returns firstFoundComponent if there's an effective cannon with non-zero firepower

    @VisibleForTesting
    protected boolean cannonPositionIsEffective(Map.Entry<Point, Cannon> e) {
        return e.getValue().getOrientation() == direction && switch (direction) {
            case 0 -> e.getKey().x == diceRoll;
            case 1, 3 ->  Math.abs(e.getKey().y - diceRoll) < 2;
            case 2 ->  Math.abs(e.getKey().x - diceRoll) < 2;
            default -> throw new IllegalArgumentException("Nonexistent direction");
        };
    }
}
