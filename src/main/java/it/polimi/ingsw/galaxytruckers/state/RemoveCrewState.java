package it.polimi.ingsw.galaxytruckers.state;

import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;

public class RemoveCrewState extends GameState{
    int crewSacrifice;
    ShipBoard shipBoard;

    public RemoveCrewState(int crewSacrifice, ShipBoard shipBoard) {
        this.crewSacrifice = crewSacrifice;
        this.shipBoard = shipBoard;
    }

    @Override
    public GameState getNextState() {
        return adventureCard.nextStep();
    }
}
