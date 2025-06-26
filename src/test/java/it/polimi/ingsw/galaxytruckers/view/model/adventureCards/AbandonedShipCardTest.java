package it.polimi.ingsw.galaxytruckers.view.model.adventureCards;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AbandonedShipCardTest {

    @Test
    void getFlightDaysLoss() {
        AbandonedShipCard card = new AbandonedShipCard(it.polimi.ingsw.galaxytruckers.model.enumTypes.Level.SECOND, 10, 3, 2, 42);
        assertEquals(2, card.getFlightDaysLoss());
    }

    @Test
    void getCreditPrize() {
        AbandonedShipCard card = new AbandonedShipCard(it.polimi.ingsw.galaxytruckers.model.enumTypes.Level.SECOND, 20, 4, 1, 43);
        assertEquals(20, card.getCreditPrize());
    }

    @Test
    void getRequiredCrew() {
        AbandonedShipCard card = new AbandonedShipCard(it.polimi.ingsw.galaxytruckers.model.enumTypes.Level.SECOND, 30, 5, 0, 44);
        assertEquals(5, card.getRequiredCrew());
    }

    @Test
    void constructorAndSuperClass() {
        AbandonedShipCard card = new AbandonedShipCard(it.polimi.ingsw.galaxytruckers.model.enumTypes.Level.SECOND, 15, 2, 3, 99);
        assertEquals(it.polimi.ingsw.galaxytruckers.model.enumTypes.Level.SECOND, card.getCardLevel());
        assertEquals(99, card.getId());
    }
}