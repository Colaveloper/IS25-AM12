package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.awt.*;

public final class RemoveCrewState extends AdventureState {
    int crewSacrifice;
    ShipBoard shipBoard;

    public RemoveCrewState(int crewSacrifice, ShipBoard shipBoard) {
        this.crewSacrifice = crewSacrifice;
        this.shipBoard = shipBoard;
    }

    @Override
    public void loseCrew(ShipBoard shipBoard, Point position) {
        if (!shipBoard.equals(this.shipBoard)) {
            throw new IllegalStateException("It's not your turn");
        }
        if (shipBoard.getCrewSize() > 0 && crewSacrifice > 0) {
            shipBoard.loseCrew(position,1);
            game.getEventListener().notifyLoseCrewEvent(shipBoard, position);
            crewSacrifice--;
        }
        if (crewSacrifice <= 0 || shipBoard.getCrewSize() <= 0) {
            game.setCurrentState(getNextState());
        }
    }

    public ShipBoard getShipBoard() {
        return shipBoard;
    }

    public int getCrewSacrifice() {
        return crewSacrifice;
    }
}
