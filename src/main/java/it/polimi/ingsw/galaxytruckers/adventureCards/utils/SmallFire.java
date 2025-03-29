package it.polimi.ingsw.galaxytruckers.adventureCards.utils;

import it.polimi.ingsw.galaxytruckers.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;

import java.awt.*;
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
        return shipBoard.getShields().keySet();
    }

    @Override
    protected Optional<Component> getComponentToRemove(ShipBoard shipBoard) {
        if (shipBoard.getShieldDirections()[direction]) {
            return Optional.empty();
        }
        return getFirstFoundComponent(shipBoard);
    }
}
