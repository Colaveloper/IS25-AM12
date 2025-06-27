package it.polimi.ingsw.galaxytruckers.view.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;

import java.util.*;

/**
 * Represents an adventure card for a planets event in the game.
 * Stores the list of planets, each with a map of goods types to their quantities,
 * and the number of flight days lost when visiting the planets.
 * Extends {@link AdventureCard} and implements {@link AdventureCardInterface}.
 */
public final class PlanetsCard extends AdventureCard implements AdventureCardInterface {
    private final List<Map<GoodsType, Integer>> planets;
    private final int flightDaysLoss;

    /**
     * Constructs a PlanetsCard with the specified parameters.
     *
     * @param level the level of the card
     * @param planets the list of planets, each as a map of goods types to quantities
     * @param flightDaysLoss the number of flight days lost
     * @param id the unique identifier for the card
     */
    public PlanetsCard(Level level, List<Map<GoodsType, Integer>> planets, int flightDaysLoss, int id) {
        super(level, id);
        this.planets = planets;
        this.flightDaysLoss = flightDaysLoss;
    }

    /**
     * Returns the list of planets, each as a map of goods types to their quantities.
     *
     * @return the list of planets
     */
    public List<Map<GoodsType, Integer>> getPlanets() {
        return planets;
    }

    /**
     * Returns the number of flight days lost when visiting the planets.
     *
     * @return the number of flight days lost
     */
    public int getFlightDaysLoss() {
        return flightDaysLoss;
    }
}
