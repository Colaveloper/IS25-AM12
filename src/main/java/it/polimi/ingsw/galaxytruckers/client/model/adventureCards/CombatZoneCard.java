package it.polimi.ingsw.galaxytruckers.client.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.client.model.adventureCards.penalty.Penalty;
import it.polimi.ingsw.galaxytruckers.shared.enums.Level;

import java.util.List;

/**
 * Represents an adventure card for a combat zone event in the game.
 * Stores the list of combat zone checks and associated penalties.
 * Extends {@link AdventureCard} and implements {@link AdventureCardInterface}.
 */
public final class CombatZoneCard extends AdventureCard implements AdventureCardInterface {

    private final List<CombatZoneCheck> checks;
    private final List<Penalty> penalties;

    /**
     * Constructs a CombatZoneCard with the specified parameters.
     *
     * @param level the level of the card
     * @param checks the list of combat zone checks
     * @param penalties the list of penalties
     * @param id the unique identifier for the card
     */
    public CombatZoneCard(Level level, List<CombatZoneCheck> checks, List<Penalty> penalties, int id) {
        super(level, id);
        this.checks = checks;
        this.penalties = penalties;
    }

    /**
     * Returns the list of combat zone checks for this card.
     *
     * @return the list of combat zone checks
     */
    public List<CombatZoneCheck> getChecks() {
        return checks;
    }

    /**
     * Returns the list of penalties associated with this card.
     *
     * @return the list of penalties
     */
    public List<Penalty> getPenalties() {
        return penalties;
    }
}
