package it.polimi.ingsw.galaxytruckers.model.adventureCards.penalty;

import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.GameState;
import it.polimi.ingsw.galaxytruckers.model.state.RemoveGoodsState;

import java.util.Optional;

public class GoodsLoss implements Penalty {
    private int goodsToLose;

    public GoodsLoss(int goodsToLose) {
        this.goodsToLose = goodsToLose;
    }

    @Override
    public Optional<GameState> givePenalty(ShipBoard shipBoard, FlightBoard flightBoard) {
        if (goodsToLose != 0) {
            int goodsToLose = this.goodsToLose;
            this.goodsToLose = 0;
            return Optional.of(new RemoveGoodsState(goodsToLose, shipBoard));
        } else {
            return Optional.empty();
        }
    }
}
