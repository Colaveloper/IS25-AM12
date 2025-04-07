package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.awt.*;
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
    public void addGood(Point position, GoodsType good) {
        if (goodsBuffer.containsKey(good) && goodsBuffer.get(good) > 0) {
            shipBoard.placeGoods(position, good, 1);
            goodsBuffer.put(good, goodsBuffer.get(good) - 1);
        } else {
            throw new IllegalArgumentException("You don't have goods of this type to add");
        }
    }

    @Override
    public void removeGood(Point position, GoodsType good) {
        shipBoard.removeGoods(position, good, 1);
        if (goodsBuffer.containsKey(good)) {
            goodsBuffer.put(good, goodsBuffer.get(good) + 1);
        } else {
            goodsBuffer.put(good, 1);
        }
    }

    @Override
    public void goNext() {
        game.setCurrentState(game.getDeck().getCurrentCard().nextStep());
    }

    //    @Override
//    public GameState getNextState() {
//        return null;
//    }
}
