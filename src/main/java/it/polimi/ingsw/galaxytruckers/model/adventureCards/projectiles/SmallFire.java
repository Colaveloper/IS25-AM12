package it.polimi.ingsw.galaxytruckers.model.adventureCards.projectiles;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.viewEnums.ProjectileType;

import java.awt.*;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.IntSupplier;
import java.util.stream.Collectors;

public class SmallFire extends Projectile {
    public SmallFire(IntSupplier dice, int direction) {
        super(dice, direction);
    }

    public SmallFire(int direction) {
        super(direction);
    }

    @Override
    public Set<Point> getActivatablePoints(ShipBoard shipBoard) {
        return shipBoard.getShields().entrySet().stream()
                .filter(e -> Arrays.stream(e.getValue().getDefensibleDirections()).anyMatch(d -> d == direction))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    @Override
    protected Optional<Point> getComponentPositionToRemove(ShipBoard shipBoard) {
        if (shipBoard.getShieldDirections()[direction]) {
            return Optional.empty();
        }
        return getFirstFoundComponentPosition(shipBoard);
    }

    @Override
    public ProjectileType getProjectileType() {
        return ProjectileType.SMALLFIRE;
    }
}
