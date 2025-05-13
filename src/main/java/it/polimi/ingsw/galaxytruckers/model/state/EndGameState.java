package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.util.Map;

public class EndGameState extends AdventureState {
    Map<ShipBoard, Integer> finalScores;

    public EndGameState(Map<ShipBoard, Integer> finalScores){
        this.finalScores = finalScores;
    }

    @Override
    public void endGame(){
        // TODO: send final scores to clients via controller
    }
}
