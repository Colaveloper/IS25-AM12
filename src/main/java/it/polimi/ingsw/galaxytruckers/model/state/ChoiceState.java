package it.polimi.ingsw.galaxytruckers.model.state;

public class ChoiceState extends GameState {
    boolean choice;

    @Override
    public void makeBooleanChoice(boolean choice) {
        this.choice = choice;
    }

    @Override
    public GameState getNextState() {
        adventureCard.choose(choice);
        return adventureCard.nextStep();
    }
}
