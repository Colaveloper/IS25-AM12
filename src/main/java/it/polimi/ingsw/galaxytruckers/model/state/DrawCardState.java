package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.Deck;

//TODO : determine a way to handle game end
public class DrawCardState extends GameState {

    @Override
    public void drawCard(Deck deck) {
        if(game.getDeck().tryDrawCard()) {
            game.setCurrentState(game.getDeck().getCurrentCard().nextStep());
        } else {
            // TODO: game over, compute scores and show them to players
        }
    }
}
