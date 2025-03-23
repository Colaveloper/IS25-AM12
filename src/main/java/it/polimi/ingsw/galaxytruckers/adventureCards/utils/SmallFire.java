package it.polimi.ingsw.galaxytruckers.adventureCards.utils;

import it.polimi.ingsw.galaxytruckers.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.shipBuilding.Connector;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class SmallFire extends Projectile {
    public SmallFire(int direction) {
        super(direction);
    }

    @Override
    public boolean fireAt(ShipBoard shipBoard) {
        if (shipBoard.getShieldDirections()[direction]) { // protected
            return false;
        } else {
            Optional<Component> hitComponent = super.getHitComponent(shipBoard);
            hitComponent.ifPresent(shipBoard::remove);
            return hitComponent.isPresent();
        }
    }

    @Override
    public Set<Point> getActivatablePoints(ShipBoard shipBoard) {
        return shipBoard.getShields().keySet().stream()
                .filter(shipBoard.getActivatables().keySet()::contains)
                .collect(Collectors.toSet());
    }
}
