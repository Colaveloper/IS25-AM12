package it.polimi.ingsw.galaxytruckers.state;

import it.polimi.ingsw.galaxytruckers.Deck;

//TODO : implement this class
//TODO : determine a way to handle game end
public class DrawCardState extends GameState {
    boolean hasDrawn = false;

    @Override
    public boolean drawCard(Deck deck) {
        hasDrawn = true;
        return true;
        // return true if a card was drawn
    }

    @Override
    public GameState getNextState() {
        if (hasDrawn) {
            return adventureCard.nextStep();
        } else {
            throw new IllegalStateException("You have not drawn yet");
        }
    }
}
