package it.polimi.ingsw.galaxytruckers.client.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.shared.enums.Level;
import org.junit.jupiter.api.Test;

/*
* The sabotage card is an unused card in the game. This test is
* only for coverage purposes.
* */
class SabotageCardTest {
    @Test
    void constructor_doesNotThrow() {
        new SabotageCard(Level.SECOND, 1);
    }
}