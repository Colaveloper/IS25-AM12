package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.util.Map;

public class EndGameState extends AdventureState {
    Map<ShipBoard, Integer> finalScores;

    public EndGameState(Map<ShipBoard, Integer> finalScores){
        this.finalScores = finalScores;
    }

    //TODO: remove this state
}
