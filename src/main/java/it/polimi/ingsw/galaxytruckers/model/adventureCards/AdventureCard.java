package it.polimi.ingsw.galaxytruckers.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.AdventureState;
import it.polimi.ingsw.galaxytruckers.model.state.GameState;

/**
 * Abstract class representing an adventure card in the game.
 * It provides methods to initialize the card and retrieve its level and ID.
 */
public abstract class AdventureCard {
    protected int currentPlayerIndex;
    protected ShipBoard currentShipBoard;
    protected FlightBoard flightBoard;
    protected final Level cardLevel;
    protected final Game game;
    protected final int id;

    /**
     * Constructor for the AdventureCard class.
     *
     * @param game      The game instance this card belongs to.
     * @param cardLevel The level of the card.
     * @param id        The unique identifier for the card.
     */
    protected AdventureCard(Game game, Level cardLevel, int id) {
        this.game = game;
        this.cardLevel = cardLevel;
        this.id = id;
    }

    /**
     * Initializes the adventure card by setting the flight board and resetting the current ship board and player index.
     */
    public void initialize() {
        this.flightBoard = game.getFlightBoard();
        this.currentShipBoard = null;
        this.currentPlayerIndex = 0;
    }

    public Level getCardLevel() {
        return cardLevel;
    }

    /**
     * Abstract method to get the next state of the adventure card, based on the rules of the card,
     * and the state of the ships.
     * This method can call recursively and make use of local subclasses variables.
     *
     * @return The next AdventureState that the game should transition to.
     */
    public abstract AdventureState getNextState();

    /**
     * Returns the unique identifier of this adventure card.
     *
     * @return the card's unique ID
     */
    public int getId() {
        return id;
    }
}
