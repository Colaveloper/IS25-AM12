package it.polimi.ingsw.galaxytruckers.view.model.adventureCards.penalty;

import it.polimi.ingsw.galaxytruckers.view.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;
import it.polimi.ingsw.galaxytruckers.view.model.state.RemoveGoodsState;

import java.util.Optional;

public final class GoodsLoss implements Penalty {
    private int goodsToLose;

    public GoodsLoss(int goodsToLose) {
        this.goodsToLose = goodsToLose;
    }
}
