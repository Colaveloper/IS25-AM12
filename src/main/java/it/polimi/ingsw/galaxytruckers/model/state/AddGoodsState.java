package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.util.Map;

//TODO: implement addGoods and removeGoods methods, handle state transition
public class AddGoodsState extends GameState {
    Map<GoodsType, Integer> goodsBuffer;
    ShipBoard shipBoard;

    public AddGoodsState(Map<GoodsType, Integer> goodsBuffer, ShipBoard shipBoard) {
        this.goodsBuffer = goodsBuffer;
        this.shipBoard = shipBoard;
    }

    @Override
    public GameState getNextState() {
        return null;
    }
}
