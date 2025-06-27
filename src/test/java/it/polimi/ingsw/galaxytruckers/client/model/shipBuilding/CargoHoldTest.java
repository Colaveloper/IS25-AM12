package it.polimi.ingsw.galaxytruckers.client.model.shipBuilding;

import it.polimi.ingsw.galaxytruckers.shared.enums.GoodsType;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.Connector;
import it.polimi.ingsw.galaxytruckers.shared.enums.Direction;

import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CargoHoldTest {
    private CargoHold createCargoHold(boolean isSpecial, int size) {
        Map<Direction, Connector> connectors = new EnumMap<>(Direction.class);
        connectors.put(Direction.UP, Connector.SINGLE);
        connectors.put(Direction.DOWN, Connector.DOUBLE);
        return new CargoHold(connectors, 1, isSpecial, size);
    }

    @Test
    void getSize() {
        CargoHold hold = createCargoHold(false, 5);
        assertEquals(5, hold.getSize());
    }

    @Test
    void isSpecial() {
        CargoHold hold1 = createCargoHold(true, 3);
        CargoHold hold2 = createCargoHold(false, 3);
        assertTrue(hold1.isSpecial());
        assertFalse(hold2.isSpecial());
    }

    @Test
    void getGoodsEmpty() {
        CargoHold hold = createCargoHold(false, 4);
        assertTrue(hold.getGoods().isEmpty());
    }

    @Test
    void addGoodsNewType() {
        CargoHold hold = createCargoHold(false, 4);
        hold.addGoods(GoodsType.RED);
        Map<GoodsType, Integer> goods = hold.getGoods();
        assertEquals(1, goods.get(GoodsType.RED));
        assertEquals(1, goods.size());
    }

    @Test
    void addGoodsExistingType() {
        CargoHold hold = createCargoHold(false, 4);
        hold.addGoods(GoodsType.RED);
        hold.addGoods(GoodsType.RED);
        assertEquals(2, hold.getGoods().get(GoodsType.RED));
    }

    @Test
    void addGoodsMultipleTypes() {
        CargoHold hold = createCargoHold(false, 4);
        hold.addGoods(GoodsType.RED);
        hold.addGoods(GoodsType.BLUE);
        assertEquals(1, hold.getGoods().get(GoodsType.RED));
        assertEquals(1, hold.getGoods().get(GoodsType.BLUE));
        assertEquals(2, hold.getGoods().size());
    }

    @Test
    void removeGoodsExistingType() {
        CargoHold hold = createCargoHold(false, 4);
        hold.addGoods(GoodsType.YELLOW);
        hold.addGoods(GoodsType.YELLOW);
        hold.removeGoods(GoodsType.YELLOW);
        assertEquals(1, hold.getGoods().get(GoodsType.YELLOW));
    }

    @Test
    void removeGoodsToZero() {
        CargoHold hold = createCargoHold(false, 4);
        hold.addGoods(GoodsType.GREEN);
        hold.removeGoods(GoodsType.GREEN);
        assertFalse(hold.getGoods().containsKey(GoodsType.GREEN));
    }

    @Test
    void removeGoodsNonExistentType() {
        CargoHold hold = createCargoHold(false, 4);
        hold.removeGoods(GoodsType.BLUE); // Should not throw
        assertTrue(hold.getGoods().isEmpty());
    }

    @Test
    void setGoodsReplacesInventory() {
        CargoHold hold = createCargoHold(false, 4);
        hold.addGoods(GoodsType.RED);
        Map<GoodsType, Integer> newGoods = new HashMap<>();
        newGoods.put(GoodsType.BLUE, 2);
        hold.setGoods(newGoods);
        Map<GoodsType, Integer> goods = hold.getGoods();
        assertEquals(1, goods.size());
        assertEquals(2, goods.get(GoodsType.BLUE));
        assertFalse(goods.containsKey(GoodsType.RED));
    }

    @Test
    void getGoodsReturnsCopy() {
        CargoHold hold = createCargoHold(false, 4);
        hold.addGoods(GoodsType.RED);
        Map<GoodsType, Integer> goods = hold.getGoods();
        goods.put(GoodsType.BLUE, 99);
        assertFalse(hold.getGoods().containsKey(GoodsType.BLUE));
    }

    @Test
    void setGoodsEmptyMap() {
        CargoHold hold = createCargoHold(false, 4);
        hold.addGoods(GoodsType.RED);
        hold.setGoods(new HashMap<>());
        assertTrue(hold.getGoods().isEmpty());
    }
}