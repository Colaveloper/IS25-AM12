package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.Map;

public final class AddGoodsState extends AdventureState implements GameStateInterface{
    Map<GoodsType, Integer> goodsBuffer;
    ShipBoard shipBoard;

    public AddGoodsState(Map<GoodsType, Integer> goodsBuffer, ShipBoard shipBoard) {
        this.goodsBuffer = goodsBuffer;
        this.shipBoard = shipBoard;
    }

    @Override
    public void addGood(ShipBoard shipBoard, Point position, GoodsType good) {
        if (!shipBoard.equals(this.shipBoard)) {
            throw new IllegalStateException("It's not your turn");
        }
        if (goodsBuffer.containsKey(good) && goodsBuffer.get(good) > 0) {
            shipBoard.placeGoods(position, good, 1);
            goodsBuffer.put(good, goodsBuffer.get(good) - 1);
            game.getEventListener().notifyGoodsUpdateEvent(shipBoard,position,good,true);
        } else {
            throw new IllegalArgumentException("You don't have goods of this type to add");
        }
    }

    @Override
    public void removeGood(ShipBoard shipBoard, Point position, GoodsType good) {
        if (!shipBoard.equals(this.shipBoard)) {
            throw new IllegalStateException("It's not your turn");
        }
        shipBoard.removeGoods(position, good, 1);
        game.getEventListener().notifyGoodsUpdateEvent(shipBoard,position,good,false);
        if (goodsBuffer.containsKey(good)) {
            goodsBuffer.put(good, goodsBuffer.get(good) + 1);
        } else {
            goodsBuffer.put(good, 1);
        }
    }

    @Override
    public void goNext(ShipBoard shipBoard) {
        if (!shipBoard.equals(this.shipBoard)) {
            throw new IllegalStateException("It's not your turn");
        }
        game.setCurrentState(super.getNextState());
    }

    public ShipBoard getShipBoard() {
        return shipBoard;
    }

    public Map<GoodsType, Integer> getGoodsBuffer(){
        return goodsBuffer;
    }
}
