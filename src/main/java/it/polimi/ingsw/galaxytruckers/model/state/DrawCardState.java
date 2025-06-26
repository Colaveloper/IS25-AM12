package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.SurrenderPolicy;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

/**
 * Represents the state of the game where a player can draw a card from the deck.
 */
public final class DrawCardState extends AdventureState implements GameStateInterface {
    private ShipBoard shipBoard;
    private boolean hasDrawn = false;

    /**
     * If it's the player's turn it skips the turn, drawing a card if it hasn't been drawn yet.
     *
     * @param shipBoard the ship board of the player who wants to skip
     */
    @Override
    public synchronized void skip(ShipBoard shipBoard) {
        if (!expired && this.shipBoard.equals(shipBoard)) {
            if (hasDrawn) {
                getNextState();
            } else {
                game.getDeck().drawCard();
                game.getDeck().getCurrentCard().initialize();
                game.getEventListener().notifyNewCardEvent(game.getDeck().getCurrentCard());
                getNextState();
            }
        }
    }

    /**
     * Initializes the state so that it's the turn of the first ship on the flight board.
     * If there are no cards left or all players have surrendered, the game ends.
     *
     * @param game the game to associate with this state
     */
    @Override
    public void setGame(Game game) {
        super.setGame(game);
        SurrenderPolicy surrenderPolicy = game.getSurrenderPolicy();
        if (surrenderPolicy.isSurrenderEnabled()) {
            surrenderPolicy.confirmSurrender(game.getFlightBoard());
        }
        if (game.tryEndGame()) {
            expired = true;
            return;
        }
        shipBoard = game.getFlightBoard().getOrderedShips().getFirst();
    }

    /**
     * Draws a card from the deck for the current ship board.
     *
     * @param shipBoard the ship board drawing the card
     * @throws IllegalStateException if it's not the player's turn or
     *                               if they have already drawn a card
     */
    @Override
    public synchronized void drawCard(ShipBoard shipBoard) {
        checkIfExpired();
        if (!this.shipBoard.equals(shipBoard)) {
            throw new IllegalStateException("It's not your turn");
        }
        if (hasDrawn) {
            throw new IllegalStateException("It's already drawn");
        }
        hasDrawn = true;
        game.getDeck().drawCard();
        game.getDeck().getCurrentCard().initialize();
        game.getEventListener().notifyNewCardEvent(game.getDeck().getCurrentCard());
    }

    /**
     * Moves to the next state of the game after the player has drawn a card.
     *
     * @param shipBoard the ship board of the player that requested the action
     * @throws IllegalStateException if it's not the player's turn or if they haven't drawn a card yet
     */
    @Override
    public synchronized void goNext(ShipBoard shipBoard) {
        if (!this.shipBoard.equals(shipBoard)) {
            throw new IllegalStateException("It's not your turn");
        }
        checkIfExpired();
        if (!hasDrawn) {
            throw new IllegalStateException("You need to draw first");
        }
        getNextState();
    }

    /**
     * @return the ship board of the player who is currently drawing a card.@
     */
    public synchronized ShipBoard getShipBoard() {
        return shipBoard;
    }

    /**
     * @return true if the player has drawn a card, false otherwise.
     */
    public synchronized boolean hasDrawn() {
        return hasDrawn;
    }
}
