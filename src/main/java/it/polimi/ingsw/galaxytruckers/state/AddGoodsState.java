package it.polimi.ingsw.galaxytruckers.state;

import it.polimi.ingsw.galaxytruckers.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;

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
