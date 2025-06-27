package it.polimi.ingsw.galaxytruckers.client.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.shared.enums.Level;

/**
 * Represents an adventure card for an open space event in the game.
 * Extends {@link AdventureCard} and implements {@link AdventureCardInterface}.
 * Players move up on the flight board based on their ship's engine power.
 */
public final class OpenSpaceCard extends AdventureCard implements AdventureCardInterface {
    /**
     * Constructs an OpenSpaceCard with the specified level and unique identifier.
     *
     * @param level the level of the card
     * @param id the unique identifier for the card
     */
    public OpenSpaceCard (Level level, int id) {
        super(level, id);
    }
}
