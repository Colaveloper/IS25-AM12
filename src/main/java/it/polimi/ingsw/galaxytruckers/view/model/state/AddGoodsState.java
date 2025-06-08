package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class AddGoodsState extends AdventureState {
    private final GoodsBuffer goodsBuffer;
    private final ShipBoard shipBoard;
    private final boolean isMyTurn;

    public AddGoodsState(ShipBoard myShip, Map<GoodsType, Integer> goodsBuffer, ShipBoard shipBoard) {
        this.myShip = myShip;
        this.isMyTurn = shipBoard.equals(myShip);
        this.goodsBuffer = new GoodsBuffer(goodsBuffer);
        this.shipBoard = shipBoard;
    }

    @Override
    public List<StateActions> getAvailableActions() {
        List<StateActions> actions = new ArrayList<>();
        if(!isMyTurn) {
            return super.getAvailableActions();
        }
        if (!goodsBuffer.isEmpty()) {
            actions.add(StateActions.ADD_GOOD);
        }
        actions.add(StateActions.REMOVE_GOOD);
        actions.add(StateActions.GO_NEXT);
        actions.addAll(super.getAvailableActions());
        return actions;
    }

    @Override
    public void notifyPlaceGoods(ShipBoard shipBoard, Point point, GoodsType goodsType) {
        goodsBuffer.removeFromBuffer(goodsType);
        game.getObservers().forEach(observer -> observer.notifyPlaceGoods(shipBoard, point, goodsType));
    }

    @Override
    public void notifyRemoveGoods(ShipBoard shipBoard, Point point, GoodsType goodsType) {
        goodsBuffer.addToBuffer(goodsType);
        game.getObservers().forEach(observer -> observer.notifyRemoveGoods(shipBoard, point, goodsType));
    }

    public GoodsBuffer getGoodsBuffer() {
        return goodsBuffer;
    }

    public ShipBoard getShipBoard() {
        return shipBoard;
    }
}
