package it.polimi.ingsw.galaxytruckers.client.model.state;

import it.polimi.ingsw.galaxytruckers.shared.enums.GoodsType;

import java.util.Map;

/**
 * Represents a buffer for managing goods in the Galaxy Truckers game.
 * This class maintains a mapping of goods types to their quantities,
 * allowing goods to be added to or removed from the buffer during gameplay.
 * It's primarily used in the AddGoodsState to track available goods for placement.
 */
public class GoodsBuffer {
    /** Map containing the goods types and their quantities in the buffer */
    private final Map<GoodsType, Integer> goodsBuffer;

    /**
     * Creates a new GoodsBuffer with the specified initial contents.
     *
     * @param goodsBuffer Initial map of goods types to quantities
     */
    public GoodsBuffer(Map<GoodsType, Integer> goodsBuffer) {
        this.goodsBuffer = goodsBuffer;
    }

    /**
     * Gets the current goods buffer mapping.
     *
     * @return Map of goods types to their quantities
     */
    public Map<GoodsType, Integer> getGoodsBuffer() {
        return goodsBuffer;
    }

    /**
     * Checks if the goods buffer is empty.
     *
     * @return true if there are no goods in the buffer, false otherwise
     */
    public boolean isEmpty() {
        return goodsBuffer.isEmpty();
    }

    /**
     * Adds one unit of the specified goods type to the buffer.
     * If the goods type is not already in the buffer, it will be added with a quantity of 1.
     * Otherwise, the existing quantity will be incremented by 1.
     *
     * @param goodsType The type of goods to add to the buffer
     */
    public void addToBuffer(GoodsType goodsType) {
        if (!goodsBuffer.containsKey(goodsType)) {
            goodsBuffer.put(goodsType, 0);
        }
        goodsBuffer.put(goodsType, goodsBuffer.get(goodsType) + 1);
    }

    /**
     * Removes one unit of the specified goods type from the buffer.
     * Decrements the quantity of the goods type by 1. If the quantity becomes zero,
     * the goods type is removed from the buffer entirely.
     *
     * @param goodsType The type of goods to remove from the buffer
     */
    public void removeFromBuffer(GoodsType goodsType) {
        goodsBuffer.put(goodsType, goodsBuffer.get(goodsType) - 1);
        if (goodsBuffer.get(goodsType) == 0) {
            goodsBuffer.remove(goodsType);
        }
    }
}
