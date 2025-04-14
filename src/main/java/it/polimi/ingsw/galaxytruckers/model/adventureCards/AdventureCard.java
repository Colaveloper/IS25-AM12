package it.polimi.ingsw.galaxytruckers.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.GameState;

public abstract class AdventureCard {
    protected FlightBoard flightBoard;
    protected int currentPlayerIndex;
    protected ShipBoard currentShipBoard;
    protected final Level cardLevel;


    protected AdventureCard(Level cardLevel) {
        this.cardLevel = cardLevel;
    }

    public void initialize(FlightBoard flightBoard) {
        this.flightBoard = flightBoard;
        this.currentShipBoard = null;
        this.currentPlayerIndex = 0;
    }

    public Level getCardLevel() {
        return cardLevel;
    }

    public abstract GameState nextStep();
}
