package it.polimi.ingsw.galaxytruckers.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.GameState;

public abstract class AdventureCard {
    protected int currentPlayerIndex;
    protected ShipBoard currentShipBoard;
    protected FlightBoard flightBoard;
    protected final Level cardLevel;
    protected final Game game;
    protected final int id;


    protected AdventureCard(Game game, Level cardLevel, int id) {
        this.game = game;
        this.cardLevel = cardLevel;
        this.id = id;
    }

    public void initialize() {
        this.flightBoard = game.getFlightBoard();
        this.currentShipBoard = null;
        this.currentPlayerIndex = 0;
    }

    public Level getCardLevel() {
        return cardLevel;
    }

    public abstract GameState nextStep();
}
