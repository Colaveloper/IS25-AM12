package it.polimi.ingsw.galaxytruckers.view;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlanetsTest {

    @Test
    void testDescribePlanetsWithLandings() throws IOException {
        // init
        String player1 = "froopy";
        AdventureCard currentCard = new AdventureCard(16);
        Planets planets = new Planets(currentCard);

        // simulate a landing on second planet
        if(planets.landPlayerOnPlanet(player1, 2)){
            System.out.println("Landing on planet...");
        }
        else{
            System.out.println("Planet already taken!");
        }

        List<String> expected = List.of(
                "planet 1: blue, blue, blue, green, red — free",
                "planet 2: blue, yellow, red — claimed by froopy",
                "planet 3: green, red — free"
        );

        assertEquals(expected, planets.describePlanets());
    }
}