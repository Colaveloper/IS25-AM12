package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.awt.*;

public final class RemoveCrewState extends AdventureState implements GameStateInterface {
    int crewSacrifice;
    ShipBoard shipBoard;

    public RemoveCrewState(int crewSacrifice, ShipBoard shipBoard) {
        this.crewSacrifice = crewSacrifice;
        this.shipBoard = shipBoard;
    }

    @Override
    public synchronized void loseCrew(ShipBoard shipBoard, Point position) {
        if (!shipBoard.equals(this.shipBoard)) {
            throw new IllegalStateException("It's not your turn");
        }
        checkIfExpired();
        if (shipBoard.getCrewSize() > 0 && crewSacrifice > 0) {
            shipBoard.loseCrew(position);
            crewSacrifice--;
        }
        if (crewSacrifice <= 0 || shipBoard.getCrewSize() <= 0) {
            getNextState();
        }
    }

    public synchronized ShipBoard getShipBoard() {
        return shipBoard;
    }

    public synchronized int getCrewSacrifice() {
        return crewSacrifice;
    }
}
