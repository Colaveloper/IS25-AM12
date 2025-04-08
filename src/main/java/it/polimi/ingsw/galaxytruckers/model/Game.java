package it.polimi.ingsw.galaxytruckers.model;

import it.polimi.ingsw.galaxytruckers.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.model.state.GameState;

public class Game {
    Deck deck;
    FlightBoard flightBoard;
    GameState currentState;

    public void setCurrentState(GameState state) {}

    public Deck getDeck() {
        return deck;
    }
}
