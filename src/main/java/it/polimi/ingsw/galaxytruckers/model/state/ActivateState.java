package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.Set;

public class ActivateState extends GameState{
    Set<Point> availablePositions;
    ShipBoard shipBoard;

    public ActivateState(Set<Point> availablePositions, ShipBoard shipBoard) {
        this.availablePositions = availablePositions;
        this.shipBoard = shipBoard;
    }

    @Override
    public void activateComponent(ShipBoard shipBoard, Point position) {
        if (availablePositions.contains(position)) {
            shipBoard.activateComponent(position);
        }
    }

    @Override
    public GameState getNextState() {
        return adventureCard.nextStep();
    }
}
