package it.polimi.ingsw.galaxytruckers.view.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;

/**
 * Represents a Star Dust adventure card in the game.
 * Extends {@link AdventureCard} and implements {@link AdventureCardInterface}.
 * Every player loses one flight day for each exposed connector.
 */
public final class StarDustCard extends AdventureCard implements AdventureCardInterface {
    /**
     * Constructs a StarDustCard with the specified level and identifier.
     *
     * @param level the level of the card
     * @param id the unique identifier of the card
     */
    public StarDustCard(Level level, int id) {
        super(level, id);
    }
}
