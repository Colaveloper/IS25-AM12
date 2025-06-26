package it.polimi.ingsw.galaxytruckers.view.model.adventureCards;

/**
 * Interface for all adventure cards in the game.
 * This interface is used to group all types of adventure cards together.
 * It allows for polymorphism when handling different adventure card types.
 */
public sealed interface AdventureCardInterface permits
        AdventureCard,
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
        StarDustCard {
}
