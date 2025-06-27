package it.polimi.ingsw.galaxytruckers.client.model.state;

import it.polimi.ingsw.galaxytruckers.shared.enums.GoodsType;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GoodsBufferTest {
    @Test
    void testAddToBufferWhenGoodsTypeAlreadyInBuffer() {
        Map<GoodsType, Integer> initialBuffer = new java.util.HashMap<>();
        initialBuffer.put(GoodsType.RED, 2);
        GoodsBuffer buffer = new GoodsBuffer(initialBuffer);

        buffer.addToBuffer(GoodsType.RED);

        assertEquals(3, buffer.getGoodsBuffer().get(GoodsType.RED));
        assertEquals(1, buffer.getGoodsBuffer().size());
    }

    @Test
    void testRemoveFromBufferWhenQuantityDoesNotReachZero() {
        Map<GoodsType, Integer> initialBuffer = new java.util.HashMap<>();
        initialBuffer.put(GoodsType.BLUE, 3);
        GoodsBuffer buffer = new GoodsBuffer(initialBuffer);

        buffer.removeFromBuffer(GoodsType.BLUE);

        assertEquals(2, buffer.getGoodsBuffer().get(GoodsType.BLUE));
        assertEquals(1, buffer.getGoodsBuffer().size());
    }

}