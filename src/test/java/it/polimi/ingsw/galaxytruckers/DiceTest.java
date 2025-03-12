package it.polimi.ingsw.galaxytruckers;

import static org.junit.jupiter.api.Assertions.*;
class DiceTest {

    Dice dice = it.polimi.ingsw.galaxytruckers.Dice.create();
    Integer min, max;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        min = Integer.MAX_VALUE;
        max = Integer.MIN_VALUE;
    }

    @org.junit.jupiter.api.Test
    void roll() {
        for (int i = 0; i < 100; i++) {
            Integer newRoll = dice.roll();
            min = Math.min(min, newRoll);
            max = Math.max(max, newRoll);
        }
        assertTrue(min >= 2 && max <= 12);
    }
}