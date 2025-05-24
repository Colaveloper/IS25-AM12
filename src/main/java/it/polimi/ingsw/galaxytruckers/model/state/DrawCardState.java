package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

public class DrawCardState extends AdventureState {
    ShipBoard leaderShipBoard;

    @Override
    public void setGame(Game game) {
        super.setGame(game);
        this.leaderShipBoard = game.getFlightBoard().getOrderedShips().getFirst();
        game.forceShipsToGiveUp();
        game.endGameIfAllShipsHaveGivenUp();
        game.getFlightBoard().removeShips(game.getGivenUpShips());
    }

    @Override
    public void drawCard(ShipBoard shipBoard) {
        if (!shipBoard.equals(this.leaderShipBoard)) {
            throw new IllegalStateException("It's not your turn");
        }
        if(game.getDeck().tryDrawCard()) {
            game.getDeck().getCurrentCard().initialize();
            game.setCurrentState(super.getNextState());
        } else {
            game.endGame();
        }
    }
}
