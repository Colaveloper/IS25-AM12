package it.polimi.ingsw.galaxytruckers.view.model.adventureCards.projectiles;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Connector;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.enums.ProjectileType;

import java.awt.*;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.IntSupplier;
import java.util.stream.Collectors;

public class SmallMeteor extends Projectile {
    public SmallMeteor(IntSupplier dice, int direction) {
        super(dice, direction);
    }

    public SmallMeteor(int direction) {
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
        return getFirstFoundComponentPosition(shipBoard)
                .filter(c -> shipBoard.getComponentMap().get(c).getConnectors().get((direction + 2) % 4) != Connector.NONE);
    }

    @Override
    public ProjectileType getProjectileType() {
        return ProjectileType.SMALLMETEOR;
    }
}
