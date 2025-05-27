package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public final class DeclareFirePowerState extends ActivateState {
    public DeclareFirePowerState(ShipBoard shipBoard) {
        super(shipBoard, shipBoard.getCannons().keySet().stream()
                .filter(p -> shipBoard.getActivatables().containsKey(p))
                .collect(Collectors.toSet()));
    }
}
