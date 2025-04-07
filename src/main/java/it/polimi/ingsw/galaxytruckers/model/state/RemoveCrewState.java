package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.awt.*;

public class RemoveCrewState extends GameState{
    int crewSacrifice;
    ShipBoard shipBoard;

    public RemoveCrewState(int crewSacrifice, ShipBoard shipBoard) {
        this.crewSacrifice = crewSacrifice;
        this.shipBoard = shipBoard;
    }

    @Override
    public void loseCrew(Point position) {
        if (shipBoard.getCrewSize() > 0 && crewSacrifice > 0) {
            shipBoard.loseCrew(position,1);
            crewSacrifice--;
        }
        if (crewSacrifice <= 0 || shipBoard.getCrewSize() <= 0) {
            game.setCurrentState(game.getDeck().getCurrentCard().nextStep());
        }

    }
}
