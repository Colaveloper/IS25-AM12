package it.polimi.ingsw.galaxytruckers.adventureCards.projectiles;

import it.polimi.ingsw.galaxytruckers.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.shipBuilding.Connector;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;

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
    protected Optional<Component> getComponentToRemove(ShipBoard shipBoard) {
        if (shipBoard.getShieldDirections()[direction]) {
            return Optional.empty();
        }
        return getFirstFoundComponent(shipBoard)
                .filter(c -> c.getConnectors().get((direction + 2) % 4) != Connector.NONE);
    }
}
