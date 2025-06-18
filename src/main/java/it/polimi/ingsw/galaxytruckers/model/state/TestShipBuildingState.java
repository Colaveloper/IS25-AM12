package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

public final class TestShipBuildingState extends ShipBuildingState implements GameStateInterface{
    public TestShipBuildingState() {
        super();
    }

    @Override
    protected void endBuilding() {
        game.submitStateTransition(() -> {
            game.getShipBoards().forEach(ShipBoard::finishBuilding);
            game.setCurrentState(game.getGameFactory().createShipCorrectionState());
        });
    }
}
