package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.SurrenderPolicy;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.util.List;

public final class DrawCardState extends AdventureState implements GameStateInterface {
    private ShipBoard shipBoard;
    private boolean hasDrawn = false;

    @Override
    public void setGame(Game game) {
        this.game = game;
        SurrenderPolicy surrenderPolicy = game.getSurrenderPolicy();
        if (surrenderPolicy.isSurrenderEnabled()) {
            surrenderPolicy.confirmSurrender(game.getFlightBoard());
        }
        if (game.tryEndGame()) return;
        shipBoard = game.getFlightBoard().getOrderedShips().getFirst();
        game.getEventListener().notifyGameStateUpdateEvent(this);
    }

    @Override
    public synchronized void drawCard(ShipBoard shipBoard) {
        checkIfExpired();
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
            throw new IllegalStateException("There are no more cards to draw");
        }
    }

    @Override
    public synchronized void goNext(ShipBoard shipBoard) {
        if (!shipBoard.equals(this.shipBoard)) {
            throw new IllegalStateException("It's not your turn");
        }
        checkIfExpired();
        if (!hasDrawn) {
            throw new IllegalStateException("You need to draw first");
        }
        getNextState();
    }

    public ShipBoard getShipBoard() {
        return shipBoard;
    }
}
