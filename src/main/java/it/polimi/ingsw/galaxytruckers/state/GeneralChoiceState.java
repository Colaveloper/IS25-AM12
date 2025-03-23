package it.polimi.ingsw.galaxytruckers.state;

import java.util.function.Consumer;

public class GeneralChoiceState extends GameState {
    Consumer<Integer> action;
    int numChoices;

    public GeneralChoiceState(Consumer<Integer> action, int numChoices) {
        this.action = action;
        this.numChoices = numChoices;
    }

    @Override
    public void makeChoice(int choice) {
        action.accept(choice);
    }

    @Override
    public GameState getNextState() {
        return adventureCard.nextStep();
    }
}
