package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.util.function.Consumer;

public class GiveUpState extends GameState{
    ShipBoard shipBoard;
    Consumer<Boolean> chooseToGiveUpMethod;

    public GiveUpState(ShipBoard shipBoard, Consumer<Boolean> chooseToGiveUpMethod){
        this.shipBoard = shipBoard;
        this.chooseToGiveUpMethod = chooseToGiveUpMethod;
    }
    @Override
    public void giveUp(ShipBoard shipBoard, boolean giveUp){
        if (!shipBoard.equals(this.shipBoard)) {
            throw new IllegalStateException("It's not your turn");
        }
        if(game.getGivenUpShips().contains(shipBoard)){
            throw new IllegalStateException("Ship has already given up");
        }
        chooseToGiveUpMethod.accept(giveUp);
        game.setCurrentState(game.getDeck().getCurrentCard().nextStep());
    }
}
