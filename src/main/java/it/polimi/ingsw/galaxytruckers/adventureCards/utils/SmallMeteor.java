package it.polimi.ingsw.galaxytruckers.adventureCards.utils;

import it.polimi.ingsw.galaxytruckers.Dice;
import it.polimi.ingsw.galaxytruckers.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.shipBuilding.Connector;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;

import java.awt.*;
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
    public boolean fireAt(ShipBoard shipBoard) {
        if (shipBoard.getShieldDirections()[direction]) {
            return false;
        }
        Optional<Component> hitComponent = super.getHitComponent(shipBoard);
        if (hitComponent.isEmpty() || hitComponent.get().getConnectors().get((direction+2)%4) == Connector.NONE) {
            return false;
        }
        shipBoard.remove(hitComponent.get());
        return true;
    }

    @Override
    public Set<Point> getActivatablePoints(ShipBoard shipBoard) {
        return shipBoard.getShields().keySet().stream()
                .filter(shipBoard.getActivatables().keySet()::contains)
                .collect(Collectors.toSet());
    }
}
