package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.util.Set;

public class DrawCardState extends GameState {
    ShipBoard shipBoard;

    @Override
    public void setGame(Game game) {
        super.setGame(game);
        //TODO: handle games with no ships left
        this.shipBoard = game.getFlightBoard().getOrderedShips().getFirst();
    }

    @Override
    public void drawCard(ShipBoard shipBoard) {
        if (!shipBoard.equals(this.shipBoard)) {
            throw new IllegalStateException("It's not your turn");
        }
        if(game.getDeck().tryDrawCard()) {
            game.getDeck().getCurrentCard().initialize(game.getFlightBoard());
            GameState nextState = game.getDeck().getCurrentCard().nextStep();
            game.forceShipsToGiveUp();
            game.setCurrentState(nextState);
        } else {
            game.endGame();
        }
    }
}
