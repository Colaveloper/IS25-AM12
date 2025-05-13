package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.util.Set;

public class DrawCardState extends AdventureState {
    ShipBoard shipBoard;

    @Override
    public void setGame(Game game) {
        super.setGame(game);
        this.shipBoard = game.getFlightBoard().getOrderedShips().getFirst();
        game.forceShipsToGiveUp();
        game.endGameIfAllShipsHaveGivenUp();
    }

    @Override
    public void drawCard(ShipBoard shipBoard) {
        if (!shipBoard.equals(this.shipBoard)) {
            throw new IllegalStateException("It's not your turn");
        }
        if(game.getDeck().tryDrawCard()) {
            game.getDeck().getCurrentCard().initialize();
            GameState nextState = game.getDeck().getCurrentCard().nextStep();
        } else {
            game.endGame();
        }
    }
}
