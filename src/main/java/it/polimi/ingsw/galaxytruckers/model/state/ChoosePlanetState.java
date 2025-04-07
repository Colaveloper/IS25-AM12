package it.polimi.ingsw.galaxytruckers.model.state;

import java.util.Set;
import java.util.function.Consumer;

public class ChoosePlanetState extends GameState {
    Consumer<Integer> choosePlanetMethod;
    Set<Integer> options;

    public ChoosePlanetState(Consumer<Integer> choosePlanetMethod, Set<Integer> options) {
        this.choosePlanetMethod = choosePlanetMethod;
        this.options = options;
    }

    @Override
    public void selectOption(int choice) {
        if (!options.contains(choice)) {
            throw new IllegalArgumentException("Invalid choice: " + choice);
        }
        choosePlanetMethod.accept(choice);
        goNext();
    }

    @Override
    public void goNext() {
        game.setCurrentState(game.getDeck().getCurrentCard().nextStep());
    }
}
