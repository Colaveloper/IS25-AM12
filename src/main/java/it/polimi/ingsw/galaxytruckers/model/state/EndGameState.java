package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.util.Map;

public final class EndGameState extends AdventureState {
    Map<ShipBoard, Integer> finalScores;

    public EndGameState(Map<ShipBoard, Integer> finalScores){
        this.finalScores = finalScores;
    }

    @Override
    public void endGame(){
        //TODO: notify at game end (state unnecessary)
        game.getEventListener().notifyGameEndEvent(finalScores);
    }
}
