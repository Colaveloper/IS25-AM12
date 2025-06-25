package it.polimi.ingsw.galaxytruckers.model;

import java.util.Random;
import java.util.function.IntSupplier;

/**
 * Represents two dice that can be rolled to get a random number between 2 and 12.
 * The number is the sum of two random integers, each ranging from 1 to 6.
 */
public interface Dice extends IntSupplier {
    // shared instance of Random
    Random RANDOM = new Random();

    @Override
    default int getAsInt() {
        return RANDOM.nextInt(6) + RANDOM.nextInt(6) + 2;
    }
}