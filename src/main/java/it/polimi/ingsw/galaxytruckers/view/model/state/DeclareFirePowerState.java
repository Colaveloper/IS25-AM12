package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class DeclareFirePowerState extends ActivateState {
    public DeclareFirePowerState(ShipBoard shipBoard, Set<Point> availablePositions) {
        super(shipBoard, availablePositions);
    }
}
