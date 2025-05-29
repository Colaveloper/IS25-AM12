package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;

import java.util.Map;

public class GoodsBuffer {
    private final Map<GoodsType, Integer> goodsBuffer;

    public GoodsBuffer(Map<GoodsType, Integer> goodsBuffer) {
        this.goodsBuffer = goodsBuffer;
    }

    public Map<GoodsType, Integer> getGoodsBuffer() {
        return goodsBuffer;
    }

    public boolean isEmpty() {
        return goodsBuffer.isEmpty();
    }

    public void addToBuffer(GoodsType goodsType) {
        if (!goodsBuffer.containsKey(goodsType)) {
            goodsBuffer.put(goodsType, 0);
        }
        goodsBuffer.put(goodsType, goodsBuffer.get(goodsType) + 1);
    }

    public void removeFromBuffer(GoodsType goodsType) {
        goodsBuffer.put(goodsType, goodsBuffer.get(goodsType) - 1);
        if (goodsBuffer.get(goodsType) == 0) {
            goodsBuffer.remove(goodsType);
        }
    }
}
