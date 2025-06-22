package it.polimi.ingsw.galaxytruckers.model.state;

public final class TestShipBuildingState extends ShipBuildingState implements GameStateInterface{
    public TestShipBuildingState() {
        super();
    }

    @Override
    protected void endBuilding() {
        synchronized (endLock) {
            if (!expired){
                expired = true;
                game.submitStateTransition(() -> {
                    game.getShipBoards().forEach(s -> {
                        s.finishBuilding();
                        if (!completedShipBoards.contains(s)) {
                            game.getFlightBoard().placeShipOnFlightBoard(s);
                        }
                    });
                    game.setCurrentState(game.getGameFactory().createShipCorrectionState());
                });
            }
        }
    }
}
