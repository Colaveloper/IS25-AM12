package it.polimi.ingsw.galaxytruckers.state;

import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ChooseCrewToLoseState extends GameState {
    private final Set<Point> availablePositions;
    private final ShipBoard shipBoard;

    public ChooseCrewToLoseState(ShipBoard shipBoard) {
        this.availablePositions = shipBoard.getCabins().entrySet().stream()
                .filter(e -> e.getValue().getNumResidents()>0)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
        this.shipBoard = shipBoard;
    }

    @Override
    public void chooseCrewToLose(Point position) {
        if (availablePositions.contains(position)) {
            adventureCard.loseCrew();
            shipBoard.loseCrew(position, 1);
        } else {
            throw new IllegalArgumentException("No crewed cabin at that position");
        }
    }

    @Override
    public GameState getNextState() {
        return adventureCard.nextStep();
    }
}
