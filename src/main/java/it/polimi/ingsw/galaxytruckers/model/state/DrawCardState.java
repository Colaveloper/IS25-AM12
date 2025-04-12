package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.Deck;
import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

//TODO : determine a way to handle game end
public class DrawCardState extends GameState {
    ShipBoard shipBoard;

    @Override
    public void setGame(Game game) {
        super.setGame(game);
        this.shipBoard = game.getFlightBoard().getOrderedShips().getFirst();
    }

    @Override
    public void drawCard(ShipBoard shipBoard, Deck deck) {
        if (!shipBoard.equals(this.shipBoard)) {
            throw new IllegalStateException("It's not your turn");
        }
        if(game.getDeck().tryDrawCard()) {
            game.setCurrentState(game.getDeck().getCurrentCard().nextStep());
        } else {
            // TODO: game over, compute scores and show them to players
        }
    }
}
