package it.polimi.ingsw.galaxytruckers.adventureCards.utils;

import it.polimi.ingsw.galaxytruckers.FlightBoard;
import it.polimi.ingsw.galaxytruckers.state.GameState;

public abstract class AdventureCard {
    protected FlightBoard flightBoard;
    protected int currentPlayerIndex;

    public abstract GameState nextStep();

    // Does nothing - does not throw exceptions because I control correct
    // method invocation through GameState
    public void choose(boolean choice) {}
}
