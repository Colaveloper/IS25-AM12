package it.polimi.ingsw.galaxytruckers.server.model.state;

import it.polimi.ingsw.galaxytruckers.server.model.SurrenderPolicy;
import it.polimi.ingsw.galaxytruckers.shared.enums.SurrenderCause;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.ShipBoard;

/**
 * Abstract class representing a state in the adventure phase of the game.
 * It extends GameState and provides methods to handle state transitions and surrender actions.
 */
public abstract class AdventureState extends GameState {

    /**
     * Checks if the state has expired because a transition has already been submitted.
     * @throws IllegalStateException if the state has expired
     */
    protected void checkIfExpired() {
        if (expired) throw new IllegalStateException("It's too late to take this action");
    }

    /**
     * {@inheritDoc}
     * <p>Calls {@link SurrenderPolicy#requestSurrender(ShipBoard, SurrenderCause)}</p>
     * on the game's surrender policy if surrender is enabled.
     * @param shipBoard the ship board of the player giving up
     * @throws UnsupportedOperationException if surrender is not enabled in the game
     */
    @Override
    public void giveUp(ShipBoard shipBoard) {
        SurrenderPolicy surrenderPolicy = game.getSurrenderPolicy();
        if (surrenderPolicy.isSurrenderEnabled()) {
            if (!surrenderPolicy.requestSurrender(shipBoard, SurrenderCause.REQUEST))
                throw new IllegalStateException("You have already surrendered");
        } else {
            throw new UnsupportedOperationException("You cannot give up in this game");
        }
    }

    /**
     * Submits a state transition to the game, setting the current state to the next state
     * of the current card.
     */
    protected synchronized void getNextState() {
        if (!expired) {
            game.submitStateTransition(() ->
                    game.setCurrentState(game.getDeck().getCurrentCard().getNextState()));
            expired = true;
        }
    }
}
