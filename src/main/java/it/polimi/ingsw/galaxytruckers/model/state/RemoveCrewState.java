package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.HashSet;
import java.util.Iterator;

public final class RemoveCrewState extends AdventureState implements GameStateInterface {
    int crewSacrifice;
    ShipBoard shipBoard;

    public RemoveCrewState(int crewSacrifice, ShipBoard shipBoard) {
        this.crewSacrifice = crewSacrifice;
        this.shipBoard = shipBoard;
    }

    @Override
    public void setGame(Game game) {
        super.setGame(game);
        if (shipBoard.getCrewSize() == 0) getNextState();
    }

    @Override
    public void skip(ShipBoard shipBoard) {
        if (!expired && this.shipBoard.equals(shipBoard)) {
            Iterator<Point> positions = new HashSet<>(shipBoard.getCabins().keySet()).iterator();
            Point currentPosition = positions.next();
            while (crewSacrifice > 0) {
                try {
                    shipBoard.loseCrew(currentPosition);
                } catch (IllegalStateException e) {
                    if (positions.hasNext()) currentPosition = positions.next();
                    else {
                        getNextState();
                        return;
                    }
                }
            }
        }
    }

    @Override
    public synchronized void loseCrew(ShipBoard shipBoard, Point position) {
        if (!this.shipBoard.equals(shipBoard)) {
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
