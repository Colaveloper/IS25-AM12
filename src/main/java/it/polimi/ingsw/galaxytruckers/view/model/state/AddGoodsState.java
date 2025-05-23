package it.polimi.ingsw.galaxytruckers.view.model.state;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddGoodsState extends AdventureState {
    private final Map<GoodsType, Integer> goodsBuffer;
    private final ShipBoard shipBoard;

    public AddGoodsState(Map<GoodsType, Integer> goodsBuffer, ShipBoard shipBoard) {
        this.goodsBuffer = goodsBuffer;
        this.shipBoard = shipBoard;
    }

    @Override
    public List<StateActions> getAvailableActions() {
        List<StateActions> actions = new ArrayList<>();
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
        shipBoard.placeGoods(point, goodsType);
        removeFromBuffer(goodsType);
    }

    @Override
    public void notifyRemoveGoods(ShipBoard shipBoard, Point point, GoodsType goodsType) {
        shipBoard.removeGoods(point, goodsType);
        addToBuffer(goodsType);
    }

    private void addToBuffer(GoodsType goodsType) {
        if (!goodsBuffer.containsKey(goodsType)) {
            goodsBuffer.put(goodsType, 0);
        }
        goodsBuffer.put(goodsType, goodsBuffer.get(goodsType) + 1);
    }

    private void removeFromBuffer(GoodsType goodsType) {
        goodsBuffer.put(goodsType, goodsBuffer.get(goodsType) - 1);
        if (goodsBuffer.get(goodsType) == 0) {
            goodsBuffer.remove(goodsType);
        }
    }
}
