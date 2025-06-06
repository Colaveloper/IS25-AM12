package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public final class DeclareEnginePowerState extends ActivateState {

    public DeclareEnginePowerState(ShipBoard shipBoard, boolean isMyTurn) {
        super(shipBoard, shipBoard.getEngines().keySet().stream()
                .filter(p -> shipBoard.getActivatables().containsKey(p))
                .collect(Collectors.toSet()), isMyTurn);
    }
}
