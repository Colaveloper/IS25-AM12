package it.polimi.ingsw.galaxytruckers.view.model.adventureCards.penalty;

import it.polimi.ingsw.galaxytruckers.view.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;
import it.polimi.ingsw.galaxytruckers.view.model.state.RemoveGoodsState;

import java.util.Optional;

/**
 * Represents a penalty that causes the loss of goods in the game.
 * Implements the {@link Penalty} interface.
 */
public final class GoodsLoss implements Penalty {
    private int goodsToLose;

    /**
     * Constructs a GoodsLoss penalty with the specified number of goods to lose.
     *
     * @param goodsToLose the number of goods to lose
     */
    public GoodsLoss(int goodsToLose) {
        this.goodsToLose = goodsToLose;
    }
}
