package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

public final class DrawCardState extends AdventureState {
    ShipBoard shipBoard;

    @Override
    public void setGame(Game game) {
        super.setGame(game);
        this.shipBoard = game.getFlightBoard().getOrderedShips().getFirst();
        if (game.getLevel() == Level.SECOND) { // TODO: do not predicate directly on the type
            game.forceShipsToGiveUp();
            game.endGameIfAllShipsHaveGivenUp();
            game.getFlightBoard().removeShips(game.getGivenUpShips());
        }
    }

    @Override
    public void drawCard(ShipBoard shipBoard) {
        if (!shipBoard.equals(this.shipBoard)) {
            throw new IllegalStateException("It's not your turn");
        }
        if(game.getDeck().tryDrawCard()) {
            game.getDeck().getCurrentCard().initialize();
            game.setCurrentState(super.getNextState());
        } else {
            game.endGame();
        }
    }

    public ShipBoard getShipBoard() {
        return shipBoard;
    }
}
