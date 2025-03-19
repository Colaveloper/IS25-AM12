package it.polimi.ingsw.galaxytruckers.adventureCards.utils;

import it.polimi.ingsw.galaxytruckers.FlightBoard;
import it.polimi.ingsw.galaxytruckers.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.state.GameState;

public abstract class AdventureCard {
    protected FlightBoard flightBoard;
    protected int currentPlayerIndex;
    protected final Level cardLevel;

    protected AdventureCard(Level cardLevel) {
        this.cardLevel = cardLevel;
    }

    public abstract GameState nextStep();

    // Does nothing - does not throw exceptions because I control correct
    // method invocation through GameState
    public void choose(boolean choice) {}
}
