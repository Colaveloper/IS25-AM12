package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.util.Set;
import java.util.function.Consumer;

public class ChoosePlanetState extends AdventureState {
    ShipBoard shipBoard;
    Consumer<Integer> choosePlanetMethod;
    Set<Integer> options;

    public ChoosePlanetState(ShipBoard shipBoard, Consumer<Integer> choosePlanetMethod, Set<Integer> options) {
        this.choosePlanetMethod = choosePlanetMethod;
        this.options = options;
        this.shipBoard = shipBoard;
    }

    @Override
    public void choosePlanet(ShipBoard shipBoard, int choice) {
        if (!shipBoard.equals(this.shipBoard)) {
            throw new IllegalStateException("It's not your turn");
        }
        if (!options.contains(choice)) {
            throw new IllegalArgumentException("Invalid choice: " + choice);
        }
        choosePlanetMethod.accept(choice);
        game.setCurrentState(super.getNextState());
    }

    @Override
    public void goNext(ShipBoard shipBoard) {
        if (!shipBoard.equals(this.shipBoard)) {
            throw new IllegalStateException("It's not your turn");
        }
        game.setCurrentState(super.getNextState());
    }
}
