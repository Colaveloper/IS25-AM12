package it.polimi.ingsw.galaxytruckers.server.model.shipBuilding;

import it.polimi.ingsw.galaxytruckers.shared.enums.GoodsType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CargoHoldTest extends ComponentTest {
    private CargoHold myNormalCargoHold;
    private CargoHold mySpecialCargoHold;

    @BeforeEach
    void setUp() {
        super.setUp();
        myNormalCargoHold = new CargoHold(myConnectors, 2, Boolean.FALSE);
        mySpecialCargoHold = new CargoHold(myConnectors, 3, Boolean.TRUE);
    }

    @Test
    void isSpecialReturnsCorrectValue() {
        assertFalse(myNormalCargoHold.getIsSpecial());
        assertTrue(mySpecialCargoHold.getIsSpecial());
    }

    @Test
    void addingSpecialGoodsToNormalCargoThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> myNormalCargoHold.addGoods(GoodsType.RED, 1));
    }

    @Test
    void addingAndRemoving() {
        assertThrows(IllegalArgumentException.class, () -> mySpecialCargoHold.removeGoods(GoodsType.BLUE, 1));
        assertThrows(IllegalArgumentException.class, () -> mySpecialCargoHold.addGoods(GoodsType.BLUE, 4));
        mySpecialCargoHold.addGoods(GoodsType.BLUE, 1);
        assertThrows(IllegalArgumentException.class, () -> mySpecialCargoHold.removeGoods(GoodsType.BLUE, 2));
        mySpecialCargoHold.removeGoods(GoodsType.BLUE, 1);
        assertTrue(mySpecialCargoHold.getGoods().isEmpty());

        mySpecialCargoHold.addGoods(GoodsType.RED, 1);
        mySpecialCargoHold.addGoods(GoodsType.GREEN, 2);
        assertEquals(1, mySpecialCargoHold.getGoods().get(GoodsType.RED));
        assertEquals(2, mySpecialCargoHold.getGoods().get(GoodsType.GREEN));
        assertEquals(2, mySpecialCargoHold.getGoods().size());
        assertThrows(IllegalArgumentException.class, () -> mySpecialCargoHold.addGoods(GoodsType.BLUE, 1));

        mySpecialCargoHold.removeGoods(GoodsType.RED, 1);
        assertFalse(mySpecialCargoHold.getGoods().containsKey(GoodsType.RED));
        mySpecialCargoHold.removeGoods(GoodsType.GREEN, 1);
        assertEquals(1, mySpecialCargoHold.getGoods().get(GoodsType.GREEN));
    }
}