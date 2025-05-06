package it.polimi.ingsw.galaxytruckers.model;

import java.util.Random;
import java.util.function.IntSupplier;

// DESCRIPTION:
// returns a number between 2 and 12, sum of two random numbers between 1 and 6

public interface Dice extends IntSupplier {
    // shared instance of Random
    Random RANDOM = new Random();

    @Override
    default int getAsInt() {
        return RANDOM.nextInt(6) + RANDOM.nextInt(6) + 2;
    }
}