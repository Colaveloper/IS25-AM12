package it.polimi.ingsw.galaxytruckers.model;

import java.util.Arrays;

/**
 * This class is used to store the scores assigned at the end of the game.
 * It includes methods to retrieve the scores for final positions on the flight board
 * and for the Best Looking Ship award.
 */
public class ScoresRegistry {
    private final int[] positionScores;
    private final int BestLookingShipAward;

    /**
     * Constructor for ScoresRegistry.
     *
     * @param positionScores an array of integers representing the scores for each position on the flight board
     * @param bestLookingShipAward an integer representing the score for the Best Looking Ship award
     */
    public ScoresRegistry(int[] positionScores, int bestLookingShipAward){
        this.positionScores = positionScores;
        this.BestLookingShipAward = bestLookingShipAward;
    }

    /**
     * @return an array of integers representing the scores for each position on the flight board
     */
    public int[] getPositionScores(){
        return Arrays.copyOf(positionScores, positionScores.length);
    }

    /**
     * @return the score for the Best Looking Ship award
     */
    public int getBestLookingShipAward(){
        return BestLookingShipAward;
    }
}
