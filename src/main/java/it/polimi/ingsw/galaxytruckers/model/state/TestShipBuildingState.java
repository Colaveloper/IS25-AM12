package it.polimi.ingsw.galaxytruckers.model.state;

public class TestShipBuildingState extends ShipBuildingState{
    public TestShipBuildingState() {
        super();
    }

    @Override
    protected void endBuilding() {
        game.setCurrentState(new TestShipCorrectionState());
    }
}
