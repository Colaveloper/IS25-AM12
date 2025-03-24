package it.polimi.ingsw.galaxytruckers.adventureCards;

import it.polimi.ingsw.galaxytruckers.FlightBoard;
import it.polimi.ingsw.galaxytruckers.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;
import javafx.scene.image.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EpidemicCardTest extends AdventureCardTestInitializer{

    EpidemicCard epidemicCard;

    @BeforeEach
    void setUp() {
        super.setUp();
        
        //epidemicCard = new EpidemicCard(null, Level.SECOND, flightBoard);

    }

    @Test
    void nextStep() {
    }
}