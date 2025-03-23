package it.polimi.ingsw.galaxytruckers.state;

import java.util.Set;
import java.util.function.Consumer;

public class GeneralChoiceState extends GameState {
    Consumer<Integer> action;
    Set<Integer> possibleChoices;
    boolean canContinue;

    public GeneralChoiceState(Consumer<Integer> action, Set<Integer> possibleChoices, boolean isOptional) {
        this.action = action;
        this.possibleChoices = possibleChoices;
        this.canContinue = isOptional;
    }

    @Override
    public void makeChoice(int choice) {
        if (!possibleChoices.contains(choice)) {
            throw new IllegalArgumentException("Invalid choice: " + choice);
        }
        action.accept(choice);
    }

    @Override
    public GameState getNextState() {
        return adventureCard.nextStep();
    }
}
