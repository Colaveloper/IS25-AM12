package it.polimi.ingsw.galaxytruckers.server.model.state;

public final class TestShipBuildingState extends ShipBuildingState implements GameStateInterface{
    public TestShipBuildingState() {
        super();
    }

    /**
     * {@inheritDoc}
     * <p>Ends the ship building phase by finalizing all ship boards and
     * placing them on the flight board. </p>
     */
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
