package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

public final class DrawCardState extends AdventureState implements GameStateInterface{
    ShipBoard shipBoard;
    boolean hasDrawn = false;

    @Override
    public void setGame(Game game) {
        this.game = game;
        if (!game.getFlightBoard().getOrderedShips().isEmpty()) {
            this.shipBoard = game.getFlightBoard().getOrderedShips().getFirst();
        }
        game.getEventListener().notifyGameStateUpdateEvent(this);
        if (game.getLevel() == Level.SECOND) { // TODO: do not predicate directly on the type
            game.forceShipsToGiveUp();
            game.endGameIfAllShipsHaveGivenUp();
            game.getFlightBoard().removeShips(game.getGivenUpShips());
            if (!game.getGivenUpShips().isEmpty()) {
                game.getEventListener().notifySurrenderEvent(game.getGivenUpShips().stream().toList());
            }
        }

        // this stops a surrendered ship from drawing a card
        if (shipBoard != null && game.getGivenUpShips().contains(shipBoard)) {
            hasDrawn = true;
            game.setCurrentState(getNextState());
        }
    }

    @Override
    public void drawCard(ShipBoard shipBoard) {
        if (game.getGivenUpShips().contains(shipBoard)) {
            throw new IllegalStateException("Ship has surrendered and cannot draw cards");
        }

        if (!shipBoard.equals(this.shipBoard)) {
            throw new IllegalStateException("It's not your turn");
        }
        if (hasDrawn) {
            throw new IllegalStateException("It's already drawn");
        }
        hasDrawn = true;
        if(game.getDeck().tryDrawCard()) {
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
