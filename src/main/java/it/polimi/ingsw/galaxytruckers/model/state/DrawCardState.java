package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.SurrenderPolicy;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.util.Set;
import java.util.stream.Collectors;

public final class DrawCardState extends AdventureState implements GameStateInterface {
    ShipBoard shipBoard;
    boolean hasDrawn = false;

    @Override
    public void setGame(Game game) {
        this.game = game;
        SurrenderPolicy surrenderPolicy = game.getSurrenderPolicy();
        if (surrenderPolicy.isSurrenderEnabled()) {
            surrenderPolicy.confirmSurrender(game.getFlightBoard());
            if (game.endGameIfAllShipsHaveGivenUp()) return;
        }
        this.shipBoard = game.getFlightBoard().getOrderedShips().getFirst();
        game.getEventListener().notifyGameStateUpdateEvent(this);
    }

    @Override
    public void drawCard(ShipBoard shipBoard) {
        if (!shipBoard.equals(this.shipBoard)) {
            throw new IllegalStateException("It's not your turn");
        }
        if (hasDrawn) {
            throw new IllegalStateException("It's already drawn");
        }
        hasDrawn = true;
        if (game.getDeck().tryDrawCard()) {
            game.getDeck().getCurrentCard().initialize();
            game.getEventListener().notifyNewCardEvent(game.getDeck().getCurrentCard());
        } else {
            game.endGame();
        }
    }

    @Override
    public void goNext(ShipBoard shipBoard) {
        if (!hasDrawn) {
            throw new IllegalStateException("You need to draw first");
        }
        game.setCurrentState(getNextState());
    }

    public ShipBoard getShipBoard() {
        return shipBoard;
    }
}
