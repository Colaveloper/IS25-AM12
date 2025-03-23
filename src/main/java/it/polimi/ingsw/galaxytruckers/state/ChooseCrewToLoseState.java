package it.polimi.ingsw.galaxytruckers.state;

import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.Set;
import java.util.stream.Collectors;

public class ChooseCrewToLoseState extends GameState {
    private final Set<Point> availablePositions;
    private final ShipBoard shipBoard;

    public ChooseCrewToLoseState(ShipBoard shipBoard) {
        this.availablePositions = shipBoard.getCabins().entrySet().stream()
                .filter(e -> e.getValue().getNumResidents()>0)
                .map(e -> e.getKey())
                .collect(Collectors.toSet());
        this.shipBoard = shipBoard;
    }

    @Override
    public void chooseCrewToLose(ShipBoard shipBoard, Point position) {
        if (availablePositions.contains(position)) {
            shipBoard.getCabins().get(position).loseResidents(1);
        }
    }

    @Override
    public GameState getNextState() {
        return adventureCard.nextStep();
    }
}
