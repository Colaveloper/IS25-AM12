package it.polimi.ingsw.galaxytruckers.model.state;

public final class TestShipBuildingState extends ShipBuildingState implements GameStateInterface{
    public TestShipBuildingState() {
        super();
    }

    @Override
    protected void endBuilding() {
        game.setCurrentState(new TestShipCorrectionState());
    }
}
