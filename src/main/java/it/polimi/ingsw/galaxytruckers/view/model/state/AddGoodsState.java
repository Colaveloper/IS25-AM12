package it.polimi.ingsw.galaxytruckers.view.model.state;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
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
}
