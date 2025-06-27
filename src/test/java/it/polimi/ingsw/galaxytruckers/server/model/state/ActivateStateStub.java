package it.polimi.ingsw.galaxytruckers.server.model.state;

import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.server.model.state.ActivateState;

import java.util.HashSet;

public class ActivateStateStub extends ActivateState {
    public ActivateStateStub(ShipBoard shipBoard) {
        super(shipBoard, new HashSet<>());
    }
}
