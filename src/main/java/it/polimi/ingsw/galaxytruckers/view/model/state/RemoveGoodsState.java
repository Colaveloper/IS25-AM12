package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public final class RemoveGoodsState extends AdventureState {
    private int goodsToLose;
    private final ShipBoard shipBoard;

    public RemoveGoodsState(int goodsToLose, ShipBoard shipBoard) {
        this.goodsToLose = goodsToLose;
        this.shipBoard = shipBoard;
    }

    @Override
    public List<StateActions> getAvailableActions() {
        List<StateActions> actions = new ArrayList<>(super.getAvailableActions());
        actions.add(StateActions.LOSE_GOOD);
        return actions;
    }

    @Override
    public void notifyRemoveGoods(ShipBoard shipBoard, Point point, GoodsType goodsType) {
        shipBoard.removeGoods(point, goodsType);
        goodsToLose--;
        game.getObservers().forEach(observer -> observer.notifyRemoveGoods(shipBoard, point, goodsType));
    }

    @Override
    public void notifyUseBattery(ShipBoard shipBoard, Point point) {
        shipBoard.useBattery(point);
        goodsToLose--;
        game.getObservers().forEach(observer -> observer.notifyUseBattery(shipBoard, point));
    }

    public int getGoodsToLose() {
        return goodsToLose;
    }

    public ShipBoard getShipBoard() {
        return shipBoard;
    }
}
