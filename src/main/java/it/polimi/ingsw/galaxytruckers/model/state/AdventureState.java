package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.SurrenderPolicy;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.SurrenderCause;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

public abstract class AdventureState extends GameState {
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

    public GameState getNextState() {
        return game.getDeck().getCurrentCard().getNextState();
    }
}
