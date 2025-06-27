package it.polimi.ingsw.galaxytruckers.client.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.shared.enums.GoodsType;
import it.polimi.ingsw.galaxytruckers.shared.enums.Level;
import java.util.EnumMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AbandonedStationCardTest {
    private AbandonedStationCard createSampleCard() {
        Map<GoodsType, Integer> goodsPrize = new EnumMap<>(GoodsType.class);
        goodsPrize.put(GoodsType.RED, 2);
        goodsPrize.put(GoodsType.BLUE, 1);
        return new AbandonedStationCard(Level.FIRST, goodsPrize, 3, 2, 42);
    }

    @Test
    void getFlightDaysLoss() {
        AbandonedStationCard card = createSampleCard();
        assertEquals(2, card.getFlightDaysLoss());
    }

    @Test
    void getGoodsPrize() {
        AbandonedStationCard card = createSampleCard();
        Map<GoodsType, Integer> goodsPrize = card.getGoodsPrize();
        assertEquals(2, goodsPrize.get(GoodsType.RED));
        assertEquals(1, goodsPrize.get(GoodsType.BLUE));
        assertFalse(goodsPrize.containsKey(GoodsType.YELLOW));
    }

    @Test
    void getRequiredCrew() {
        AbandonedStationCard card = createSampleCard();
        assertEquals(3, card.getRequiredCrew());
    }

    @Test
    void testInheritanceAndId() {
        AbandonedStationCard card = createSampleCard();
        assertEquals(Level.FIRST, card.getCardLevel());
        assertEquals(42, card.getId());
    }
}