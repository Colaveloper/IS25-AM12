package it.polimi.ingsw.galaxytruckers.state;

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
