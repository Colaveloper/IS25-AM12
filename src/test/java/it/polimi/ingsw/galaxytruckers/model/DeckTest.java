package it.polimi.ingsw.galaxytruckers.model;

import it.polimi.ingsw.galaxytruckers.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.CombatZoneCard;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.check.CrewSizeCheck;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.check.EnginePowerCheck;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.check.FirePowerCheck;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DeckTest {
    Deck deck;
    Game game;

    @BeforeEach
    void setup(){
        game = new Game(Level.SECOND);
    }

    @Test
    void loadComponents() throws IOException {
        List<AdventureCard> allCards = Deck.loadRelevantCards(game);
        List<CombatZoneCard> combatZoneCards = allCards.stream()
                .filter(c -> c instanceof CombatZoneCard)
                .map(c -> (CombatZoneCard) c)
                .toList();
        CombatZoneCard combatZoneCard1 = combatZoneCards.get(0);
        CombatZoneCard combatZoneCard2 = combatZoneCards.get(1);
        assertEquals(List.of(CrewSizeCheck.getInstance(),
                EnginePowerCheck.getInstance(),
                FirePowerCheck.getInstance()), combatZoneCard1.getChecks());

    }
}