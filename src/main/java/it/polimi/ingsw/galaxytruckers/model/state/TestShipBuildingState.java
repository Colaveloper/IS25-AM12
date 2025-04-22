package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

public class TestShipBuildingState extends ShipBuildingState{
    public TestShipBuildingState() {
        super();
    }

    @Override
    protected void endBuilding() {
        game.setCurrentState(new ShipCorrectionState()); //TODO: set TestShipCorrectionState
    }
}
