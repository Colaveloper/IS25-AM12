package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public final class DeclareEnginePowerState extends ActivateState {

    public DeclareEnginePowerState(ShipBoard shipBoard, Set<Point> availablePoints) {
        super(shipBoard, availablePoints);
    }
}
