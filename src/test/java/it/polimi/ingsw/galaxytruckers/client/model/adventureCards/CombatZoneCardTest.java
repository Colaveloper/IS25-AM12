package it.polimi.ingsw.galaxytruckers.client.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.shared.enums.Level;
import it.polimi.ingsw.galaxytruckers.client.model.adventureCards.penalty.CrewLoss;
import it.polimi.ingsw.galaxytruckers.client.model.adventureCards.penalty.Penalty;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CombatZoneCardTest {

    @Test
    void getChecksReturnsCorrectChecks() {
        List<CombatZoneCheck> checks = List.of(CombatZoneCheck.CREWSIZE, CombatZoneCheck.FIREPOWER);
        CombatZoneCard card = new CombatZoneCard(Level.SECOND, checks, List.of(), 7);
        assertEquals(checks, card.getChecks());
    }

    @Test
    void getPenaltiesReturnsCorrectPenalties() {
        Penalty penalty1 = new CrewLoss(2);
        Penalty penalty2 = new CrewLoss(3);
        List<Penalty> penalties = List.of(penalty1, penalty2);
        CombatZoneCard card = new CombatZoneCard(Level.FIRST, List.of(), penalties, 8);
        assertEquals(penalties, card.getPenalties());
    }

    @Test
    void testInheritanceAndId() {
        CombatZoneCard card = new CombatZoneCard(Level.FIRST, List.of(), List.of(), 99);
        assertEquals(Level.FIRST, card.getCardLevel());
        assertEquals(99, card.getId());
    }

    @Test
    void getChecksEmptyList() {
        CombatZoneCard card = new CombatZoneCard(Level.TEST, List.of(), List.of(), 1);
        assertTrue(card.getChecks().isEmpty());
    }

    @Test
    void getPenaltiesEmptyList() {
        CombatZoneCard card = new CombatZoneCard(Level.TEST, List.of(), List.of(), 2);
        assertTrue(card.getPenalties().isEmpty());
    }
}