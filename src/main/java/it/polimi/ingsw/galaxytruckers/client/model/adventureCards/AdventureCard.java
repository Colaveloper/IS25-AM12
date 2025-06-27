package it.polimi.ingsw.galaxytruckers.client.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.shared.enums.Level;

/**
 * Abstract base class for all adventure cards in the game.
 * Stores the card's level and unique identifier.
 * Implements the {@link AdventureCardInterface}.
 */
public abstract non-sealed class AdventureCard implements AdventureCardInterface {
    private final Level cardLevel;
    private final int id;

    /**
     * Constructs an AdventureCard with the specified level and id.
     *
     * @param cardLevel the level of the adventure card
     * @param id the unique identifier for the card
     */
    protected AdventureCard(Level cardLevel, int id) {
        this.cardLevel = cardLevel;
        this.id = id;
    }

    /**
     * Returns the level of the adventure card.
     *
     * @return the card's level
     */
    public Level getCardLevel() {
        return cardLevel;
    }

    /**
     * Returns the unique identifier of the adventure card.
     *
     * @return the card's id
     */
    public int getId() {
        return id;
    }
}
