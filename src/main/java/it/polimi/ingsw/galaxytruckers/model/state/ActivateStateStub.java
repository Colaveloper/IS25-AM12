package it.polimi.ingsw.galaxytruckers.model.state;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

//WARNING: This class should be used for tests ONLY
// it is necessary to allow stubbing within the sealed game state hierarchy

@VisibleForTesting
public non-sealed class ActivateStateStub extends ActivateState {
    public ActivateStateStub(ShipBoard shipBoard) {
        super(shipBoard);
    }
}
