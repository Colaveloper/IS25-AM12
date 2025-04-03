package it.polimi.ingsw.galaxytruckers.model.adventureCards.projectiles;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.Optional;
import java.util.Set;
import java.util.function.IntSupplier;

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
