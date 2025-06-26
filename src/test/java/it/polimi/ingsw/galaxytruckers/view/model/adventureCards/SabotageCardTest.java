package it.polimi.ingsw.galaxytruckers.view.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
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