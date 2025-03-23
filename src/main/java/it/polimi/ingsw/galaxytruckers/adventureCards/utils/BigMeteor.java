package it.polimi.ingsw.galaxytruckers.adventureCards.utils;

import it.polimi.ingsw.galaxytruckers.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.shipBuilding.Connector;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class BigMeteor extends Projectile {
    public BigMeteor(int direction) {
        super(direction);
    }

    @Override
    public boolean fireAt(ShipBoard shipBoard) {

        Optional<Component> hitComponent = super.getHitComponent(shipBoard);
        if (hitComponent.isEmpty()) return false;

        // not active double cannons
        Set<Point> notActiveDoubleCannons = shipBoard.getActivatables().entrySet().stream()
                .filter(e -> !e.getValue().isActive())
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        boolean isDefended = shipBoard.getCannons().entrySet().stream()
                .filter(e -> e.getValue().getOrientation() == direction)
                .filter(e -> !notActiveDoubleCannons.contains(e.getKey()))
                .anyMatch(e -> {
                    return switch (direction) {
                        case 0 -> e.getKey().x == diceRoll;
                        case 1, 3 ->  Math.abs(e.getKey().y - diceRoll) < 2;
                        case 2 ->  Math.abs(e.getKey().x - diceRoll) < 2;
                        default -> throw new IllegalArgumentException("Nonexistent direction");
                    };
                });

        if (isDefended) return false;

        shipBoard.remove(hitComponent.get());
        return true;
    }

    @Override
    public Set<Point> getActivatablePoints(ShipBoard shipBoard) {
        return shipBoard.getCannons().keySet().stream()
                .filter(shipBoard.getActivatables().keySet()::contains)
                .collect(Collectors.toSet());
    }
}
