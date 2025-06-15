package it.polimi.ingsw.galaxytruckers.model;

import java.util.Arrays;

public class ScoresRegistry {
    private final int[] positionScores;
    private final int BestLookingShipAward;

    public ScoresRegistry(int[] positionScores, int bestLookingShipAward){
        this.positionScores = positionScores;
        this.BestLookingShipAward = bestLookingShipAward;
    }

    public int[] getPositionScores(){
        return Arrays.copyOf(positionScores, positionScores.length);
    }

    public int getBestLookingShipAward(){
        return BestLookingShipAward;
    }
}
