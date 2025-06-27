package it.polimi.ingsw.galaxytruckers.view.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;

/**
 * Represents an adventure card for an epidemic event in the game.
 * Extends {@link AdventureCard} and implements {@link AdventureCardInterface}.
 * Causes the player to lose crew members that are connected to aliens.
 */
public final class EpidemicCard extends AdventureCard implements AdventureCardInterface {
    /**
     * Constructs an EpidemicCard with the specified level and unique identifier.
     *
     * @param level the level of the card
     * @param id the unique identifier for the card
     */
    public EpidemicCard(Level level, int id) {
        super(level, id);
    }
}
