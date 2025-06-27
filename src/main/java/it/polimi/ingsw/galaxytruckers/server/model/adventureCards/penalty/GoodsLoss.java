package it.polimi.ingsw.galaxytruckers.server.model.adventureCards.penalty;

import it.polimi.ingsw.galaxytruckers.server.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.server.model.state.AdventureState;
import it.polimi.ingsw.galaxytruckers.server.model.state.RemoveGoodsState;

import java.util.Optional;

/**
 * Class representing a penalty that causes the player to lose a certain amount of goods.
 * Implements the Penalty interface.
 */
public class GoodsLoss implements Penalty {
    private int goodsToLose;

    public GoodsLoss(int goodsToLose) {
        this.goodsToLose = goodsToLose;
    }

    /**
     * Inflicts a penalty by requiring the player to remove a specified number of goods from their ship.
     * If goodsToLose is zero, no penalty is applied.
     *
     * @param shipBoard the ShipBoard of the player being penalized
     * @param flightBoard the FlightBoard containing all players' ship boards
     * @return an Optional containing a RemoveGoodsState if goods are to be removed, or empty if not
     */
    @Override
    public Optional<AdventureState> inflictPenalty(ShipBoard shipBoard, FlightBoard flightBoard) {
        if (goodsToLose != 0) {
            int goodsToLose = this.goodsToLose;
            this.goodsToLose = 0;
            return Optional.of(new RemoveGoodsState(goodsToLose, shipBoard));
        } else {
            return Optional.empty();
        }
    }
}
