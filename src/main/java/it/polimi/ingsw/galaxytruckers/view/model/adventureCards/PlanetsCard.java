package it.polimi.ingsw.galaxytruckers.view.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;

import java.util.*;

public final class PlanetsCard extends AdventureCard {
    private final List<Map<GoodsType, Integer>> planets;
    private final int flightDaysLoss;

    public PlanetsCard(Level level, List<Map<GoodsType, Integer>> planets, int flightDaysLoss, int id) {
        super(level, id);
        this.planets = planets;
        this.flightDaysLoss = flightDaysLoss;
    }

    public List<Map<GoodsType, Integer>> getPlanets() {
        return planets;
    }

    public int getFlightDaysLoss() {
        return flightDaysLoss;
    }
}
