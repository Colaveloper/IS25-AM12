package it.polimi.ingsw.galaxytruckers.client.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.shared.enums.GoodsType;
import it.polimi.ingsw.galaxytruckers.shared.enums.Level;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PlanetsCardTest {
    @Test
    void getPlanets() {
        List<Map<GoodsType, Integer>> planets = new ArrayList<>();
        Map<GoodsType, Integer> planet1 = new EnumMap<>(GoodsType.class);
        planet1.put(GoodsType.RED, 2);
        planet1.put(GoodsType.BLUE, 1);
        Map<GoodsType, Integer> planet2 = new EnumMap<>(GoodsType.class);
        planet2.put(GoodsType.YELLOW, 3);
        planets.add(planet1);
        planets.add(planet2);
        PlanetsCard card = new PlanetsCard(Level.FIRST, planets, 2, 42);
        assertEquals(planets, card.getPlanets());
        assertEquals(2, card.getPlanets().size());
        assertEquals(2, card.getPlanets().get(0).get(GoodsType.RED));
        assertEquals(1, card.getPlanets().get(0).get(GoodsType.BLUE));
        assertEquals(3, card.getPlanets().get(1).get(GoodsType.YELLOW));
    }

    @Test
    void getFlightDaysLoss() {
        PlanetsCard card = new PlanetsCard(Level.SECOND, Collections.emptyList(), 5, 99);
        assertEquals(5, card.getFlightDaysLoss());
    }

    @Test
    void getCardLevelAndId() {
        PlanetsCard card = new PlanetsCard(Level.TEST, Collections.emptyList(), 0, 7);
        assertEquals(Level.TEST, card.getCardLevel());
        assertEquals(7, card.getId());
    }
}