package it.polimi.ingsw.galaxytruckers.view.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.penalty.Penalty;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;

import java.util.List;

public final class CombatZoneCard extends AdventureCard implements AdventureCardInterface {

    private final List<CombatZoneCheck> checks;
    private final List<Penalty> penalties;

    public CombatZoneCard(Level level, List<CombatZoneCheck> checks, List<Penalty> penalties, int id) {
        super(level, id);
        this.checks = checks;
        this.penalties = penalties;
    }

    public List<CombatZoneCheck> getChecks() {
        return checks;
    }

    public List<Penalty> getPenalties() {
        return penalties;
    }
}
