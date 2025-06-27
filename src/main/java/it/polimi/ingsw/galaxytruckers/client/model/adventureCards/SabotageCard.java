package it.polimi.ingsw.galaxytruckers.client.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.shared.enums.Level;

/**
 * Represents an adventure card for a sabotage event in the game.
 * Extends {@link AdventureCard} and implements {@link AdventureCardInterface}.
 * Destroys a random component of the player's ship with the least amount of crew.
 */
public final class SabotageCard extends AdventureCard implements AdventureCardInterface {
    /**
     * Constructs a SabotageCard with the specified level and unique identifier.
     *
     * @param level the level of the card
     * @param id the unique identifier for the card
     */
    public SabotageCard(Level level, int id) {
        super(level, id);
    }
}
