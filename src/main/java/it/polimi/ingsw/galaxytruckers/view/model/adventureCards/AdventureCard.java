package it.polimi.ingsw.galaxytruckers.view.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;

public abstract sealed class AdventureCard permits
        AbandonedShipCard,
        AbandonedStationCard,
        CombatZoneCard,
        EpidemicCard,
        MeteorSwarmCard,
        OpenSpaceCard,
        PiratesCard,
        PlanetsCard,
        SabotageCard,
        SlaversCard,
        SmugglersCard,
        StarDustCard
{
    private final Level cardLevel;
    private final int id;

    protected AdventureCard(Level cardLevel, int id) {
        this.cardLevel = cardLevel;
        this.id = id;
    }

    public Level getCardLevel() {
        return cardLevel;
    }

    public int getId() {
        return id;
    }
}
