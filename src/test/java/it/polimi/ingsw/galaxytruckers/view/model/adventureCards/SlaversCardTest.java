package it.polimi.ingsw.galaxytruckers.view.model.adventureCards;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SlaversCardTest {

    @Test
    void getFirePowerThreshold() {
        SlaversCard card = new SlaversCard(null, 2, 5, 10, 3, 1);
        assertEquals(5, card.getFirePowerThreshold());
    }

    @Test
    void getCreditPrize() {
        SlaversCard card = new SlaversCard(null, 2, 5, 10, 3, 1);
        assertEquals(10, card.getCreditPrize());
    }

    @Test
    void getFlightDaysLoss() {
        SlaversCard card = new SlaversCard(null, 2, 5, 10, 3, 1);
        assertEquals(3, card.getFlightDaysLoss());
    }

    @Test
    void getCrewLoss() {
        SlaversCard card = new SlaversCard(null, 2, 5, 10, 3, 1);
        assertEquals(2, card.getCrewLoss());
    }

    @Test
    void constructorAndGettersWithDifferentValues() {
        SlaversCard card = new SlaversCard(null, 7, 8, 15, 4, 99);
        assertEquals(8, card.getFirePowerThreshold());
        assertEquals(15, card.getCreditPrize());
        assertEquals(4, card.getFlightDaysLoss());
        assertEquals(7, card.getCrewLoss());
    }
}