package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.util.HashSet;

public class ActivateStateStub extends ActivateState {
    public ActivateStateStub(ShipBoard shipBoard) {
        super(shipBoard, new HashSet<>());
    }
}
